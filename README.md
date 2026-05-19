# Global Dam Watch Visualization Backend

这是 Global Dam Watch 可视化项目的 Spring Boot 后端服务，主要为前端提供水库与大坝数据查询、空间位置返回、统计汇总、筛选同步，以及多 Provider 的 LLM 请求代理能力。

## 技术栈

- Java 21
- Spring Boot 3.4.12
- Spring Web / Spring WebFlux
- MyBatis-Plus 3.5.14
- PostgreSQL / PostGIS
- Lombok
- Maven

## 项目结构

```text
backend-springboot/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/org/example/backendspringboot/
│   │   │   ├── BackendSpringbootApplication.java
│   │   │   ├── common/        # 通用响应、区间、统计与 LLM Provider 配置模型
│   │   │   ├── config/        # WebClient 与 LLM Provider Bean 配置
│   │   │   ├── controller/    # Reservoir、Barrier、LLM 代理接口
│   │   │   ├── dto/           # 查询表单与统计结果 DTO
│   │   │   ├── entity/        # gdw_reservoir、gdw_barrier 表实体
│   │   │   ├── mapper/        # MyBatis-Plus Mapper 与自定义 SQL
│   │   │   ├── service/       # 业务接口
│   │   │   ├── service/impl/  # 查询、筛选、统计业务实现
│   │   │   └── util/          # 统计工具方法
│   │   └── resources/
│   │       ├── application.yml
│   │       └── application-dev.yml  # 本地开发配置，已被 .gitignore 忽略
│   └── test/
└── target/
```

## 环境要求

- JDK 21
- Maven（项目未包含 Maven Wrapper，需要本机已安装 `mvn`）
- PostgreSQL，并启用 PostGIS 能力
- 数据库中存在以下表：
  - `public.gdw_reservoir`
  - `public.gdw_barrier`

服务默认端口为 `8100`。

## 配置说明

主配置文件 `src/main/resources/application.yml` 默认启用 `dev` profile，并通过占位符读取数据库和 LLM Provider 配置：

```yaml
server:
  port: 8100

postgresql:
  url: jdbc:postgresql://localhost:5432/your_database
  username: your_username
  password: your_password

llm-config:
  cloud:
    base-url: https://api.example.com/v1
    api-key: your_cloud_api_key
  local:
    base-url: http://localhost:11434/v1
    api-key: your_local_api_key
  chat:
    base-url: https://api.example.com/v1
    api-key: your_chat_api_key
  rag:
    base-url: https://api.example.com/v1
    api-key: your_rag_api_key
```

建议将以上内容放在本地 `src/main/resources/application-dev.yml` 中。该文件已在 `.gitignore` 中忽略，适合存放本地数据库地址和 API Key。

也可以通过环境变量或 JVM 参数提供这些配置，例如：

```powershell
$env:POSTGRESQL_URL="jdbc:postgresql://localhost:5432/your_database"
$env:POSTGRESQL_USERNAME="your_username"
$env:POSTGRESQL_PASSWORD="your_password"
```

## 启动项目

在项目根目录执行：

```bash
mvn spring-boot:run
```

打包：

```bash
mvn clean package
```

运行打包产物：

```bash
java -jar target/backend-springboot-0.0.1-SNAPSHOT.jar
```

启动后接口基础地址为：

```text
http://localhost:8100
```

## 接口说明

所有水库与大坝业务接口默认返回统一结构：

```json
{
  "code": 1,
  "message": "success",
  "data": {}
}
```

### 水库接口

基础路径：`/reservoir`

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| `GET` | `/reservoir/location` | 获取水库空间位置列表，返回 `gdwId`、名称、年份和 GeoJSON 几何字段 |
| `GET` | `/reservoir/{id}` | 按 `gdw_id` 查询水库详情 |
| `POST` | `/reservoir/position` | 按水库名称模糊查询一条记录，请求体为纯文本名称 |
| `POST` | `/reservoir/filter` | 按 SQL 条件字符串筛选水库位置数据 |
| `POST` | `/reservoir/stats` | 按筛选表单返回统计结果 |
| `POST` | `/reservoir/sync` | 按筛选表单返回同步后的水库空间位置列表 |

`/reservoir/stats` 和 `/reservoir/sync` 请求体示例：

```json
{
  "capMcm": { "eq": -1, "gt": -1, "ge": 10, "lt": -1, "le": 1000 },
  "areaSkm": null,
  "mainBasin": ["Yangtze"],
  "mainUse": ["Irrigation"],
  "quality": ["Good"],
  "year": [1950, 2020],
  "country": ["China"],
  "depthM": null
}
```

### 大坝接口

基础路径：`/barrier`

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| `GET` | `/barrier/location` | 获取大坝空间位置列表，返回 `gdwId`、名称、类型、年份和 GeoJSON 几何字段 |
| `GET` | `/barrier/{id}` | 按 `gdw_id` 查询大坝详情 |
| `POST` | `/barrier/position` | 按大坝名称模糊查询一条记录，请求体为纯文本名称 |
| `POST` | `/barrier/filter` | 按 SQL 条件字符串筛选大坝位置数据 |
| `POST` | `/barrier/stats` | 按筛选表单返回统计结果 |
| `POST` | `/barrier/sync` | 按筛选表单返回同步后的大坝空间位置列表 |

`/barrier/stats` 和 `/barrier/sync` 请求体示例：

```json
{
  "damType": ["Concrete"],
  "damLenM": null,
  "mainBasin": ["Yangtze"],
  "mainUse": ["Hydroelectricity"],
  "quality": ["Good"],
  "year": [1950, 2020],
  "country": ["China"],
  "damHgtM": { "eq": -1, "gt": 20, "ge": -1, "lt": -1, "le": -1 }
}
```

区间字段使用 `RangeData`：

| 字段 | 含义 |
| --- | --- |
| `eq` | 等于 |
| `gt` | 大于 |
| `ge` | 大于等于 |
| `lt` | 小于 |
| `le` | 小于等于 |

数值为 `-1` 时表示不启用该条件。

### LLM 代理接口

基础路径：`/api/llm`

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| `POST` | `/api/llm/{provider}/chat/completions` | 将请求体转发到指定 Provider 的 `/chat/completions` |
| `POST` | `/api/llm/{provider}/embeddings` | 将请求体转发到指定 Provider 的 `/embeddings` |

`provider` 来自 `llm.providers` 配置，目前默认包含：

- `cloud`
- `local`
- `chat`
- `rag`

请求体会原样转发给目标 Provider，并自动添加：

```text
Authorization: Bearer <api-key>
Content-Type: application/json
```

## 数据库说明

实体类映射到：

- `Reservoir` -> `public.gdw_reservoir`
- `Barrier` -> `public.gdw_barrier`

Mapper 中使用 `ST_AsGeoJSON(wkb_geometry)` 将 PostGIS 几何字段转换为 GeoJSON 字符串，前端可直接用于地图渲染。

`/filter` 接口当前接收 SQL 条件字符串，并通过 Mapper 拼接到查询条件中。该接口应只暴露给可信调用方，生产环境建议在调用层限制可用条件或改为结构化筛选参数。

## 常用开发命令

```bash
# 启动服务
mvn spring-boot:run

# 编译并跳过测试
mvn -DskipTests package

# 运行测试
mvn test
```

## 开发约定

- Controller 层统一返回 `Result<T>`。
- 水库与大坝筛选逻辑分别位于 `ReservoirServiceImpl` 和 `BarrierServiceImpl`。
- 统计结果使用 `StatsData`，区间统计逻辑集中在 `StatsUtils`。
- 跨域当前通过 `@CrossOrigin(origins = "*")` 放开，生产环境可按前端域名收敛。
- 本地配置和密钥不要提交到仓库，优先放入被忽略的 `application-dev.yml` 或环境变量。

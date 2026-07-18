# Global Dam Watch Visualization Backend

Global Dam Watch（GDW）可视化项目的 Spring Boot 后端。服务基于 PostgreSQL/PostGIS 管理全球水库与大坝数据，为前端提供空间位置、详情查询、条件筛选、聚合统计和地图数据同步接口，同时提供兼容 OpenAI API 格式的多供应商 LLM 请求代理。

## 主要功能

- 水库（面要素）和大坝（点要素）的 GeoJSON 位置数据查询
- 按 GDW ID 查询完整属性
- 按名称模糊定位水库或大坝
- 按分类、年份和数值区间筛选数据
- 按容量、面积、用途、质量、年份、国家等维度生成统计结果
- 将筛选结果同步为地图所需的精简空间数据
- 将聊天补全和向量化请求代理到多个 OpenAI 兼容服务
- 提供可直接导入 PostgreSQL 的 GDW 数据脚本

## 技术栈

| 组件                 | 当前版本或用途       |
| -------------------- | -------------------- |
| Java                 | 21                   |
| Spring Boot          | 3.4.12               |
| Spring Web           | REST API             |
| Spring WebFlux       | `WebClient` LLM 代理 |
| MyBatis-Plus         | 3.5.14               |
| PostgreSQL / PostGIS | 业务数据与空间查询   |
| Lombok               | 1.18.42              |
| Maven                | 构建与依赖管理       |

项目未包含 Maven Wrapper，运行构建命令前需在本机安装 Maven。

## 项目结构

```text
backend-springboot/
├─ data/                       # 数据库建表、数据和索引脚本
├─ src/main/java/org/example/backendspringboot/
│  ├─ common/                  # 统一响应、区间、统计与 LLM 配置模型
│  ├─ config/                  # WebClient 配置
│  ├─ controller/              # 水库、大坝和 LLM 代理接口
│  ├─ dto/                     # 筛选表单与统计响应 DTO
│  ├─ entity/                  # gdw_reservoir、gdw_barrier 实体
│  ├─ mapper/                  # MyBatis-Plus Mapper 与空间 SQL
│  ├─ service/                 # 业务接口及实现
│  └─ util/                    # 统计与区间条件工具
├─ src/main/resources/
│  └─ application.yml          # 公共配置，默认启用 dev Profile
├─ pom.xml
└─ README.md
```

当前仓库未跟踪测试目录；`target/` 为 Maven 构建产物，不属于源码。

## 环境要求

- JDK 21
- Maven 3.9 或兼容版本
- PostgreSQL（数据脚本来源版本为 PostgreSQL 14）
- PostGIS 扩展
- 可选：一个或多个兼容 OpenAI `/chat/completions`、`/embeddings` 接口的 LLM 服务

应用默认监听 `8100` 端口，并在启动时启用 `dev` Profile。

## 数据库初始化

`data/` 中包含当前项目使用的两张表及数据：

| 表                     | 空间类型                  | 数据量 | 脚本                                                                 |
| ---------------------- | ------------------------- | -----: | -------------------------------------------------------------------- |
| `public.gdw_barrier`   | `POINT, SRID 4326`        | 41,145 | `gdw_barrier.sql`                                                    |
| `public.gdw_reservoir` | `MULTIPOLYGON, SRID 4326` | 35,295 | `gdw_reservoir_00_schema.sql`、`01`～`05_data.sql`、`99_indexes.sql` |

> 注意：建表脚本包含 `DROP TABLE IF EXISTS`，重复执行会删除同名表及其中已有数据。请先确认目标数据库并做好备份。

脚本中的主键默认值依赖 `gdw_barrier_ogc_fid_seq` 和 `gdw_reservoir_ogc_fid_seq`，但脚本本身不会创建这两个序列。首次导入前需要先创建 PostGIS 扩展和序列：

```sql
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE SEQUENCE IF NOT EXISTS public.gdw_barrier_ogc_fid_seq;
CREATE SEQUENCE IF NOT EXISTS public.gdw_reservoir_ogc_fid_seq;
```

使用 `psql` 导入时，按以下顺序执行：

```bash
psql -d gdw -f data/gdw_barrier.sql
psql -d gdw -f data/gdw_reservoir_00_schema.sql
psql -d gdw -f data/gdw_reservoir_01_data.sql
psql -d gdw -f data/gdw_reservoir_02_data.sql
psql -d gdw -f data/gdw_reservoir_03_data.sql
psql -d gdw -f data/gdw_reservoir_04_data.sql
psql -d gdw -f data/gdw_reservoir_05_data.sql
psql -d gdw -f data/gdw_reservoir_99_indexes.sql
```

其中 `99_indexes.sql` 应在全部水库数据导入后执行，以创建空间索引和主键约束。

## 应用配置

公共配置位于 `src/main/resources/application.yml`。该文件默认读取外部数据库和 LLM 参数，并启用 `dev` Profile。建议在本地新建 `src/main/resources/application-dev.yml`；该文件已被 `.gitignore` 忽略，不应提交数据库密码或 API Key。

```yaml
postgresql:
    url: jdbc:postgresql://localhost:5432/gdw
    username: postgres
    password: change_me

llm-config:
    cloud:
        base-url: https://api.example.com/v1
        api-key: change_me
    local:
        base-url: http://localhost:11434/v1
        api-key: change_me
    chat:
        base-url: https://api.example.com/v1
        api-key: change_me
    rag:
        base-url: https://api.example.com/v1
        api-key: change_me
```

也可以通过环境变量提供配置。Spring Boot 会将大写下划线形式映射到配置键：

```powershell
$env:POSTGRESQL_URL = "jdbc:postgresql://localhost:5432/gdw"
$env:POSTGRESQL_USERNAME = "postgres"
$env:POSTGRESQL_PASSWORD = "change_me"
$env:LLM_CONFIG_LOCAL_BASE_URL = "http://localhost:11434/v1"
$env:LLM_CONFIG_LOCAL_API_KEY = "change_me"
```

`application.yml` 当前固定声明了 `cloud`、`local`、`chat`、`rag` 四个 Provider，因此启动时四组占位符都需要能够解析。若只使用部分 Provider，可在本地 Profile 中覆盖 `llm.providers` 列表，或为其余 Provider 提供占位配置。

## 启动与构建

在项目根目录执行：

```bash
# 开发模式启动
mvn spring-boot:run

# 运行测试并打包
mvn clean package

# 运行构建产物
java -jar target/backend-springboot-0.0.1-SNAPSHOT.jar
```

服务启动后基础地址为：

```text
http://localhost:8100
```

## 统一响应格式

水库和大坝接口返回 `Result<T>`：

```json
{
    "code": 1,
    "message": "success",
    "data": {}
}
```

`code` 为 `1` 表示成功、`0` 表示错误。LLM 代理接口不使用该结构，而是直接透传上游服务的响应体和状态。

## 水库接口

基础路径：`/reservoir`

| 方法   | 路径                  | 请求体               | 返回数据                                            |
| ------ | --------------------- | -------------------- | --------------------------------------------------- |
| `GET`  | `/reservoir/location` | 无                   | 全部水库的 `gdwId`、`resName`、`yearDam` 和 GeoJSON |
| `GET`  | `/reservoir/{id}`     | 无                   | 指定 `gdw_id` 的水库详情及 GeoJSON                  |
| `POST` | `/reservoir/position` | 纯文本水库名称       | 模糊匹配到的第一条记录                              |
| `POST` | `/reservoir/filter`   | 纯文本 SQL 条件      | 满足条件的精简位置数据                              |
| `POST` | `/reservoir/stats`    | `ReservoirForm` JSON | 筛选后的数量与分组统计                              |
| `POST` | `/reservoir/sync`     | `ReservoirForm` JSON | 筛选后的地图位置数据                                |

`/stats` 和 `/sync` 请求示例：

```json
{
    "capMcm": { "eq": -1, "gt": -1, "ge": 10, "lt": -1, "le": 1000 },
    "areaSkm": null,
    "mainBasin": ["Yangtze"],
    "mainUse": ["Irrigation"],
    "quality": ["2: Good"],
    "year": [1950, 2020],
    "country": ["China"],
    "depthM": null
}
```

水库统计响应的 `data` 包含 `total`、`capMcm`、`areaSkm`、`mainUse`、`quality`、`yearDam` 和 `country`。每个统计项结构为 `{ "name": "分组", "value": 数量 }`。

## 大坝接口

基础路径：`/barrier`

| 方法   | 路径                | 请求体             | 返回数据                                                       |
| ------ | ------------------- | ------------------ | -------------------------------------------------------------- |
| `GET`  | `/barrier/location` | 无                 | 全部大坝的 `gdwId`、`damName`、`damType`、`yearDam` 和 GeoJSON |
| `GET`  | `/barrier/{id}`     | 无                 | 指定 `gdw_id` 的大坝详情                                       |
| `POST` | `/barrier/position` | 纯文本大坝名称     | 模糊匹配到的第一条记录                                         |
| `POST` | `/barrier/filter`   | 纯文本 SQL 条件    | 满足条件的精简位置数据                                         |
| `POST` | `/barrier/stats`    | `BarrierForm` JSON | 筛选后的数量与分组统计                                         |
| `POST` | `/barrier/sync`     | `BarrierForm` JSON | 筛选后的地图位置数据                                           |

`/stats` 和 `/sync` 请求示例：

```json
{
    "damType": ["Dam"],
    "damLenM": null,
    "mainBasin": ["Yangtze"],
    "mainUse": ["Hydroelectricity"],
    "quality": ["2: Good"],
    "year": [1950, 2020],
    "country": ["China"],
    "damHgtM": { "eq": -1, "gt": 20, "ge": -1, "lt": -1, "le": -1 }
}
```

大坝统计响应的 `data` 包含 `total`、`damType`、`powerMw`、`mainUse`、`quality`、`yearDam` 和 `country`。

### 区间筛选规则

`capMcm`、`areaSkm`、`depthM`、`damLenM` 和 `damHgtM` 使用相同的区间对象：

| 字段 | 含义     |
| ---- | -------- |
| `eq` | 等于     |
| `gt` | 大于     |
| `ge` | 大于等于 |
| `lt` | 小于     |
| `le` | 小于等于 |

值为 `-1` 表示不启用对应条件；整个对象为 `null` 表示不对该字段筛选。`year` 使用两个元素表示包含边界的起止年份。

### 原始条件筛选的安全提示

`/reservoir/filter` 和 `/barrier/filter` 会将请求体作为 SQL `WHERE` 条件直接拼接到查询中，例如 `country = 'China' AND year_dam >= 2000`。这两个接口存在 SQL 注入风险，只应向可信调用方开放。生产环境应增加鉴权、条件白名单，或改为结构化筛选参数。

## LLM 代理接口

基础路径：`/api/llm`

| 方法   | 路径                                   | 说明                                   |
| ------ | -------------------------------------- | -------------------------------------- |
| `POST` | `/api/llm/{provider}/chat/completions` | 转发到 Provider 的 `/chat/completions` |
| `POST` | `/api/llm/{provider}/embeddings`       | 转发到 Provider 的 `/embeddings`       |

当前公共配置声明的 `provider` 值为 `cloud`、`local`、`chat` 和 `rag`。请求体会原样转发，代理自动添加以下请求头：

```text
Authorization: Bearer <provider-api-key>
Content-Type: application/json
```

`base-url` 应包含服务的公共 API 前缀（通常为 `/v1`），但不要包含 `/chat/completions` 或 `/embeddings`。未知 Provider 会返回 HTTP 400。

## 当前实现说明

- `gdw_reservoir.wkb_geometry` 通过 `ST_AsGeoJSON` 转换为 GeoJSON 字符串；水库详情也包含空间数据。
- `gdw_barrier.wkb_geometry` 在位置与筛选接口中转换为 GeoJSON；当前大坝详情查询不选择该字段。
- 分类统计会过滤空字符串和数值 `-99`；区间统计也会忽略 `-99`。
- 容量、面积和装机功率的统计区间为 `<1`、`1-10`、`10-100`、`100-1000`、`>1000`。
- 年份统计区间为 `<1900`、`1900-1920`、`1920-1940`、`1940-1960`、`1960-1980`、`1980-2000`、`>2000`。
- MyBatis SQL 日志当前输出到标准输出，生产环境建议调整日志级别或关闭 `StdOutImpl`。
- 所有 Controller 当前通过 `@CrossOrigin(origins = "*")` 开放跨域，生产部署时应限制为实际前端域名。
- 项目当前没有认证、授权和全局异常处理，请勿直接将管理能力暴露到公网。

## 常用命令

```bash
# 编译并运行测试
mvn test

# 跳过测试打包
mvn -DskipTests package

# 查看依赖树
mvn dependency:tree
```

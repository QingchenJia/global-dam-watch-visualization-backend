package org.example.backendspringboot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootTest
class BackendSpringbootApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void name() {
        String API_URL = "https://api.siliconflow.cn/v1/chat/completions";
        String API_KEY = "sk-pdjrkpkvrejfxxdumvfwvidlhtwjxlzvtrxawjomhxpmsznl"; // 替换为你的API密钥


        try {
            // 创建HTTP客户端
            HttpClient client = HttpClient.newHttpClient();
            String model = "Qwen/Qwen2.5-7B-Instruct";

            // 构建请求体
            String requestBody = String.format(
                    "{\"model\": \"%s\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}], \"stream\": false}",
                    model, "你好，请介绍一下你自己");

            // 创建请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // 发送请求并获取响应
            HttpResponse<String> response = null;
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // 解析响应
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = null;
                try {
                    jsonNode = mapper.readTree(response.body());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(jsonNode.get("choices").get(0).get("message").get("content").asText());
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}

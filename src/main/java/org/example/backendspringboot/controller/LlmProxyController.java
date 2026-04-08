package org.example.backendspringboot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/llm")
@CrossOrigin(origins = "*")
@Slf4j
public class LlmProxyController {

    @Autowired
    private Map<String, WebClient> llmWebClients;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/{provider}/chat/completions")
    public Mono<ResponseEntity<String>> chat(
            @PathVariable String provider,
            @RequestBody String body
    ) throws JsonProcessingException {
        WebClient client = llmWebClients.get(provider);

        if (client == null) {
            return Mono.just(ResponseEntity.badRequest()
                    .body("{\"error\": \"Unknown provider: " + provider + "\"}"));
        }

        Mono<ResponseEntity<String>> mono = client.post()
                .uri("/chat/completions")
                .bodyValue(body)
                .retrieve()
                .toEntity(String.class);

        // 记录llm代理请求日志以显示代理请求的模式和使用的大模型
        log.info(
                "[LLM]请求成功！模式：{}|模型：{}",
                provider,
                objectMapper.readTree(body).get("model").asText()
        );

        return mono;
    }
}

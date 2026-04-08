package org.example.backendspringboot.config;

import org.example.backendspringboot.common.LlmProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebClientConfig {

    @Bean
    @ConfigurationProperties(prefix = "llm")
    public LlmProperties llmProperties() {
        return new LlmProperties();
    }

    @Bean
    public Map<String, WebClient> llmWebClients(LlmProperties properties) {
        Map<String, WebClient> clients = new HashMap<>();

        properties.getProviders().forEach(config -> {
            WebClient client = WebClient.builder()
                    .baseUrl(config.getBaseUrl())
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + config.getApiKey())
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .build();

            clients.put(config.getName(), client);
        });
        return clients;
    }
}

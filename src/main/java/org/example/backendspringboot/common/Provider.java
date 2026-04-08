package org.example.backendspringboot.common;

import lombok.Data;

@Data
public class Provider {

    private String name;

    private String baseUrl;

    private String apiKey;
}
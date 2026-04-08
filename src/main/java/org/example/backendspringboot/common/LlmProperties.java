package org.example.backendspringboot.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LlmProperties {

    private List<Provider> providers = new ArrayList<>();
}

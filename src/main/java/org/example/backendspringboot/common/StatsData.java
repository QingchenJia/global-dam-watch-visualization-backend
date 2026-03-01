package org.example.backendspringboot.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsData {

    private String name;

    private int value;
}

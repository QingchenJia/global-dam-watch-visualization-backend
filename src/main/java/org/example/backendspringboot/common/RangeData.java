package org.example.backendspringboot.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RangeData {

    private int eq;

    private int gt;

    private int ge;

    private int lt;

    private int le;
}

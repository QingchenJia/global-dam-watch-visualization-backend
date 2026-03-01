package org.example.backendspringboot.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RangeData {

    /*
     * 所有字段默认设置为-1
     * 解析处理时忽略值为-1的字段
     * */
    private int eq = -1;

    private int gt = -1;

    private int ge = -1;

    private int lt = -1;

    private int le = -1;
}

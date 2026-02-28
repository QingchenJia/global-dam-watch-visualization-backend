package org.example.backendspringboot.dto;

import lombok.Data;

import java.util.List;

@Data
public class BarrierForm {

    List<String> damType;

    String powerMw;

    List<String> mainBasin;

    List<String> mainUse;

    List<String> quality;

    List<Integer> year;

    List<String> country;

    String damHgtM;
}

package org.example.backendspringboot.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReservoirForm {

    String capMcm;

    String areaSkm;

    List<String> mainBasin;

    List<String> mainUse;

    List<String> quality;

    List<Integer> year;

    List<String> country;

    String depthM;
}

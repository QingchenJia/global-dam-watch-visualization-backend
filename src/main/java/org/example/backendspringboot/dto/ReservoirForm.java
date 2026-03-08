package org.example.backendspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.backendspringboot.common.RangeData;

import java.util.List;

@Data
@AllArgsConstructor
public class ReservoirForm {

    RangeData capMcm;

    RangeData areaSkm;

    List<String> mainBasin;

    List<String> mainUse;

    List<String> quality;

    List<Integer> year;

    List<String> country;

    RangeData depthM;
}

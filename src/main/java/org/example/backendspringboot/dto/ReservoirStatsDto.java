package org.example.backendspringboot.dto;

import lombok.Data;
import org.example.backendspringboot.common.StatsData;

import java.util.List;

@Data
public class ReservoirStatsDto {

    private int total;

    List<StatsData> capMcm;

    List<StatsData> areaSkm;

    List<StatsData> mainUse;

    List<StatsData> quality;

    List<StatsData> yearRes;

    List<StatsData> country;
}

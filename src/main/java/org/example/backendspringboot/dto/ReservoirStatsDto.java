package org.example.backendspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.backendspringboot.common.StatsData;

import java.util.List;

@Data
@AllArgsConstructor
public class ReservoirStatsDto {

    private long total;

    List<StatsData> capMcm;

    List<StatsData> areaSkm;

    List<StatsData> mainUse;

    List<StatsData> quality;

    List<StatsData> yearDam;

    List<StatsData> country;
}

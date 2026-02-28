package org.example.backendspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.backendspringboot.common.StatsData;

import java.util.List;

@Data
@AllArgsConstructor
public class BarrierStatsDto {

    private int total;

    List<StatsData> damType;

    List<StatsData> powerMw;

    List<StatsData> mainUse;

    List<StatsData> quality;

    List<StatsData> yearDam;

    List<StatsData> country;
}

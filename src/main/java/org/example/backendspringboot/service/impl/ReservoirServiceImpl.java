package org.example.backendspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.backendspringboot.common.StatsData;
import org.example.backendspringboot.dto.ReservoirForm;
import org.example.backendspringboot.dto.ReservoirStatsDto;
import org.example.backendspringboot.entity.Reservoir;
import org.example.backendspringboot.mapper.ReservoirMapper;
import org.example.backendspringboot.service.ReservoirService;
import org.example.backendspringboot.util.StatsUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservoirServiceImpl extends ServiceImpl<ReservoirMapper, Reservoir> implements ReservoirService {

    @Override
    public List<Reservoir> databaseOperation(String sql) {
        return baseMapper.executeSql(sql);
    }

    @Override
    public List<Reservoir> loadAllArea() {
        return baseMapper.selectAllArea();
    }

    @Override
    public Reservoir showDescription(int gdwId) {
        return baseMapper.selectByGdwId(gdwId);
    }

    @Override
    public Reservoir findLikeName(String resName) {
        List<Reservoir> reservoirIds = baseMapper.selectLikeName(resName);
        return reservoirIds.getFirst();
    }

    @Override
    public ReservoirStatsDto dataStats(ReservoirForm form) {
        LambdaQueryWrapper<Reservoir> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.in(form.getMainBasin() != null, Reservoir::getMainBasin, form.getMainBasin())
                .in(form.getMainUse() != null, Reservoir::getMainUse, form.getMainUse())
                .in(form.getCountry() != null, Reservoir::getCountry, form.getCountry())
                .in(form.getQuality() != null, Reservoir::getQuality, form.getQuality());

        if (form.getYear() != null && form.getYear().getFirst() != null && form.getYear().getLast() != null) {
            queryWrapper.between(
                    Reservoir::getYearDam,
                    form.getYear().getFirst(),
                    form.getYear().getLast()
            );
        }

        queryWrapper = StatsUtils.setRange(queryWrapper, Reservoir::getCapMcm, form.getCapMcm());
        queryWrapper = StatsUtils.setRange(queryWrapper, Reservoir::getAreaSkm, form.getAreaSkm());
        queryWrapper = StatsUtils.setRange(queryWrapper, Reservoir::getDepthM, form.getDepthM());

        long count = count(queryWrapper);
        queryWrapper.select(
                Reservoir::getCapMcm,
                Reservoir::getAreaSkm,
                Reservoir::getMainUse,
                Reservoir::getQuality,
                Reservoir::getYearDam,
                Reservoir::getCountry
        );
        List<Reservoir> reservoirs = list(queryWrapper);

        List<StatsData> capMcm = StatsUtils.formatStats(reservoirs, Reservoir::getCapMcm);
        List<StatsData> areaSkm = StatsUtils.formatStats(reservoirs, Reservoir::getAreaSkm);
        List<StatsData> mainUse = StatsUtils.formatStats(reservoirs, Reservoir::getMainUse);
        List<StatsData> quality = StatsUtils.formatStats(reservoirs, Reservoir::getQuality);
        List<StatsData> yearDam = StatsUtils.formatStats(reservoirs, Reservoir::getYearDam);
        List<StatsData> country = StatsUtils.formatStats(reservoirs, Reservoir::getCountry);

        return new ReservoirStatsDto(count, capMcm, areaSkm, mainUse, quality, yearDam, country);
    }
}

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
        LambdaQueryWrapper<Reservoir> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                Reservoir::getGdwId,
                Reservoir::getResName
        );
        return baseMapper.selectGeoByCustomCondition(queryWrapper);
    }

    @Override
    public Reservoir showDescription(int gdwId) {
        return baseMapper.selectByGdwId(gdwId);
    }

    @Override
    public Reservoir findLikeName(String resName) {
        LambdaQueryWrapper<Reservoir> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Reservoir::getGdwId, Reservoir::getResName)
                .like(Reservoir::getResName, resName)
                .last("LIMIT 1");
        return getOne(queryWrapper);
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

        // 进行区间统计避免分类过多影响视觉体验
        List<StatsData> capMcm = StatsUtils.statsFieldRange(
                reservoirs,
                Reservoir::getCapMcm,
                value -> {
                    if (value < 1) return "<1";
                    if (value < 10) return "1-10";
                    if (value < 100) return "10-100";
                    if (value < 1000) return "100-1000";
                    return ">1000";
                }
        );

        List<StatsData> areaSkm = StatsUtils.statsFieldRange(
                reservoirs,
                Reservoir::getAreaSkm,
                value -> {
                    if (value < 1) return "<1";
                    if (value < 10) return "1-10";
                    if (value < 100) return "10-100";
                    if (value < 1000) return "100-1000";
                    return ">1000";
                }
        );

        List<StatsData> mainUse = StatsUtils.formatStats(reservoirs, Reservoir::getMainUse);
        List<StatsData> quality = StatsUtils.formatStats(reservoirs, Reservoir::getQuality);

        List<StatsData> yearDam = StatsUtils.statsFieldRange(
                reservoirs,
                Reservoir::getYearDam,
                value -> {
                    if (value < 1900) return "<1900";
                    if (value < 1920) return "1900-1920";
                    if (value < 1940) return "1920-1940";
                    if (value < 1960) return "1940-1960";
                    if (value < 1980) return "1960-1980";
                    if (value < 2000) return "1980-2000";
                    return ">2000";
                }
        );

        List<StatsData> country = StatsUtils.formatStats(reservoirs, Reservoir::getCountry);

        return new ReservoirStatsDto(count, capMcm, areaSkm, mainUse, quality, yearDam, country);
    }

    @Override
    public List<Reservoir> dataSync(ReservoirForm form) {
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

        queryWrapper.select(
                Reservoir::getGdwId,
                Reservoir::getResName
        );

        return baseMapper.selectGeoByCustomCondition(queryWrapper);
    }
}

package org.example.backendspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.backendspringboot.common.StatsData;
import org.example.backendspringboot.dto.BarrierForm;
import org.example.backendspringboot.dto.BarrierStatsDto;
import org.example.backendspringboot.entity.Barrier;
import org.example.backendspringboot.mapper.BarrierMapper;
import org.example.backendspringboot.service.BarrierService;
import org.example.backendspringboot.util.StatsUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarrierServiceImpl extends ServiceImpl<BarrierMapper, Barrier> implements BarrierService {

    @Override
    public List<Barrier> databaseOperation(String condition) {
        LambdaQueryWrapper<Barrier> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                Barrier::getGdwId,
                Barrier::getDamName,
                Barrier::getDamType,
                Barrier::getYearDam
        );
        return baseMapper.executeSql(queryWrapper, condition);
    }

    @Override
    public List<Barrier> loadAllLocation() {
        LambdaQueryWrapper<Barrier> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(
                Barrier::getGdwId,
                Barrier::getDamName,
                Barrier::getDamType,
                Barrier::getYearDam
        );
        return baseMapper.selectGeoByCustomCondition(queryWrapper);
    }

    @Override
    public Barrier showDecription(int gdwId) {
        return baseMapper.selectByGdwId(gdwId);
    }

    @Override
    public Barrier findLikeName(String damName) {
        LambdaQueryWrapper<Barrier> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Barrier::getGdwId, Barrier::getDamName)
                .like(Barrier::getDamName, damName)
                // 获取一条数据
                .last("LIMIT 1");
        return getOne(queryWrapper);
    }

    @Override
    public BarrierStatsDto dataStats(BarrierForm form) {
        LambdaQueryWrapper<Barrier> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.in(form.getDamType() != null, Barrier::getDamType, form.getDamType())
                .in(form.getMainBasin() != null, Barrier::getMainBasin, form.getMainBasin())
                .in(form.getMainUse() != null, Barrier::getMainUse, form.getMainUse())
                .in(form.getCountry() != null, Barrier::getCountry, form.getCountry())
                .in(form.getQuality() != null, Barrier::getQuality, form.getQuality());

        if (form.getYear() != null && form.getYear().getFirst() != null && form.getYear().getLast() != null) {
            queryWrapper.between(
                    Barrier::getYearDam,
                    form.getYear().getFirst(),
                    form.getYear().getLast()
            );
        }

        queryWrapper = StatsUtils.setRange(queryWrapper, Barrier::getDamLenM, form.getDamLenM());
        queryWrapper = StatsUtils.setRange(queryWrapper, Barrier::getDamHgtM, form.getDamHgtM());

        // 统计符合查询条件的数据总数
        long count = count(queryWrapper);

        // 如果没有符合条件的数据则返回空
        if (count == 0) {
            return new BarrierStatsDto(0, null, null, null, null, null, null);
        }

        queryWrapper.select(
                Barrier::getDamType,
                Barrier::getPowerMw,
                Barrier::getMainUse,
                Barrier::getQuality,
                Barrier::getYearDam,
                Barrier::getCountry
        );
        // 条件查询得到大坝集合原始数据
        List<Barrier> barriers = list(queryWrapper);

        List<StatsData> damType = StatsUtils.formatStats(barriers, Barrier::getDamType);

        List<StatsData> powerMw = StatsUtils.statsFieldRange(
                barriers,
                Barrier::getPowerMw,
                value -> {
                    if (value < 1) return "<1";
                    if (value < 10) return "1-10";
                    if (value < 100) return "10-100";
                    if (value < 1000) return "100-1000";
                    return ">1000";
                }
        );

        List<StatsData> mainUse = StatsUtils.formatStats(barriers, Barrier::getMainUse);
        List<StatsData> quality = StatsUtils.formatStats(barriers, Barrier::getQuality);

        List<StatsData> yearDam = StatsUtils.statsFieldRange(
                barriers,
                Barrier::getYearDam,
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

        List<StatsData> country = StatsUtils.formatStats(barriers, Barrier::getCountry);

        return new BarrierStatsDto(count, damType, powerMw, mainUse, quality, yearDam, country);
    }

    @Override
    public List<Barrier> dataSync(BarrierForm form) {
        LambdaQueryWrapper<Barrier> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.in(form.getDamType() != null, Barrier::getDamType, form.getDamType())
                .in(form.getMainBasin() != null, Barrier::getMainBasin, form.getMainBasin())
                .in(form.getMainUse() != null, Barrier::getMainUse, form.getMainUse())
                .in(form.getCountry() != null, Barrier::getCountry, form.getCountry())
                .in(form.getQuality() != null, Barrier::getQuality, form.getQuality());

        if (form.getYear() != null && form.getYear().getFirst() != null && form.getYear().getLast() != null) {
            queryWrapper.between(
                    Barrier::getYearDam,
                    form.getYear().getFirst(),
                    form.getYear().getLast()
            );
        }

        queryWrapper = StatsUtils.setRange(queryWrapper, Barrier::getDamLenM, form.getDamLenM());
        queryWrapper = StatsUtils.setRange(queryWrapper, Barrier::getDamHgtM, form.getDamHgtM());

        queryWrapper.select(
                Barrier::getGdwId,
                Barrier::getDamName,
                Barrier::getDamType,
                Barrier::getYearDam
        );

        return baseMapper.selectGeoByCustomCondition(queryWrapper);
    }
}

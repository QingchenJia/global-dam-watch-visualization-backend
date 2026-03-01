package org.example.backendspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.example.backendspringboot.common.RangeData;
import org.example.backendspringboot.common.StatsData;
import org.example.backendspringboot.dto.BarrierForm;
import org.example.backendspringboot.dto.BarrierStatsDto;
import org.example.backendspringboot.entity.Barrier;
import org.example.backendspringboot.mapper.BarrierMapper;
import org.example.backendspringboot.service.BarrierService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BarrierServiceImpl extends ServiceImpl<BarrierMapper, Barrier> implements BarrierService {

    @Override
    public List<Barrier> databaseOperation(String sql) {
        return baseMapper.executeSql(sql);
    }

    @Override
    public List<Barrier> loadAllLocation() {
        return baseMapper.selectAllLocation();
    }

    @Override
    public Barrier showDecription(int gdwId) {
        return baseMapper.selectByGdwId(gdwId);
    }

    @Override
    public Barrier findLikeName(String damName) {
        List<Barrier> barrierIds = baseMapper.selectLikeName(damName);
        return barrierIds.getFirst();
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
            queryWrapper.between(Barrier::getYearDam, form.getYear().getFirst(), form.getYear().getLast());
        }

        queryWrapper = setRange(queryWrapper, form.getDamLenM());
        queryWrapper = setRange(queryWrapper, form.getDamHgtM());

        // 统计符合查询条件的数据总数
        long count = count(queryWrapper);
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

        List<StatsData> damType = formatStats(barriers, Barrier::getDamType);
        List<StatsData> powerMw = formatStats(barriers, Barrier::getPowerMw);
        List<StatsData> mainUse = formatStats(barriers, Barrier::getMainUse);
        List<StatsData> quality = formatStats(barriers, Barrier::getQuality);
        List<StatsData> yearDam = formatStats(barriers, Barrier::getYearDam);
        List<StatsData> country = formatStats(barriers, Barrier::getCountry);

        return new BarrierStatsDto((int) count, damType, powerMw, mainUse, quality, yearDam, country);
    }

    private static List<StatsData> formatStats(List<Barrier> barriers, Function<? super Barrier, ?> getField) {
        Map<Object, Integer> count = barriers.stream()
                // 过滤掉具体数据值为空字符串或-99的情况
                .filter(barrier -> {
                    Object fieldValue = getField.apply(barrier);
                    return !(fieldValue instanceof Long && (Long) fieldValue == -99)
                            && !(fieldValue instanceof Double && (Double) fieldValue == -99)
                            && !("".equals(fieldValue));
                })
                .collect(Collectors.groupingBy(
                        getField,
                        Collectors.summingInt(e -> 1)
                ));

        return count.entrySet()
                .stream()
                .map(entry ->
                        new StatsData(ObjectUtils.toString(entry.getKey()), entry.getValue()))
                .toList();
    }

    private static LambdaQueryWrapper<Barrier> setRange(LambdaQueryWrapper<Barrier> queryWrapper, RangeData rangeData) {
        if (rangeData != null) {
            return queryWrapper.eq(rangeData.getEq() != -1, Barrier::getPowerMw, rangeData.getEq())
                    .gt(rangeData.getGt() != -1, Barrier::getPowerMw, rangeData.getGt())
                    .ge(rangeData.getGe() != -1, Barrier::getPowerMw, rangeData.getGe())
                    .lt(rangeData.getLt() != -1, Barrier::getPowerMw, rangeData.getLt())
                    .le(rangeData.getLe() != -1, Barrier::getPowerMw, rangeData.getLe());
        }

        return queryWrapper;
    }
}

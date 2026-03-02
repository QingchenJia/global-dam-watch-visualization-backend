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
        List<StatsData> powerMw = StatsUtils.formatStats(barriers, Barrier::getPowerMw);
        List<StatsData> mainUse = StatsUtils.formatStats(barriers, Barrier::getMainUse);
        List<StatsData> quality = StatsUtils.formatStats(barriers, Barrier::getQuality);
        List<StatsData> yearDam = StatsUtils.formatStats(barriers, Barrier::getYearDam);
        List<StatsData> country = StatsUtils.formatStats(barriers, Barrier::getCountry);

        return new BarrierStatsDto(count, damType, powerMw, mainUse, quality, yearDam, country);
    }
}

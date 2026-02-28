package org.example.backendspringboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.backendspringboot.common.StatsData;
import org.example.backendspringboot.dto.BarrierForm;
import org.example.backendspringboot.dto.BarrierStatsDto;
import org.example.backendspringboot.entity.Barrier;
import org.example.backendspringboot.mapper.BarrierMapper;
import org.example.backendspringboot.service.BarrierService;
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
        List<StatsData> damTypeStats = null;
        List<StatsData> powerMwStats = null;
        List<StatsData> mainUseStats = null;
        List<StatsData> qualityStats = null;
        List<StatsData> yearDamStats = null;
        List<StatsData> countryStats = null;

        if (form.getDamType() == null) {
            damTypeStats = baseMapper.damTypeStats();
        }
        if (form.getPowerMw() == null) {
            powerMwStats = baseMapper.powerMwStats();
        }
        if (form.getMainUse() == null) {
            mainUseStats = baseMapper.mainUseStats();
        }
        if (form.getQuality() == null) {
            qualityStats = baseMapper.qualityStats();
        }
        if (form.getYear() == null) {
            yearDamStats = baseMapper.yearDamStats();
        }
        if (form.getCountry() == null) {
            countryStats = baseMapper.countryStats();
        }
        long count = count();

        return new BarrierStatsDto((int) count, damTypeStats, powerMwStats, mainUseStats, qualityStats, yearDamStats, countryStats);
    }
}

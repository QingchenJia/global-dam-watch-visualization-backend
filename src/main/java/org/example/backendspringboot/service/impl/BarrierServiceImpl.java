package org.example.backendspringboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
}

package org.example.backendspringboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.backendspringboot.entity.Reservoir;
import org.example.backendspringboot.mapper.ReservoirMapper;
import org.example.backendspringboot.service.ReservoirService;
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
}

package org.example.backendspringboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.backendspringboot.dto.BarrierForm;
import org.example.backendspringboot.dto.BarrierStatsDto;
import org.example.backendspringboot.entity.Barrier;

import java.util.List;

public interface BarrierService extends IService<Barrier> {

    List<Barrier> databaseOperation(String sql);

    List<Barrier> loadAllLocation();

    Barrier showDecription(int gdwId);

    Barrier findLikeName(String damName);

    BarrierStatsDto dataStats(BarrierForm form);
}

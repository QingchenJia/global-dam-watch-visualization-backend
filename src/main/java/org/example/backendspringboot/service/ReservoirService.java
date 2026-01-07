package org.example.backendspringboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.backendspringboot.entity.Reservoir;

import java.util.List;

public interface ReservoirService extends IService<Reservoir> {

    List<Reservoir> databaseOperation(String sql);

    List<Reservoir> loadAllArea();

    Reservoir showDescription(int gdwId);

    Reservoir findLikeName(String resName);
}

package org.example.backendspringboot.controller;

import org.example.backendspringboot.common.Result;
import org.example.backendspringboot.dto.BarrierForm;
import org.example.backendspringboot.dto.BarrierStatsDto;
import org.example.backendspringboot.entity.Barrier;
import org.example.backendspringboot.service.BarrierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/barrier")
@CrossOrigin(origins = "*")
public class BarrierController {

    @Autowired
    private BarrierService barrierService;

    @GetMapping("/location")
    private Result<List<Barrier>> loadLocation() {
        List<Barrier> barriers = barrierService.loadAllLocation();
        return Result.success(barriers);
    }

    @GetMapping("/{id}")
    private Result<Barrier> detail(@PathVariable("id") int gdwId) {
        Barrier barrier = barrierService.showDecription(gdwId);
        return Result.success(barrier);
    }

    @PostMapping("/position")
    private Result<Barrier> findPosition(@RequestBody String damName) {
        Barrier barrier = barrierService.findLikeName(damName);
        return Result.success(barrier);
    }

    @PostMapping("/filter")
    public Result<List<Barrier>> filterBarriers(@RequestBody String sql) {
        return Result.success(barrierService.databaseOperation(sql));
    }

    @PostMapping("/stats")
    public Result<BarrierStatsDto> dataStats(@RequestBody BarrierForm form) {
        return Result.success(barrierService.dataStats(form));
    }

    @PostMapping("/sync")
    public Result<List<Barrier>> syncData(@RequestBody BarrierForm form) {
        return Result.success(barrierService.dataSync(form));
    }
}

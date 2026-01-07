package org.example.backendspringboot.controller;

import org.example.backendspringboot.common.Result;
import org.example.backendspringboot.entity.Reservoir;
import org.example.backendspringboot.service.ReservoirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservoir")
@CrossOrigin(origins = "*")
public class ReservoirController {

    @Autowired
    private ReservoirService reservoirService;

    @GetMapping("/location")
    private Result<List<Reservoir>> loadLocation() {
        List<Reservoir> reservoirs = reservoirService.loadAllArea();
        return Result.success(reservoirs);
    }

    @GetMapping("/{id}")
    private Result<Reservoir> detail(@PathVariable("id") int gdwId) {
        Reservoir reservoir = reservoirService.showDescription(gdwId);
        return Result.success(reservoir);
    }

    @PostMapping("/position")
    private Result<Reservoir> findPosition(@RequestBody String resName) {
        Reservoir reservoir = reservoirService.findLikeName(resName);
        return Result.success(reservoir);
    }

    @PostMapping("/filter")
    public Result<List<Reservoir>> filterReservoirs(@RequestBody String sql) {
        return Result.success(reservoirService.databaseOperation(sql));
    }
}

package ch.strasser.marvin.controller;

import ch.strasser.marvin.model.TrainLine;
import ch.strasser.marvin.service.TrainLineService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/lines")
public class TrainLineController {

    private final TrainLineService service;

    public TrainLineController(TrainLineService service) {
        this.service = service;
    }

    @PostMapping
    public TrainLine create(@RequestBody TrainLine line) {
        return service.create(line);
    }

    @GetMapping
    public List<TrainLine> getAll() {
        return service.findAll();
    }
}

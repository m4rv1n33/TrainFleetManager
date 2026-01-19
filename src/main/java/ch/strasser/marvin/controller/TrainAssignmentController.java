package ch.strasser.marvin.controller;

import ch.strasser.marvin.model.Train;
import ch.strasser.marvin.model.TrainAssignment;
import ch.strasser.marvin.model.TrainLine;
import ch.strasser.marvin.service.TrainAssignmentService;
import ch.strasser.marvin.service.TrainLineService;
import ch.strasser.marvin.service.TrainService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assignments")
public class TrainAssignmentController {

    private final TrainAssignmentService assignmentService;
    private final TrainService trainService;
    private final TrainLineService lineService;

    public TrainAssignmentController(
            TrainAssignmentService assignmentService,
            TrainService trainService,
            TrainLineService lineService
    ) {
        this.assignmentService = assignmentService;
        this.trainService = trainService;
        this.lineService = lineService;
    }

    @PostMapping
    public TrainAssignment create(@RequestBody TrainAssignment assignment) {
        return assignmentService.create(assignment);
    }

    @GetMapping
    public List<TrainAssignment> getAll() {
        return assignmentService.findAll();
    }

    @GetMapping("/train/{vehicleNumber}")
    public List<TrainAssignment> getByTrain(@PathVariable String vehicleNumber) {
        Train train = trainService.findByNumber(vehicleNumber);
        return assignmentService.findByTrain(train);
    }

    @GetMapping("/line/{lineId}")
    public List<TrainAssignment> getByLine(@PathVariable Long lineId) {
        TrainLine line = lineService.findById(lineId);
        return assignmentService.findByLine(line);
    }
}

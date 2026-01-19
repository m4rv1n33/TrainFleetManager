package ch.strasser.marvin.controller;

import ch.strasser.marvin.model.Train;
import ch.strasser.marvin.model.TrainAssignment;
import ch.strasser.marvin.model.TrainLine;
import ch.strasser.marvin.service.TrainAssignmentService;
import ch.strasser.marvin.service.TrainLineService;
import ch.strasser.marvin.service.TrainService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * Logic for assignments
 * </p>
 *
 * @author Marvin Strasser
 * @version 1.0
 * @since 19/01/2026
 */
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

    /**
     * Can create new assignments
     * @param assignment
     * @return
     */
    @PostMapping
    public TrainAssignment create(@RequestBody TrainAssignment assignment) {
        return assignmentService.create(assignment);
    }

    /**
     * Can get all assignments
     * @return
     */
    @GetMapping
    public List<TrainAssignment> getAll() {
        return assignmentService.findAll();
    }

    /**
     * Can get all assignments of a single vehicle
     * @param vehicleNumber
     * @return
     */
    @GetMapping("/train/{vehicleNumber}")
    public List<TrainAssignment> getByTrain(@PathVariable String vehicleNumber) {
        Train train = trainService.findByNumber(vehicleNumber);
        return assignmentService.findByTrain(train);
    }

    /**
     * Can get all assignments of a single line
     * @param lineId
     * @return
     */
    @GetMapping("/line/{lineId}")
    public List<TrainAssignment> getByLine(@PathVariable Long lineId) {
        TrainLine line = lineService.findById(lineId);
        return assignmentService.findByLine(line);
    }
    /**
     * Can delete assignment by id
     * @param id
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        assignmentService.delete(id);
    }
}

package ch.strasser.marvin.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.strasser.marvin.dto.AssignmentDto;
import ch.strasser.marvin.model.AssignmentStatus;
import ch.strasser.marvin.model.Train;
import ch.strasser.marvin.model.TrainAssignment;
import ch.strasser.marvin.model.TrainLine;
import ch.strasser.marvin.service.TrainAssignmentService;
import ch.strasser.marvin.service.TrainLineService;
import ch.strasser.marvin.service.TrainService;

/**
 * Logic for assignments
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

    public TrainAssignmentController(TrainAssignmentService assignmentService,
                                     TrainService trainService,
                                     TrainLineService lineService) {
        this.assignmentService = assignmentService;
        this.trainService = trainService;
        this.lineService = lineService;
    }

    /** Create a new assignment */
    @PostMapping
    public TrainAssignment create(@RequestBody TrainAssignment assignment) {
        return assignmentService.create(assignment);
    }

    /** Get all assignments */
    @GetMapping
    public List<AssignmentDto> getAll() {
        return assignmentService.findAll();
    }

    /** Get assignments by train */
    @GetMapping("/train/{vehicleNumber}")
    public List<AssignmentDto> getByTrain(@PathVariable String vehicleNumber) {
        Train train = trainService.findByNumber(vehicleNumber);
        return assignmentService.findByTrain(train);
    }

    /** Get assignments by line */
    @GetMapping("/line/{lineId}")
    public List<AssignmentDto> getByLine(@PathVariable Long lineId) {
        TrainLine line = lineService.findById(lineId);
        return assignmentService.findByLine(line);
    }

    /** Update status of an assignment */
    @PutMapping("/{id}/status")
    public TrainAssignment updateStatus(@PathVariable Long id, @RequestBody AssignmentStatus status) {
        return assignmentService.updateStatus(id, status);
    }

    /** Delete an assignment */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        assignmentService.delete(id);
    }
}

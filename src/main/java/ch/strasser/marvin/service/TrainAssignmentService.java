package ch.strasser.marvin.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import ch.strasser.marvin.dto.AssignmentDto;
import ch.strasser.marvin.model.AssignmentStatus;
import ch.strasser.marvin.model.Train;
import ch.strasser.marvin.model.TrainAssignment;
import ch.strasser.marvin.model.TrainLine;
import ch.strasser.marvin.model.TrainStatus;
import ch.strasser.marvin.repository.TrainAssignmentRepository;

/**
 * Business logic for train assignments.
 */
@Service
public class TrainAssignmentService {

    private final TrainAssignmentRepository repository;
    private final TrainService trainService;
    private final TrainLineService lineService;

    public TrainAssignmentService(TrainAssignmentRepository repository,
                                  TrainService trainService,
                                  TrainLineService lineService) {
        this.repository = repository;
        this.trainService = trainService;
        this.lineService = lineService;
    }

    /** Create an assignment. */
    public TrainAssignment create(TrainAssignment assignment) {
        if (assignment == null ||
                assignment.getTrain() == null ||
                assignment.getTrain().getVehicleNumber() == null ||
                assignment.getLine() == null ||
                assignment.getLine().getId() == null ||
                assignment.getStatus() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Train vehicleNumber, line id, and status are required");
        }

        Train train = fetchTrain(assignment.getTrain().getVehicleNumber());
        TrainLine line = fetchLine(assignment.getLine().getId());

        if (assignment.getStatus() == AssignmentStatus.ACTIVE) {
            validateActive(train, null);
        }

        assignment.setTrain(train);
        assignment.setLine(line);
        return repository.save(assignment);
    }

    /** Update assignment status. */
    public TrainAssignment updateStatus(Long id, AssignmentStatus newStatus) {
        TrainAssignment existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignment not found"));

        if (newStatus == AssignmentStatus.ACTIVE) {
            validateActive(existing.getTrain(), existing);
        }

        existing.setStatus(newStatus);
        return repository.save(existing);
    }

    /** Delete assignment by id. */
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignment not found");
        }
        repository.deleteById(id);
    }

    /** Find all assignments. */
    public List<AssignmentDto> findAll() {
        return repository.findAllWithTrainAndLine().stream().map(this::toDto).toList();
    }

    /** Find assignments by train. */
    public List<AssignmentDto> findByTrain(Train train) {
        return repository.findByTrain(train).stream().map(this::toDto).toList();
    }

    /** Find assignments by line. */
    public List<AssignmentDto> findByLine(TrainLine line) {
        return repository.findByLine(line).stream().map(this::toDto).toList();
    }

    private AssignmentDto toDto(TrainAssignment assignment) {
        String vehicle = assignment.getTrain() != null ? assignment.getTrain().getVehicleNumber() : null;
        String lineName = assignment.getLine() != null ? assignment.getLine().getName() : null;
        return new AssignmentDto(
                assignment.getId(),
                assignment.getDate(),
                assignment.getStatus(),
                vehicle,
                lineName,
                assignment.getTrain(),
                assignment.getLine()
        );
    }

    private Train fetchTrain(String vehicleNumber) {
        try {
            return trainService.findByNumber(vehicleNumber);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    private TrainLine fetchLine(Long id) {
        try {
            return lineService.findById(id);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    private void validateActive(Train train, TrainAssignment current) {
        if (train.getStatus() == TrainStatus.MAINTENANCE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "This vehicle is undergoing maintenance and does not have a definitive time or date when it is ready to be used again"
            );
        }
        if (train.getStatus() != TrainStatus.IN_SERVICE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Train must be IN_SERVICE for ACTIVE assignment");
        }
        boolean hasActive = repository.existsByTrainAndStatus(train, AssignmentStatus.ACTIVE);
        boolean currentIsActive = current != null && current.getStatus() == AssignmentStatus.ACTIVE;
        if (hasActive && !currentIsActive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Train already has an ACTIVE assignment");
        }
    }
}

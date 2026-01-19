package ch.strasser.marvin.service;

import ch.strasser.marvin.model.AssignmentStatus;
import ch.strasser.marvin.model.Train;
import ch.strasser.marvin.model.TrainAssignment;
import ch.strasser.marvin.model.TrainLine;
import ch.strasser.marvin.model.TrainStatus;
import ch.strasser.marvin.repository.TrainAssignmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TrainAssignmentService {

    private final TrainAssignmentRepository repository;
    private final TrainService trainService;
    private final TrainLineService lineService;

    public TrainAssignmentService(
            TrainAssignmentRepository repository,
            TrainService trainService,
            TrainLineService lineService
    ) {
        this.repository = repository;
        this.trainService = trainService;
        this.lineService = lineService;
    }

    public TrainAssignment create(TrainAssignment assignment) {
        if (assignment == null ||
                assignment.getTrain() == null ||
                assignment.getTrain().getVehicleNumber() == null ||
                assignment.getLine() == null ||
                assignment.getLine().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Train vehicleNumber and line id are required");
        }

        Train train;
        TrainLine line;
        try {
            train = trainService.findByNumber(assignment.getTrain().getVehicleNumber());
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        try {
            line = lineService.findById(assignment.getLine().getId());
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }

        if (assignment.getStatus() == AssignmentStatus.ACTIVE && train.getStatus() != TrainStatus.IN_SERVICE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Train must be IN_SERVICE for ACTIVE assignment"
            );
        }

        assignment.setTrain(train);
        assignment.setLine(line);
        return repository.save(assignment);
    }

    public List<TrainAssignment> findAll() {
        return repository.findAll();
    }

    public List<TrainAssignment> findByTrain(Train train) {
        return repository.findByTrain(train);
    }

    public List<TrainAssignment> findByLine(TrainLine line) {
        return repository.findByLine(line);
    }
    /**
     * Deletes assignment by id
     * @param id
     */
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Assignment not found");
        }
        repository.deleteById(id);
    }
}

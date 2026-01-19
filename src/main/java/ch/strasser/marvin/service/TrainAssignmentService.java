package ch.strasser.marvin.service;

import ch.strasser.marvin.model.Train;
import ch.strasser.marvin.model.TrainAssignment;
import ch.strasser.marvin.model.TrainLine;
import ch.strasser.marvin.model.TrainStatus;
import ch.strasser.marvin.repository.TrainAssignmentRepository;
import org.springframework.stereotype.Service;

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
        Train train = trainService.findByNumber(assignment.getTrain().getVehicleNumber());
        TrainLine line = lineService.findById(assignment.getLine().getId());

        if (train.getStatus() != TrainStatus.IN_SERVICE) {
            throw new RuntimeException("Train not available");
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
}

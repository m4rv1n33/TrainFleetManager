package ch.strasser.marvin.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import ch.strasser.marvin.dto.TrainStatusDto;
import ch.strasser.marvin.model.AssignmentStatus;
import ch.strasser.marvin.model.Train;
import ch.strasser.marvin.model.TrainAssignment;
import ch.strasser.marvin.model.TrainStatus;
import ch.strasser.marvin.repository.TrainAssignmentRepository;
import ch.strasser.marvin.repository.TrainRepository;

@Service
public class TrainService {

    private final TrainRepository repository;
    private final TrainAssignmentRepository assignmentRepository;

    public TrainService(TrainRepository repository, TrainAssignmentRepository assignmentRepository) {
        this.repository = repository;
        this.assignmentRepository = assignmentRepository;
    }

    public Train create(Train train) {
        if (train.getVehicleNumber() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "vehicleNumber is required");
        }
        if (repository.existsByVehicleNumber(train.getVehicleNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "vehicleNumber must be unique");
        }
        return repository.save(train);
    }

    public List<Train> findAll() {
        return repository.findAll();
    }

    public Train findByNumber(String vehicleNumber) {
        return repository.findByVehicleNumber(vehicleNumber)
                .orElseThrow(() -> new RuntimeException("Train not found"));
    }

    public Train updateStatus(String vehicleNumber, TrainStatus status) {
        Train train = findByNumber(vehicleNumber);
        train.setStatus(status);
        return repository.save(train);
    }

    public TrainStatusDto getStatusWithLine(String vehicleNumber) {
        Train train = findByNumber(vehicleNumber);
        String lineName = null;

        if (train.getStatus() == TrainStatus.IN_SERVICE && train.getCurrentAssignment() != null) {
            lineName = train.getCurrentAssignment().getLine().getName();
        }

        return new TrainStatusDto(train.getVehicleNumber(), train.getStatus().name(), lineName);
    }

    public void deleteByNumber(String vehicleNumber) {
        Train train = findByNumber(vehicleNumber);

        // Prevent deleting a train that is currently active on a line.
        if (assignmentRepository.existsByTrainAndStatus(train, AssignmentStatus.ACTIVE)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete train with an ACTIVE assignment");
        }

        // Remove historical assignments linked to the train to satisfy FK constraints.
        List<TrainAssignment> relatedAssignments = assignmentRepository.findByTrain(train);
        if (!relatedAssignments.isEmpty()) {
            assignmentRepository.deleteAll(relatedAssignments);
        }

        repository.delete(train);
    }
}

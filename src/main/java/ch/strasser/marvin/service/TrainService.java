package ch.strasser.marvin.service;

import ch.strasser.marvin.dto.TrainStatusDto;
import ch.strasser.marvin.model.Train;
import ch.strasser.marvin.model.TrainStatus;
import ch.strasser.marvin.repository.TrainRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TrainService {

    private final TrainRepository repository;

    public TrainService(TrainRepository repository) {
        this.repository = repository;
    }

    public Train create(Train train) {
        if (train.getVehicleNumber() == null) {
            throw new IllegalArgumentException("vehicleNumber is required");
        }
        if (repository.existsByVehicleNumber(train.getVehicleNumber())) {
            throw new RuntimeException("vehicleNumber must be unique");
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
}

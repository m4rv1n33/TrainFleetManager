package ch.strasser.marvin.repository;

import ch.strasser.marvin.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainRepository extends JpaRepository<Train, Long> {
    Optional<Train> findByVehicleNumber(String vehicleNumber);
    boolean existsByVehicleNumber(String vehicleNumber);
}

package ch.strasser.marvin.repository;

import ch.strasser.marvin.model.TrainLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainLineRepository extends JpaRepository<TrainLine, Long> {
}

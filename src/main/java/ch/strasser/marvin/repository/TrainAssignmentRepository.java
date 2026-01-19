package ch.strasser.marvin.repository;

import ch.strasser.marvin.model.TrainAssignment;
import ch.strasser.marvin.model.Train;
import ch.strasser.marvin.model.TrainLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainAssignmentRepository extends JpaRepository<TrainAssignment, Long> {

    List<TrainAssignment> findByTrain(Train train);

    List<TrainAssignment> findByLine(TrainLine line);
}

package ch.strasser.marvin.repository;

import ch.strasser.marvin.model.AssignmentStatus;
import ch.strasser.marvin.model.Train;
import ch.strasser.marvin.model.TrainAssignment;
import ch.strasser.marvin.model.TrainLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Persistence for train assignments */
@Repository
public interface TrainAssignmentRepository extends JpaRepository<TrainAssignment, Long> {

    List<TrainAssignment> findByTrain(Train train);

    List<TrainAssignment> findByLine(TrainLine line);

    boolean existsByTrainAndStatus(Train train, AssignmentStatus status);
}

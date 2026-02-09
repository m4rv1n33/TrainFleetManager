package ch.strasser.marvin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ch.strasser.marvin.model.AssignmentStatus;
import ch.strasser.marvin.model.Train;
import ch.strasser.marvin.model.TrainAssignment;
import ch.strasser.marvin.model.TrainLine;

/** Persistence for train assignments */
@Repository
public interface TrainAssignmentRepository extends JpaRepository<TrainAssignment, Long> {

    @EntityGraph(attributePaths = {"train", "line"})
    @Query("SELECT a FROM TrainAssignment a")
    List<TrainAssignment> findAllWithTrainAndLine();

    @EntityGraph(attributePaths = {"train", "line"})
    List<TrainAssignment> findByTrain(Train train);

    @EntityGraph(attributePaths = {"train", "line"})
    List<TrainAssignment> findByLine(TrainLine line);

    boolean existsByTrainAndStatus(Train train, AssignmentStatus status);
}

package ch.strasser.marvin.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import ch.strasser.marvin.dto.TrainStatusDto;
import ch.strasser.marvin.model.AssignmentStatus;
import ch.strasser.marvin.model.Train;
import ch.strasser.marvin.model.TrainAssignment;
import ch.strasser.marvin.model.TrainLine;
import ch.strasser.marvin.model.TrainStatus;
import ch.strasser.marvin.repository.TrainAssignmentRepository;
import ch.strasser.marvin.repository.TrainRepository;

@ExtendWith(MockitoExtension.class)
class TrainServiceTest {

    @Mock
    private TrainRepository repository;

    @Mock
    private TrainAssignmentRepository assignmentRepository;

    @InjectMocks
    private TrainService service;

    @Test
    void create_requiresVehicleNumber() {
        Train train = new Train();

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.create(train));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void create_rejectsDuplicateVehicleNumber() {
        Train train = new Train();
        train.setVehicleNumber("511-001");
        when(repository.existsByVehicleNumber("511-001")).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.create(train));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void create_savesTrain() {
        Train train = new Train();
        train.setVehicleNumber("511-002");
        when(repository.existsByVehicleNumber("511-002")).thenReturn(false);
        when(repository.save(any(Train.class))).thenReturn(train);

        Train saved = service.create(train);

        assertNotNull(saved);
        assertEquals("511-002", saved.getVehicleNumber());
        verify(repository).save(train);
    }

    @Test
    void getStatusWithLine_includesLineWhenInService() {
        Train train = new Train();
        train.setVehicleNumber("511-003");
        train.setStatus(TrainStatus.IN_SERVICE);

        TrainLine line = new TrainLine();
        line.setName("IC5");
        TrainAssignment assignment = new TrainAssignment();
        assignment.setLine(line);
        train.setCurrentAssignment(assignment);

        when(repository.findByVehicleNumber("511-003")).thenReturn(Optional.of(train));

        TrainStatusDto dto = service.getStatusWithLine("511-003");

        assertEquals("511-003", dto.getVehicleNumber());
        assertEquals("IN_SERVICE", dto.getStatus());
        assertEquals("IC5", dto.getLine());
    }

    @Test
    void deleteByNumber_blocksActiveAssignment() {
        Train train = new Train();
        train.setVehicleNumber("511-004");
        when(repository.findByVehicleNumber("511-004")).thenReturn(Optional.of(train));
        when(assignmentRepository.existsByTrainAndStatus(train, AssignmentStatus.ACTIVE)).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.deleteByNumber("511-004"));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    void deleteByNumber_removesAssignmentsThenTrain() {
        Train train = new Train();
        train.setVehicleNumber("511-005");
        when(repository.findByVehicleNumber("511-005")).thenReturn(Optional.of(train));
        when(assignmentRepository.existsByTrainAndStatus(train, AssignmentStatus.ACTIVE)).thenReturn(false);
        TrainAssignment assignment = new TrainAssignment();
        when(assignmentRepository.findByTrain(train)).thenReturn(List.of(assignment));

        service.deleteByNumber("511-005");

        verify(assignmentRepository).deleteAll(List.of(assignment));
        verify(repository).delete(train);
    }
}

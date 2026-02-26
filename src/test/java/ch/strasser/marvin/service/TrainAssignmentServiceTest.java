package ch.strasser.marvin.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import ch.strasser.marvin.dto.AssignmentDto;
import ch.strasser.marvin.model.AssignmentStatus;
import ch.strasser.marvin.model.Train;
import ch.strasser.marvin.model.TrainAssignment;
import ch.strasser.marvin.model.TrainLine;
import ch.strasser.marvin.model.TrainStatus;
import ch.strasser.marvin.repository.TrainAssignmentRepository;

@ExtendWith(MockitoExtension.class)
class TrainAssignmentServiceTest {

    @Mock
    private TrainAssignmentRepository repository;

    @Mock
    private TrainService trainService;

    @Mock
    private TrainLineService lineService;

    @InjectMocks
    private TrainAssignmentService service;

    @Test
    void create_requiresTrainLineAndStatus() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.create(new TrainAssignment()));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void create_blocksMaintenanceTrainWhenActive() {
        Train train = new Train();
        train.setVehicleNumber("511-010");
        train.setStatus(TrainStatus.MAINTENANCE);

        TrainLine line = new TrainLine();
        line.setId(10L);

        TrainAssignment assignment = new TrainAssignment();
        assignment.setStatus(AssignmentStatus.ACTIVE);
        assignment.setTrain(train);
        assignment.setLine(line);

        when(trainService.findByNumber("511-010")).thenReturn(train);
        when(lineService.findById(10L)).thenReturn(line);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.create(assignment));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void create_blocksDuplicateActiveAssignments() {
        Train train = new Train();
        train.setVehicleNumber("511-011");
        train.setStatus(TrainStatus.IN_SERVICE);

        TrainLine line = new TrainLine();
        line.setId(11L);

        TrainAssignment assignment = new TrainAssignment();
        assignment.setStatus(AssignmentStatus.ACTIVE);
        assignment.setTrain(train);
        assignment.setLine(line);

        when(trainService.findByNumber("511-011")).thenReturn(train);
        when(lineService.findById(11L)).thenReturn(line);
        when(repository.existsByTrainAndStatus(train, AssignmentStatus.ACTIVE)).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.create(assignment));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void updateStatus_requiresExistingAssignment() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.updateStatus(99L, AssignmentStatus.ACTIVE));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void updateStatus_setsStatusWhenValid() {
        Train train = new Train();
        train.setStatus(TrainStatus.IN_SERVICE);

        TrainAssignment assignment = new TrainAssignment();
        assignment.setId(12L);
        assignment.setStatus(AssignmentStatus.PLANNED);
        assignment.setTrain(train);

        when(repository.findById(12L)).thenReturn(Optional.of(assignment));
        when(repository.existsByTrainAndStatus(train, AssignmentStatus.ACTIVE)).thenReturn(false);
        when(repository.save(any(TrainAssignment.class))).thenReturn(assignment);

        TrainAssignment saved = service.updateStatus(12L, AssignmentStatus.ACTIVE);

        assertEquals(AssignmentStatus.ACTIVE, saved.getStatus());
        verify(repository).save(assignment);
    }

    @Test
    void findAll_mapsVehicleAndLineNames() {
        Train train = new Train();
        train.setVehicleNumber("511-020");

        TrainLine line = new TrainLine();
        line.setName("IR36");

        TrainAssignment assignment = new TrainAssignment();
        assignment.setId(1L);
        assignment.setDate(LocalDate.of(2026, 2, 20));
        assignment.setStatus(AssignmentStatus.PLANNED);
        assignment.setTrain(train);
        assignment.setLine(line);

        when(repository.findAllWithTrainAndLine()).thenReturn(List.of(assignment));

        List<AssignmentDto> results = service.findAll();

        assertEquals(1, results.size());
        AssignmentDto dto = results.get(0);
        assertEquals("511-020", dto.getTrainVehicleNumber());
        assertEquals("IR36", dto.getLineName());
    }
}

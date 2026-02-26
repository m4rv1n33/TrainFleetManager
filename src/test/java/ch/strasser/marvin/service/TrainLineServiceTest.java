package ch.strasser.marvin.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.strasser.marvin.model.TrainLine;
import ch.strasser.marvin.repository.TrainLineRepository;

@ExtendWith(MockitoExtension.class)
class TrainLineServiceTest {

    @Mock
    private TrainLineRepository repository;

    @InjectMocks
    private TrainLineService service;

    @Test
    void create_savesLine() {
        TrainLine line = new TrainLine();
        line.setName("IC5");
        when(repository.save(line)).thenReturn(line);

        TrainLine saved = service.create(line);

        assertEquals("IC5", saved.getName());
        verify(repository).save(line);
    }

    @Test
    void findById_returnsLine() {
        TrainLine line = new TrainLine();
        line.setId(4L);
        when(repository.findById(4L)).thenReturn(Optional.of(line));

        TrainLine found = service.findById(4L);

        assertEquals(4L, found.getId());
    }
}

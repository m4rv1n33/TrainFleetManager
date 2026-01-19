package ch.strasser.marvin.service;

import ch.strasser.marvin.model.TrainLine;
import ch.strasser.marvin.repository.TrainLineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainLineService {

    private final TrainLineRepository repository;

    public TrainLineService(TrainLineRepository repository) {
        this.repository = repository;
    }

    public TrainLine create(TrainLine line) {
        return repository.save(line);
    }

    public TrainLine findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public List<TrainLine> findAll() {
        return repository.findAll();
    }

}

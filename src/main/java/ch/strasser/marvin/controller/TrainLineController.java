package ch.strasser.marvin.controller;

import ch.strasser.marvin.model.TrainLine;
import ch.strasser.marvin.service.TrainLineService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * <p>
 * Logic for lines
 * </p>
 *
 * @author Marvin Strasser
 * @version 1.0
 * @since 19/01/2026
 */
@RestController
@RequestMapping("/lines")
public class TrainLineController {

    private final TrainLineService service;

    public TrainLineController(TrainLineService service) {
        this.service = service;
    }

    /**
     * Creates new line
     * @param line
     * @return
     */
    @PostMapping
    public TrainLine create(@RequestBody TrainLine line) {
        return service.create(line);
    }

    /**
     * Can get all lines
     * @return
     */
    @GetMapping
    public List<TrainLine> getAll() {
        return service.findAll();
    }
}

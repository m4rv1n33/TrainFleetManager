package ch.strasser.marvin.controller;

import ch.strasser.marvin.dto.TrainStatusDto;
import ch.strasser.marvin.dto.TrainStatusUpdateRequest;
import ch.strasser.marvin.model.Train;
import ch.strasser.marvin.model.TrainStatus;
import ch.strasser.marvin.service.TrainService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * <p>
 * Logic for application, has GET, PUT and POST methods
 * </p>
 *
 * @author Marvin Strasser
 * @version 1.0
 * @since 19/01/2026
 */
@RestController
@RequestMapping("/trains")
public class TrainController {

    private final TrainService service;

    public TrainController(TrainService service) {
        this.service = service;
    }

    /**
     * Initiates new vehicle object
     * @param train
     * @return
     */
    @PostMapping
    public Train create(@RequestBody Train train) {
        return service.create(train);
    }

    /**
     * Gets all info about a train by vehicleNumber
     * @param vehicleNumber
     * @return
     */
    @GetMapping("/number/{vehicleNumber}")
    public Train getByNumber(@PathVariable String vehicleNumber) {
        return service.findByNumber(vehicleNumber);
    }

    /**
     * Gets status of a train by vehicleNumber
     * @param vehicleNumber
     * @return
     */
    @GetMapping("/number/{vehicleNumber}/status")
    public TrainStatusDto getStatus(@PathVariable String vehicleNumber) {
        return service.getStatusWithLine(vehicleNumber);
    }

    /**
     * Gets all info about all trains
     * @return
     */
    @GetMapping
    public List<Train> getAll() {
        return service.findAll();
    }

    /**
     * Can update train status
     * @param vehicleNumber
     * @param request
     * @return
     */
    @PutMapping("/number/{vehicleNumber}/status")
    public Train updateStatus(
            @PathVariable String vehicleNumber,
            @RequestBody TrainStatusUpdateRequest request
    ) {
        return service.updateStatus(vehicleNumber, request.getStatus());
    }
}

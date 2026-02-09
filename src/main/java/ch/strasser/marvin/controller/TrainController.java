package ch.strasser.marvin.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.strasser.marvin.dto.TrainStatusDto;
import ch.strasser.marvin.dto.TrainStatusUpdateRequest;
import ch.strasser.marvin.model.Train;
import ch.strasser.marvin.service.TrainService;

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
    @GetMapping(value = "/number/{vehicleNumber}", produces = "application/json")
    public Train getByNumber(@PathVariable String vehicleNumber) {
        return service.findByNumber(vehicleNumber);
    }

    /**
     * Gets status of a train by vehicleNumber
     * @param vehicleNumber
     * @return
     */
    @GetMapping(value = "/number/{vehicleNumber}/status", produces = "application/json")
    public TrainStatusDto getStatus(@PathVariable String vehicleNumber) {
        return service.getStatusWithLine(vehicleNumber);
    }

    /**
     * Gets all info about all trains
     * @return
     */
    @GetMapping(produces = "application/json")
    public List<Train> getAll() {
        return service.findAll();
    }

    /**
     * Can update train status
     * @param vehicleNumber
     * @param request
     * @return
     */
    @PutMapping(value = "/number/{vehicleNumber}/status", consumes = "application/json", produces = "application/json")
    public Train updateStatus(
            @PathVariable String vehicleNumber,
            @RequestBody TrainStatusUpdateRequest request
    ) {
        return service.updateStatus(vehicleNumber, request.getStatus());
    }

    /**
     * Delete a train by vehicleNumber. Blocks deletion if the train has an ACTIVE assignment.
     */
    @DeleteMapping(value = "/number/{vehicleNumber}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String vehicleNumber) {
        service.deleteByNumber(vehicleNumber);
    }
}

package ch.strasser.marvin.controller;

import ch.strasser.marvin.dto.TrainStatusDto;
import ch.strasser.marvin.dto.TrainStatusUpdateRequest;
import ch.strasser.marvin.model.Train;
import ch.strasser.marvin.model.TrainStatus;
import ch.strasser.marvin.service.TrainService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/trains")
public class TrainController {

    private final TrainService service;

    public TrainController(TrainService service) {
        this.service = service;
    }

    @PostMapping
    public Train create(@RequestBody Train train) {
        return service.create(train);
    }

    @GetMapping("/number/{vehicleNumber}")
    public Train getByNumber(@PathVariable String vehicleNumber) {
        return service.findByNumber(vehicleNumber);
    }

    @GetMapping("/number/{vehicleNumber}/status")
    public TrainStatusDto getStatus(@PathVariable String vehicleNumber) {
        return service.getStatusWithLine(vehicleNumber);
    }

    @GetMapping
    public List<Train> getAll() {
        return service.findAll();
    }

    @PutMapping("/number/{vehicleNumber}/status")
    public Train updateStatus(
            @PathVariable String vehicleNumber,
            @RequestBody TrainStatusUpdateRequest request
    ) {
        return service.updateStatus(vehicleNumber, request.getStatus());
    }
}

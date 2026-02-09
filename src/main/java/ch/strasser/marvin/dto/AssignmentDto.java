package ch.strasser.marvin.dto;

import java.time.LocalDate;

import ch.strasser.marvin.model.AssignmentStatus;
import ch.strasser.marvin.model.Train;
import ch.strasser.marvin.model.TrainLine;

/** Lightweight view model for assignments to ensure train info is always present. */
public class AssignmentDto {
    private Long id;
    private LocalDate date;
    private AssignmentStatus status;
    private String trainVehicleNumber;
    private String lineName;
    private Train train;
    private TrainLine line;

    public AssignmentDto(Long id,
                         LocalDate date,
                         AssignmentStatus status,
                         String trainVehicleNumber,
                         String lineName,
                         Train train,
                         TrainLine line) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.trainVehicleNumber = trainVehicleNumber;
        this.lineName = lineName;
        this.train = train;
        this.line = line;
    }

    public Long getId() { return id; }
    public LocalDate getDate() { return date; }
    public AssignmentStatus getStatus() { return status; }
    public String getTrainVehicleNumber() { return trainVehicleNumber; }
    public String getLineName() { return lineName; }
    public Train getTrain() { return train; }
    public TrainLine getLine() { return line; }
}

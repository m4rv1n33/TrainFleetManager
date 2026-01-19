package ch.strasser.marvin.dto;

import ch.strasser.marvin.model.TrainStatus;

public class TrainStatusUpdateRequest {
    private TrainStatus status;
    public TrainStatus getStatus() { return status; }
    public void setStatus(TrainStatus status) { this.status = status; }
}

package ch.strasser.marvin.dto;
/**
 * <p>
 * TrainStatus Data Transfer Object
 * </p>
 *
 * @author Marvin Strasser
 * @version 1.0
 * @since 19/01/2026
 */
public class TrainStatusDto {

    private String vehicleNumber;
    private String status;
    private String line;

    public TrainStatusDto(String vehicleNumber, String status, String line) {
        this.vehicleNumber = vehicleNumber;
        this.status = status;
        this.line = line;
    }

    // Getters & Setters
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLine() { return line; }
    public void setLine(String line) { this.line = line; }
}

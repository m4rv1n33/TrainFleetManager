package ch.strasser.marvin.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
/**
 * <p>
 * Used to initate train objects, contains a technical id as well as a real life-like vehicleNumber.
 * </p>
 *
 * @author Marvin Strasser
 * @version 1.0
 * @since 19/01/2026
 */

@Entity
@Table(name = "Train")
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String vehicleNumber; // Ex. 511-001
    private int maxSpeed;
    private int length;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainStatus status = TrainStatus.IN_SERVICE;

    @OneToOne(mappedBy = "train")
    @JsonManagedReference
    private TrainAssignment currentAssignment;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public int getMaxSpeed() { return maxSpeed; }
    public void setMaxSpeed(int maxSpeed) { this.maxSpeed = maxSpeed; }

    public int getLength() { return length; }
    public void setLength(int length) { this.length = length; }

    public TrainStatus getStatus() { return status; }
    public void setStatus(TrainStatus status) { this.status = status; }

    public TrainAssignment getCurrentAssignment() { return currentAssignment; }
    public void setCurrentAssignment(TrainAssignment currentAssignment) { this.currentAssignment = currentAssignment; }
}

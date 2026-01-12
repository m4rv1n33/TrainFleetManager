package ch.strasser.marvin.model;/*
Marvin Strasser
TrainFleetManager
12/01/2026
*/

import jakarta.persistence.*;

@Entity
@Table(name = "Train")
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //Technische ID
    private String VehicleNumber; //Fachliche Betriebsnummer: Ex. 511-001
    private int maxSpeed;
    private int length;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVehicleNumber() {
        return VehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        VehicleNumber = vehicleNumber;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public TrainStatus getStatus() {
        return status;
    }

    public void setStatus(TrainStatus status) {
        this.status = status;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
}


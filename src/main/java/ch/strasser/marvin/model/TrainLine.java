package ch.strasser.marvin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "TrainLine")
public class TrainLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String startStation;
    private String endStation;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStartStation() { return startStation; }
    public void setStartStation(String startStation) { this.startStation = startStation; }

    public String getEndStation() { return endStation; }
    public void setEndStation(String endStation) { this.endStation = endStation; }
}

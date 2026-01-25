package ch.strasser.marvin.model;

import jakarta.persistence.*;
/**
 * <p>
 * Contains id, name as well as start and endStation. Used to define a train line, ex. IR36 or IC5.
 * </p>
 *
 * @author Marvin Strasser
 * @version 1.0
 * @since 19/01/2026
 */
@Entity
@Table(name = "TrainLine")
public class TrainLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String startStation;
    private String endStation;

  
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStartStation() { return startStation; }
    public void setStartStation(String startStation) { this.startStation = startStation; }

    public String getEndStation() { return endStation; }
    public void setEndStation(String endStation) { this.endStation = endStation; }
}

package ch.strasser.marvin.model;/*
Marvin Strasser
TrainFleetManager
12/01/2026
*/

import jakarta.persistence.*;

import java.time.LocalDate;
/**
 * <p>
 * Contains id, start and endDate as well as a short description for maintenance.
 * </p>
 *
 * @author Marvin Strasser
 * @version 1.0
 * @since 19/01/2026
 */
@Entity
@Table(name = "TrainMaintenance")
public class TrainMaintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
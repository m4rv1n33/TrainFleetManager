package ch.strasser.marvin.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;
 /**
 * <p>
 * Used to link vehicles to services OR maintenance
 * </p>
 *
 * @author Marvin Strasser
 * @version 1.0
 * @since 19/01/2026
 */
@Entity
@Table(name = "TrainAssignment")
public class TrainAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentStatus status;

    @OneToOne
    @JoinColumn(name = "train_id")
    @JsonBackReference
    private Train train;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private TrainLine line;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public AssignmentStatus getStatus() { return status; }
    public void setStatus(AssignmentStatus status) { this.status = status; }

    public Train getTrain() { return train; }
    public void setTrain(Train train) { this.train = train; }

    public TrainLine getLine() { return line; }
    public void setLine(TrainLine line) { this.line = line; }
}

package info.pekny.golfcourses.round;

import info.pekny.golfcourses.course.Hole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "round_scores", uniqueConstraints = @UniqueConstraint(columnNames = {"round_id", "hole_id"}))
public class RoundScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id", nullable = false)
    private PlayedRound round;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hole_id", nullable = false)
    private Hole hole;

    @Column(nullable = false)
    private int score;

    protected RoundScore() {
    }

    public RoundScore(PlayedRound round, Hole hole, int score) {
        this.round = round;
        this.hole = hole;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public PlayedRound getRound() {
        return round;
    }

    public Hole getHole() {
        return hole;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

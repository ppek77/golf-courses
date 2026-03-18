package info.pekny.golfcourses.course;

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
@Table(name = "hole_tee_lengths", uniqueConstraints = @UniqueConstraint(columnNames = {"hole_id", "tee_id"}))
public class HoleTeeLength {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hole_id", nullable = false)
    private Hole hole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tee_id", nullable = false)
    private Tee tee;

    @Column(nullable = false)
    private int length;

    protected HoleTeeLength() {
    }

    public HoleTeeLength(Hole hole, Tee tee, int length) {
        this.hole = hole;
        this.tee = tee;
        this.length = length;
    }

    public Long getId() {
        return id;
    }

    public Hole getHole() {
        return hole;
    }

    public Tee getTee() {
        return tee;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}

package info.pekny.golfcourses.course;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "holes", uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "number"}))
public class Hole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int number;

    @Column(nullable = false)
    private int par;

    @Column(nullable = false)
    private int hcp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private GolfCourse course;

    @OneToMany(mappedBy = "hole", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HoleTeeLength> teeLengths = new ArrayList<>();

    protected Hole() {
    }

    public Hole(int number, int par, int hcp, GolfCourse course) {
        this.number = number;
        this.par = par;
        this.hcp = hcp;
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPar() {
        return par;
    }

    public void setPar(int par) {
        this.par = par;
    }

    public int getHcp() {
        return hcp;
    }

    public void setHcp(int hcp) {
        this.hcp = hcp;
    }

    public GolfCourse getCourse() {
        return course;
    }

    public List<HoleTeeLength> getTeeLengths() {
        return teeLengths;
    }
}

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

@Entity
@Table(name = "tees")
public class Tee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Double courseRating;

    private Double slopeRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private GolfCourse course;

    protected Tee() {
    }

    public Tee(String name, GolfCourse course) {
        this.name = name;
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCourseRating() {
        return courseRating;
    }

    public void setCourseRating(Double courseRating) {
        this.courseRating = courseRating;
    }

    public Double getSlopeRating() {
        return slopeRating;
    }

    public void setSlopeRating(Double slopeRating) {
        this.slopeRating = slopeRating;
    }

    public GolfCourse getCourse() {
        return course;
    }
}

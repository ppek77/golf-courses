package info.pekny.golfcourses.round;

import info.pekny.golfcourses.course.GolfCourse;
import info.pekny.golfcourses.course.Tee;
import info.pekny.golfcourses.user.User;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "played_rounds")
public class PlayedRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private GolfCourse course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tee_id", nullable = false)
    private Tee tee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoundScore> scores = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected PlayedRound() {
    }

    public PlayedRound(LocalDate date, GolfCourse course, Tee tee, User user) {
        this.date = date;
        this.course = course;
        this.tee = tee;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public GolfCourse getCourse() {
        return course;
    }

    public void setCourse(GolfCourse course) {
        this.course = course;
    }

    public Tee getTee() {
        return tee;
    }

    public void setTee(Tee tee) {
        this.tee = tee;
    }

    public User getUser() {
        return user;
    }

    public List<RoundScore> getScores() {
        return scores;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

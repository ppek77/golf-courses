package info.pekny.golfcourses.course;

import info.pekny.golfcourses.country.Country;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "golf_courses")
public class GolfCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    private String description;

    private Double officialRating;

    private Double personalRating;

    @Column(nullable = false)
    private boolean logoBall;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LengthUnit lengthUnit;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tee> tees = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("number")
    private List<Hole> holes = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    protected GolfCourse() {
    }

    public GolfCourse(String name, Country country, LengthUnit lengthUnit) {
        this.name = name;
        this.country = country;
        this.lengthUnit = lengthUnit;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getOfficialRating() {
        return officialRating;
    }

    public void setOfficialRating(Double officialRating) {
        this.officialRating = officialRating;
    }

    public Double getPersonalRating() {
        return personalRating;
    }

    public void setPersonalRating(Double personalRating) {
        this.personalRating = personalRating;
    }

    public boolean isLogoBall() {
        return logoBall;
    }

    public void setLogoBall(boolean logoBall) {
        this.logoBall = logoBall;
    }

    public LengthUnit getLengthUnit() {
        return lengthUnit;
    }

    public void setLengthUnit(LengthUnit lengthUnit) {
        this.lengthUnit = lengthUnit;
    }

    public List<Tee> getTees() {
        return tees;
    }

    public List<Hole> getHoles() {
        return holes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

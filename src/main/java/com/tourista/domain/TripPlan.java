package com.tourista.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TripPlan.
 */
@Entity
@Table(name = "trip_plan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TripPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "trip_name")
    private String tripName;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "total_days")
    private Integer totalDays;

    @Column(name = "review")
    private String review;

    @OneToMany(mappedBy = "tripPlan")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "activities", "tripPlan" }, allowSetters = true)
    private Set<TripDay> tripDays = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "plans", "friends", "userProfile" }, allowSetters = true)
    private UserProfile userProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TripPlan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTripName() {
        return this.tripName;
    }

    public TripPlan tripName(String tripName) {
        this.setTripName(tripName);
        return this;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public TripPlan startDate(String startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public TripPlan endDate(String endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getTotalDays() {
        return this.totalDays;
    }

    public TripPlan totalDays(Integer totalDays) {
        this.setTotalDays(totalDays);
        return this;
    }

    public void setTotalDays(Integer totalDays) {
        this.totalDays = totalDays;
    }

    public String getReview() {
        return this.review;
    }

    public TripPlan review(String review) {
        this.setReview(review);
        return this;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Set<TripDay> getTripDays() {
        return this.tripDays;
    }

    public void setTripDays(Set<TripDay> tripDays) {
        if (this.tripDays != null) {
            this.tripDays.forEach(i -> i.setTripPlan(null));
        }
        if (tripDays != null) {
            tripDays.forEach(i -> i.setTripPlan(this));
        }
        this.tripDays = tripDays;
    }

    public TripPlan tripDays(Set<TripDay> tripDays) {
        this.setTripDays(tripDays);
        return this;
    }

    public TripPlan addTripDays(TripDay tripDay) {
        this.tripDays.add(tripDay);
        tripDay.setTripPlan(this);
        return this;
    }

    public TripPlan removeTripDays(TripDay tripDay) {
        this.tripDays.remove(tripDay);
        tripDay.setTripPlan(null);
        return this;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public TripPlan userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TripPlan)) {
            return false;
        }
        return id != null && id.equals(((TripPlan) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TripPlan{" +
            "id=" + getId() +
            ", tripName='" + getTripName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", totalDays=" + getTotalDays() +
            ", review='" + getReview() + "'" +
            "}";
    }
}

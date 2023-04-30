package com.tourista.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TripDay.
 */
@Entity
@Table(name = "trip_day")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TripDay implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @OneToMany(mappedBy = "tripDay")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "location", "tripDay" }, allowSetters = true)
    private Set<Activity> activities = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "tripDays", "userProfile" }, allowSetters = true)
    private TripPlan tripPlan;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TripDay id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public TripDay date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Set<Activity> getActivities() {
        return this.activities;
    }

    public void setActivities(Set<Activity> activities) {
        if (this.activities != null) {
            this.activities.forEach(i -> i.setTripDay(null));
        }
        if (activities != null) {
            activities.forEach(i -> i.setTripDay(this));
        }
        this.activities = activities;
    }

    public TripDay activities(Set<Activity> activities) {
        this.setActivities(activities);
        return this;
    }

    public TripDay addActivities(Activity activity) {
        this.activities.add(activity);
        activity.setTripDay(this);
        return this;
    }

    public TripDay removeActivities(Activity activity) {
        this.activities.remove(activity);
        activity.setTripDay(null);
        return this;
    }

    public TripPlan getTripPlan() {
        return this.tripPlan;
    }

    public void setTripPlan(TripPlan tripPlan) {
        this.tripPlan = tripPlan;
    }

    public TripDay tripPlan(TripPlan tripPlan) {
        this.setTripPlan(tripPlan);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TripDay)) {
            return false;
        }
        return id != null && id.equals(((TripDay) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TripDay{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}

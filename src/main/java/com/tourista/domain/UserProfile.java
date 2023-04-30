package com.tourista.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserProfile.
 */
@Entity
@Table(name = "user_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "phone")
    private String phone;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "userProfile")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tripDays", "userProfile" }, allowSetters = true)
    private Set<TripPlan> plans = new HashSet<>();

    @OneToMany(mappedBy = "userProfile")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "plans", "friends", "userProfile" }, allowSetters = true)
    private Set<UserProfile> friends = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "plans", "friends", "userProfile" }, allowSetters = true)
    private UserProfile userProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return this.phone;
    }

    public UserProfile phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserProfile user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<TripPlan> getPlans() {
        return this.plans;
    }

    public void setPlans(Set<TripPlan> tripPlans) {
        if (this.plans != null) {
            this.plans.forEach(i -> i.setUserProfile(null));
        }
        if (tripPlans != null) {
            tripPlans.forEach(i -> i.setUserProfile(this));
        }
        this.plans = tripPlans;
    }

    public UserProfile plans(Set<TripPlan> tripPlans) {
        this.setPlans(tripPlans);
        return this;
    }

    public UserProfile addPlans(TripPlan tripPlan) {
        this.plans.add(tripPlan);
        tripPlan.setUserProfile(this);
        return this;
    }

    public UserProfile removePlans(TripPlan tripPlan) {
        this.plans.remove(tripPlan);
        tripPlan.setUserProfile(null);
        return this;
    }

    public Set<UserProfile> getFriends() {
        return this.friends;
    }

    public void setFriends(Set<UserProfile> userProfiles) {
        if (this.friends != null) {
            this.friends.forEach(i -> i.setUserProfile(null));
        }
        if (userProfiles != null) {
            userProfiles.forEach(i -> i.setUserProfile(this));
        }
        this.friends = userProfiles;
    }

    public UserProfile friends(Set<UserProfile> userProfiles) {
        this.setFriends(userProfiles);
        return this;
    }

    public UserProfile addFriends(UserProfile userProfile) {
        this.friends.add(userProfile);
        userProfile.setUserProfile(this);
        return this;
    }

    public UserProfile removeFriends(UserProfile userProfile) {
        this.friends.remove(userProfile);
        userProfile.setUserProfile(null);
        return this;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserProfile userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserProfile)) {
            return false;
        }
        return id != null && id.equals(((UserProfile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserProfile{" +
            "id=" + getId() +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}

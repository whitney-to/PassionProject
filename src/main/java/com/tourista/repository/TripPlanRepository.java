package com.tourista.repository;

import com.tourista.domain.TripPlan;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TripPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripPlanRepository extends JpaRepository<TripPlan, Long> {}

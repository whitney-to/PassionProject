package com.tourista.repository;

import com.tourista.domain.TripDay;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TripDay entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripDayRepository extends JpaRepository<TripDay, Long> {}

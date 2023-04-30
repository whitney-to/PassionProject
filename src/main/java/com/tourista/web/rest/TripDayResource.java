package com.tourista.web.rest;

import com.tourista.domain.TripDay;
import com.tourista.repository.TripDayRepository;
import com.tourista.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.tourista.domain.TripDay}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TripDayResource {

    private final Logger log = LoggerFactory.getLogger(TripDayResource.class);

    private static final String ENTITY_NAME = "tripDay";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TripDayRepository tripDayRepository;

    public TripDayResource(TripDayRepository tripDayRepository) {
        this.tripDayRepository = tripDayRepository;
    }

    /**
     * {@code POST  /trip-days} : Create a new tripDay.
     *
     * @param tripDay the tripDay to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tripDay, or with status {@code 400 (Bad Request)} if the tripDay has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trip-days")
    public ResponseEntity<TripDay> createTripDay(@RequestBody TripDay tripDay) throws URISyntaxException {
        log.debug("REST request to save TripDay : {}", tripDay);
        if (tripDay.getId() != null) {
            throw new BadRequestAlertException("A new tripDay cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TripDay result = tripDayRepository.save(tripDay);
        return ResponseEntity
            .created(new URI("/api/trip-days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trip-days/:id} : Updates an existing tripDay.
     *
     * @param id the id of the tripDay to save.
     * @param tripDay the tripDay to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripDay,
     * or with status {@code 400 (Bad Request)} if the tripDay is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tripDay couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trip-days/{id}")
    public ResponseEntity<TripDay> updateTripDay(@PathVariable(value = "id", required = false) final Long id, @RequestBody TripDay tripDay)
        throws URISyntaxException {
        log.debug("REST request to update TripDay : {}, {}", id, tripDay);
        if (tripDay.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripDay.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripDayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TripDay result = tripDayRepository.save(tripDay);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tripDay.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trip-days/:id} : Partial updates given fields of an existing tripDay, field will ignore if it is null
     *
     * @param id the id of the tripDay to save.
     * @param tripDay the tripDay to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripDay,
     * or with status {@code 400 (Bad Request)} if the tripDay is not valid,
     * or with status {@code 404 (Not Found)} if the tripDay is not found,
     * or with status {@code 500 (Internal Server Error)} if the tripDay couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trip-days/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TripDay> partialUpdateTripDay(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TripDay tripDay
    ) throws URISyntaxException {
        log.debug("REST request to partial update TripDay partially : {}, {}", id, tripDay);
        if (tripDay.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripDay.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripDayRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TripDay> result = tripDayRepository
            .findById(tripDay.getId())
            .map(existingTripDay -> {
                if (tripDay.getDate() != null) {
                    existingTripDay.setDate(tripDay.getDate());
                }

                return existingTripDay;
            })
            .map(tripDayRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tripDay.getId().toString())
        );
    }

    /**
     * {@code GET  /trip-days} : get all the tripDays.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tripDays in body.
     */
    @GetMapping("/trip-days")
    public List<TripDay> getAllTripDays() {
        log.debug("REST request to get all TripDays");
        return tripDayRepository.findAll();
    }

    /**
     * {@code GET  /trip-days/:id} : get the "id" tripDay.
     *
     * @param id the id of the tripDay to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tripDay, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trip-days/{id}")
    public ResponseEntity<TripDay> getTripDay(@PathVariable Long id) {
        log.debug("REST request to get TripDay : {}", id);
        Optional<TripDay> tripDay = tripDayRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tripDay);
    }

    /**
     * {@code DELETE  /trip-days/:id} : delete the "id" tripDay.
     *
     * @param id the id of the tripDay to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trip-days/{id}")
    public ResponseEntity<Void> deleteTripDay(@PathVariable Long id) {
        log.debug("REST request to delete TripDay : {}", id);
        tripDayRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

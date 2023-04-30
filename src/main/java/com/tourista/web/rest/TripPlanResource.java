package com.tourista.web.rest;

import com.tourista.domain.TripPlan;
import com.tourista.repository.TripPlanRepository;
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
 * REST controller for managing {@link com.tourista.domain.TripPlan}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TripPlanResource {

    private final Logger log = LoggerFactory.getLogger(TripPlanResource.class);

    private static final String ENTITY_NAME = "tripPlan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TripPlanRepository tripPlanRepository;

    public TripPlanResource(TripPlanRepository tripPlanRepository) {
        this.tripPlanRepository = tripPlanRepository;
    }

    /**
     * {@code POST  /trip-plans} : Create a new tripPlan.
     *
     * @param tripPlan the tripPlan to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tripPlan, or with status {@code 400 (Bad Request)} if the tripPlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trip-plans")
    public ResponseEntity<TripPlan> createTripPlan(@RequestBody TripPlan tripPlan) throws URISyntaxException {
        log.debug("REST request to save TripPlan : {}", tripPlan);
        if (tripPlan.getId() != null) {
            throw new BadRequestAlertException("A new tripPlan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TripPlan result = tripPlanRepository.save(tripPlan);
        return ResponseEntity
            .created(new URI("/api/trip-plans/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trip-plans/:id} : Updates an existing tripPlan.
     *
     * @param id the id of the tripPlan to save.
     * @param tripPlan the tripPlan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripPlan,
     * or with status {@code 400 (Bad Request)} if the tripPlan is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tripPlan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trip-plans/{id}")
    public ResponseEntity<TripPlan> updateTripPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TripPlan tripPlan
    ) throws URISyntaxException {
        log.debug("REST request to update TripPlan : {}, {}", id, tripPlan);
        if (tripPlan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripPlan.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TripPlan result = tripPlanRepository.save(tripPlan);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tripPlan.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trip-plans/:id} : Partial updates given fields of an existing tripPlan, field will ignore if it is null
     *
     * @param id the id of the tripPlan to save.
     * @param tripPlan the tripPlan to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tripPlan,
     * or with status {@code 400 (Bad Request)} if the tripPlan is not valid,
     * or with status {@code 404 (Not Found)} if the tripPlan is not found,
     * or with status {@code 500 (Internal Server Error)} if the tripPlan couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trip-plans/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TripPlan> partialUpdateTripPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TripPlan tripPlan
    ) throws URISyntaxException {
        log.debug("REST request to partial update TripPlan partially : {}, {}", id, tripPlan);
        if (tripPlan.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tripPlan.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tripPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TripPlan> result = tripPlanRepository
            .findById(tripPlan.getId())
            .map(existingTripPlan -> {
                if (tripPlan.getTripName() != null) {
                    existingTripPlan.setTripName(tripPlan.getTripName());
                }
                if (tripPlan.getStartDate() != null) {
                    existingTripPlan.setStartDate(tripPlan.getStartDate());
                }
                if (tripPlan.getEndDate() != null) {
                    existingTripPlan.setEndDate(tripPlan.getEndDate());
                }
                if (tripPlan.getTotalDays() != null) {
                    existingTripPlan.setTotalDays(tripPlan.getTotalDays());
                }
                if (tripPlan.getReview() != null) {
                    existingTripPlan.setReview(tripPlan.getReview());
                }

                return existingTripPlan;
            })
            .map(tripPlanRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tripPlan.getId().toString())
        );
    }

    /**
     * {@code GET  /trip-plans} : get all the tripPlans.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tripPlans in body.
     */
    @GetMapping("/trip-plans")
    public List<TripPlan> getAllTripPlans() {
        log.debug("REST request to get all TripPlans");
        return tripPlanRepository.findAll();
    }

    /**
     * {@code GET  /trip-plans/:id} : get the "id" tripPlan.
     *
     * @param id the id of the tripPlan to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tripPlan, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trip-plans/{id}")
    public ResponseEntity<TripPlan> getTripPlan(@PathVariable Long id) {
        log.debug("REST request to get TripPlan : {}", id);
        Optional<TripPlan> tripPlan = tripPlanRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tripPlan);
    }

    /**
     * {@code DELETE  /trip-plans/:id} : delete the "id" tripPlan.
     *
     * @param id the id of the tripPlan to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trip-plans/{id}")
    public ResponseEntity<Void> deleteTripPlan(@PathVariable Long id) {
        log.debug("REST request to delete TripPlan : {}", id);
        tripPlanRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

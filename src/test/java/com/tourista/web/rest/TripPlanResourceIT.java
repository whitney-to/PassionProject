package com.tourista.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tourista.IntegrationTest;
import com.tourista.domain.TripPlan;
import com.tourista.repository.TripPlanRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TripPlanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TripPlanResourceIT {

    private static final String DEFAULT_TRIP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TRIP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_START_DATE = "AAAAAAAAAA";
    private static final String UPDATED_START_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_END_DATE = "AAAAAAAAAA";
    private static final String UPDATED_END_DATE = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOTAL_DAYS = 1;
    private static final Integer UPDATED_TOTAL_DAYS = 2;

    private static final String DEFAULT_REVIEW = "AAAAAAAAAA";
    private static final String UPDATED_REVIEW = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/trip-plans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TripPlanRepository tripPlanRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTripPlanMockMvc;

    private TripPlan tripPlan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TripPlan createEntity(EntityManager em) {
        TripPlan tripPlan = new TripPlan()
            .tripName(DEFAULT_TRIP_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .totalDays(DEFAULT_TOTAL_DAYS)
            .review(DEFAULT_REVIEW);
        return tripPlan;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TripPlan createUpdatedEntity(EntityManager em) {
        TripPlan tripPlan = new TripPlan()
            .tripName(UPDATED_TRIP_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .totalDays(UPDATED_TOTAL_DAYS)
            .review(UPDATED_REVIEW);
        return tripPlan;
    }

    @BeforeEach
    public void initTest() {
        tripPlan = createEntity(em);
    }

    @Test
    @Transactional
    void createTripPlan() throws Exception {
        int databaseSizeBeforeCreate = tripPlanRepository.findAll().size();
        // Create the TripPlan
        restTripPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripPlan)))
            .andExpect(status().isCreated());

        // Validate the TripPlan in the database
        List<TripPlan> tripPlanList = tripPlanRepository.findAll();
        assertThat(tripPlanList).hasSize(databaseSizeBeforeCreate + 1);
        TripPlan testTripPlan = tripPlanList.get(tripPlanList.size() - 1);
        assertThat(testTripPlan.getTripName()).isEqualTo(DEFAULT_TRIP_NAME);
        assertThat(testTripPlan.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTripPlan.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testTripPlan.getTotalDays()).isEqualTo(DEFAULT_TOTAL_DAYS);
        assertThat(testTripPlan.getReview()).isEqualTo(DEFAULT_REVIEW);
    }

    @Test
    @Transactional
    void createTripPlanWithExistingId() throws Exception {
        // Create the TripPlan with an existing ID
        tripPlan.setId(1L);

        int databaseSizeBeforeCreate = tripPlanRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTripPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripPlan)))
            .andExpect(status().isBadRequest());

        // Validate the TripPlan in the database
        List<TripPlan> tripPlanList = tripPlanRepository.findAll();
        assertThat(tripPlanList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTripPlans() throws Exception {
        // Initialize the database
        tripPlanRepository.saveAndFlush(tripPlan);

        // Get all the tripPlanList
        restTripPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tripPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].tripName").value(hasItem(DEFAULT_TRIP_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE)))
            .andExpect(jsonPath("$.[*].totalDays").value(hasItem(DEFAULT_TOTAL_DAYS)))
            .andExpect(jsonPath("$.[*].review").value(hasItem(DEFAULT_REVIEW)));
    }

    @Test
    @Transactional
    void getTripPlan() throws Exception {
        // Initialize the database
        tripPlanRepository.saveAndFlush(tripPlan);

        // Get the tripPlan
        restTripPlanMockMvc
            .perform(get(ENTITY_API_URL_ID, tripPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tripPlan.getId().intValue()))
            .andExpect(jsonPath("$.tripName").value(DEFAULT_TRIP_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE))
            .andExpect(jsonPath("$.totalDays").value(DEFAULT_TOTAL_DAYS))
            .andExpect(jsonPath("$.review").value(DEFAULT_REVIEW));
    }

    @Test
    @Transactional
    void getNonExistingTripPlan() throws Exception {
        // Get the tripPlan
        restTripPlanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTripPlan() throws Exception {
        // Initialize the database
        tripPlanRepository.saveAndFlush(tripPlan);

        int databaseSizeBeforeUpdate = tripPlanRepository.findAll().size();

        // Update the tripPlan
        TripPlan updatedTripPlan = tripPlanRepository.findById(tripPlan.getId()).get();
        // Disconnect from session so that the updates on updatedTripPlan are not directly saved in db
        em.detach(updatedTripPlan);
        updatedTripPlan
            .tripName(UPDATED_TRIP_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .totalDays(UPDATED_TOTAL_DAYS)
            .review(UPDATED_REVIEW);

        restTripPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTripPlan.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTripPlan))
            )
            .andExpect(status().isOk());

        // Validate the TripPlan in the database
        List<TripPlan> tripPlanList = tripPlanRepository.findAll();
        assertThat(tripPlanList).hasSize(databaseSizeBeforeUpdate);
        TripPlan testTripPlan = tripPlanList.get(tripPlanList.size() - 1);
        assertThat(testTripPlan.getTripName()).isEqualTo(UPDATED_TRIP_NAME);
        assertThat(testTripPlan.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTripPlan.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testTripPlan.getTotalDays()).isEqualTo(UPDATED_TOTAL_DAYS);
        assertThat(testTripPlan.getReview()).isEqualTo(UPDATED_REVIEW);
    }

    @Test
    @Transactional
    void putNonExistingTripPlan() throws Exception {
        int databaseSizeBeforeUpdate = tripPlanRepository.findAll().size();
        tripPlan.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tripPlan.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tripPlan))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripPlan in the database
        List<TripPlan> tripPlanList = tripPlanRepository.findAll();
        assertThat(tripPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTripPlan() throws Exception {
        int databaseSizeBeforeUpdate = tripPlanRepository.findAll().size();
        tripPlan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tripPlan))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripPlan in the database
        List<TripPlan> tripPlanList = tripPlanRepository.findAll();
        assertThat(tripPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTripPlan() throws Exception {
        int databaseSizeBeforeUpdate = tripPlanRepository.findAll().size();
        tripPlan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripPlanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripPlan)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TripPlan in the database
        List<TripPlan> tripPlanList = tripPlanRepository.findAll();
        assertThat(tripPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTripPlanWithPatch() throws Exception {
        // Initialize the database
        tripPlanRepository.saveAndFlush(tripPlan);

        int databaseSizeBeforeUpdate = tripPlanRepository.findAll().size();

        // Update the tripPlan using partial update
        TripPlan partialUpdatedTripPlan = new TripPlan();
        partialUpdatedTripPlan.setId(tripPlan.getId());

        partialUpdatedTripPlan.tripName(UPDATED_TRIP_NAME).endDate(UPDATED_END_DATE);

        restTripPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTripPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTripPlan))
            )
            .andExpect(status().isOk());

        // Validate the TripPlan in the database
        List<TripPlan> tripPlanList = tripPlanRepository.findAll();
        assertThat(tripPlanList).hasSize(databaseSizeBeforeUpdate);
        TripPlan testTripPlan = tripPlanList.get(tripPlanList.size() - 1);
        assertThat(testTripPlan.getTripName()).isEqualTo(UPDATED_TRIP_NAME);
        assertThat(testTripPlan.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTripPlan.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testTripPlan.getTotalDays()).isEqualTo(DEFAULT_TOTAL_DAYS);
        assertThat(testTripPlan.getReview()).isEqualTo(DEFAULT_REVIEW);
    }

    @Test
    @Transactional
    void fullUpdateTripPlanWithPatch() throws Exception {
        // Initialize the database
        tripPlanRepository.saveAndFlush(tripPlan);

        int databaseSizeBeforeUpdate = tripPlanRepository.findAll().size();

        // Update the tripPlan using partial update
        TripPlan partialUpdatedTripPlan = new TripPlan();
        partialUpdatedTripPlan.setId(tripPlan.getId());

        partialUpdatedTripPlan
            .tripName(UPDATED_TRIP_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .totalDays(UPDATED_TOTAL_DAYS)
            .review(UPDATED_REVIEW);

        restTripPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTripPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTripPlan))
            )
            .andExpect(status().isOk());

        // Validate the TripPlan in the database
        List<TripPlan> tripPlanList = tripPlanRepository.findAll();
        assertThat(tripPlanList).hasSize(databaseSizeBeforeUpdate);
        TripPlan testTripPlan = tripPlanList.get(tripPlanList.size() - 1);
        assertThat(testTripPlan.getTripName()).isEqualTo(UPDATED_TRIP_NAME);
        assertThat(testTripPlan.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTripPlan.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testTripPlan.getTotalDays()).isEqualTo(UPDATED_TOTAL_DAYS);
        assertThat(testTripPlan.getReview()).isEqualTo(UPDATED_REVIEW);
    }

    @Test
    @Transactional
    void patchNonExistingTripPlan() throws Exception {
        int databaseSizeBeforeUpdate = tripPlanRepository.findAll().size();
        tripPlan.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tripPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tripPlan))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripPlan in the database
        List<TripPlan> tripPlanList = tripPlanRepository.findAll();
        assertThat(tripPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTripPlan() throws Exception {
        int databaseSizeBeforeUpdate = tripPlanRepository.findAll().size();
        tripPlan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tripPlan))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripPlan in the database
        List<TripPlan> tripPlanList = tripPlanRepository.findAll();
        assertThat(tripPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTripPlan() throws Exception {
        int databaseSizeBeforeUpdate = tripPlanRepository.findAll().size();
        tripPlan.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripPlanMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tripPlan)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TripPlan in the database
        List<TripPlan> tripPlanList = tripPlanRepository.findAll();
        assertThat(tripPlanList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTripPlan() throws Exception {
        // Initialize the database
        tripPlanRepository.saveAndFlush(tripPlan);

        int databaseSizeBeforeDelete = tripPlanRepository.findAll().size();

        // Delete the tripPlan
        restTripPlanMockMvc
            .perform(delete(ENTITY_API_URL_ID, tripPlan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TripPlan> tripPlanList = tripPlanRepository.findAll();
        assertThat(tripPlanList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

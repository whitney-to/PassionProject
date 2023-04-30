package com.tourista.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tourista.IntegrationTest;
import com.tourista.domain.TripDay;
import com.tourista.repository.TripDayRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link TripDayResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TripDayResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/trip-days";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TripDayRepository tripDayRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTripDayMockMvc;

    private TripDay tripDay;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TripDay createEntity(EntityManager em) {
        TripDay tripDay = new TripDay().date(DEFAULT_DATE);
        return tripDay;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TripDay createUpdatedEntity(EntityManager em) {
        TripDay tripDay = new TripDay().date(UPDATED_DATE);
        return tripDay;
    }

    @BeforeEach
    public void initTest() {
        tripDay = createEntity(em);
    }

    @Test
    @Transactional
    void createTripDay() throws Exception {
        int databaseSizeBeforeCreate = tripDayRepository.findAll().size();
        // Create the TripDay
        restTripDayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripDay)))
            .andExpect(status().isCreated());

        // Validate the TripDay in the database
        List<TripDay> tripDayList = tripDayRepository.findAll();
        assertThat(tripDayList).hasSize(databaseSizeBeforeCreate + 1);
        TripDay testTripDay = tripDayList.get(tripDayList.size() - 1);
        assertThat(testTripDay.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createTripDayWithExistingId() throws Exception {
        // Create the TripDay with an existing ID
        tripDay.setId(1L);

        int databaseSizeBeforeCreate = tripDayRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTripDayMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripDay)))
            .andExpect(status().isBadRequest());

        // Validate the TripDay in the database
        List<TripDay> tripDayList = tripDayRepository.findAll();
        assertThat(tripDayList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTripDays() throws Exception {
        // Initialize the database
        tripDayRepository.saveAndFlush(tripDay);

        // Get all the tripDayList
        restTripDayMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tripDay.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getTripDay() throws Exception {
        // Initialize the database
        tripDayRepository.saveAndFlush(tripDay);

        // Get the tripDay
        restTripDayMockMvc
            .perform(get(ENTITY_API_URL_ID, tripDay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tripDay.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTripDay() throws Exception {
        // Get the tripDay
        restTripDayMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTripDay() throws Exception {
        // Initialize the database
        tripDayRepository.saveAndFlush(tripDay);

        int databaseSizeBeforeUpdate = tripDayRepository.findAll().size();

        // Update the tripDay
        TripDay updatedTripDay = tripDayRepository.findById(tripDay.getId()).get();
        // Disconnect from session so that the updates on updatedTripDay are not directly saved in db
        em.detach(updatedTripDay);
        updatedTripDay.date(UPDATED_DATE);

        restTripDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTripDay.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTripDay))
            )
            .andExpect(status().isOk());

        // Validate the TripDay in the database
        List<TripDay> tripDayList = tripDayRepository.findAll();
        assertThat(tripDayList).hasSize(databaseSizeBeforeUpdate);
        TripDay testTripDay = tripDayList.get(tripDayList.size() - 1);
        assertThat(testTripDay.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTripDay() throws Exception {
        int databaseSizeBeforeUpdate = tripDayRepository.findAll().size();
        tripDay.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tripDay.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tripDay))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripDay in the database
        List<TripDay> tripDayList = tripDayRepository.findAll();
        assertThat(tripDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTripDay() throws Exception {
        int databaseSizeBeforeUpdate = tripDayRepository.findAll().size();
        tripDay.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripDayMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tripDay))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripDay in the database
        List<TripDay> tripDayList = tripDayRepository.findAll();
        assertThat(tripDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTripDay() throws Exception {
        int databaseSizeBeforeUpdate = tripDayRepository.findAll().size();
        tripDay.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripDayMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tripDay)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TripDay in the database
        List<TripDay> tripDayList = tripDayRepository.findAll();
        assertThat(tripDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTripDayWithPatch() throws Exception {
        // Initialize the database
        tripDayRepository.saveAndFlush(tripDay);

        int databaseSizeBeforeUpdate = tripDayRepository.findAll().size();

        // Update the tripDay using partial update
        TripDay partialUpdatedTripDay = new TripDay();
        partialUpdatedTripDay.setId(tripDay.getId());

        partialUpdatedTripDay.date(UPDATED_DATE);

        restTripDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTripDay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTripDay))
            )
            .andExpect(status().isOk());

        // Validate the TripDay in the database
        List<TripDay> tripDayList = tripDayRepository.findAll();
        assertThat(tripDayList).hasSize(databaseSizeBeforeUpdate);
        TripDay testTripDay = tripDayList.get(tripDayList.size() - 1);
        assertThat(testTripDay.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTripDayWithPatch() throws Exception {
        // Initialize the database
        tripDayRepository.saveAndFlush(tripDay);

        int databaseSizeBeforeUpdate = tripDayRepository.findAll().size();

        // Update the tripDay using partial update
        TripDay partialUpdatedTripDay = new TripDay();
        partialUpdatedTripDay.setId(tripDay.getId());

        partialUpdatedTripDay.date(UPDATED_DATE);

        restTripDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTripDay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTripDay))
            )
            .andExpect(status().isOk());

        // Validate the TripDay in the database
        List<TripDay> tripDayList = tripDayRepository.findAll();
        assertThat(tripDayList).hasSize(databaseSizeBeforeUpdate);
        TripDay testTripDay = tripDayList.get(tripDayList.size() - 1);
        assertThat(testTripDay.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTripDay() throws Exception {
        int databaseSizeBeforeUpdate = tripDayRepository.findAll().size();
        tripDay.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTripDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tripDay.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tripDay))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripDay in the database
        List<TripDay> tripDayList = tripDayRepository.findAll();
        assertThat(tripDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTripDay() throws Exception {
        int databaseSizeBeforeUpdate = tripDayRepository.findAll().size();
        tripDay.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripDayMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tripDay))
            )
            .andExpect(status().isBadRequest());

        // Validate the TripDay in the database
        List<TripDay> tripDayList = tripDayRepository.findAll();
        assertThat(tripDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTripDay() throws Exception {
        int databaseSizeBeforeUpdate = tripDayRepository.findAll().size();
        tripDay.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTripDayMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tripDay)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TripDay in the database
        List<TripDay> tripDayList = tripDayRepository.findAll();
        assertThat(tripDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTripDay() throws Exception {
        // Initialize the database
        tripDayRepository.saveAndFlush(tripDay);

        int databaseSizeBeforeDelete = tripDayRepository.findAll().size();

        // Delete the tripDay
        restTripDayMockMvc
            .perform(delete(ENTITY_API_URL_ID, tripDay.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TripDay> tripDayList = tripDayRepository.findAll();
        assertThat(tripDayList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

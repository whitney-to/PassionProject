package com.tourista.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tourista.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TripPlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TripPlan.class);
        TripPlan tripPlan1 = new TripPlan();
        tripPlan1.setId(1L);
        TripPlan tripPlan2 = new TripPlan();
        tripPlan2.setId(tripPlan1.getId());
        assertThat(tripPlan1).isEqualTo(tripPlan2);
        tripPlan2.setId(2L);
        assertThat(tripPlan1).isNotEqualTo(tripPlan2);
        tripPlan1.setId(null);
        assertThat(tripPlan1).isNotEqualTo(tripPlan2);
    }
}

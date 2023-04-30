package com.tourista.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.tourista.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TripDayTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TripDay.class);
        TripDay tripDay1 = new TripDay();
        tripDay1.setId(1L);
        TripDay tripDay2 = new TripDay();
        tripDay2.setId(tripDay1.getId());
        assertThat(tripDay1).isEqualTo(tripDay2);
        tripDay2.setId(2L);
        assertThat(tripDay1).isNotEqualTo(tripDay2);
        tripDay1.setId(null);
        assertThat(tripDay1).isNotEqualTo(tripDay2);
    }
}

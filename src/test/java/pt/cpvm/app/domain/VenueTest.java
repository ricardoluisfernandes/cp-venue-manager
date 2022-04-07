package pt.cpvm.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pt.cpvm.app.web.rest.TestUtil;

class VenueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Venue.class);
        Venue venue1 = new Venue();
        venue1.setId(1L);
        Venue venue2 = new Venue();
        venue2.setId(venue1.getId());
        assertThat(venue1).isEqualTo(venue2);
        venue2.setId(2L);
        assertThat(venue1).isNotEqualTo(venue2);
        venue1.setId(null);
        assertThat(venue1).isNotEqualTo(venue2);
    }
}

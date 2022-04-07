package pt.cpvm.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pt.cpvm.app.web.rest.TestUtil;

class MemberAvailabilityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemberAvailability.class);
        MemberAvailability memberAvailability1 = new MemberAvailability();
        memberAvailability1.setId(1L);
        MemberAvailability memberAvailability2 = new MemberAvailability();
        memberAvailability2.setId(memberAvailability1.getId());
        assertThat(memberAvailability1).isEqualTo(memberAvailability2);
        memberAvailability2.setId(2L);
        assertThat(memberAvailability1).isNotEqualTo(memberAvailability2);
        memberAvailability1.setId(null);
        assertThat(memberAvailability1).isNotEqualTo(memberAvailability2);
    }
}

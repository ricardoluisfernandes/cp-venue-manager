package pt.cpvm.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pt.cpvm.app.web.rest.TestUtil;

class MemberAvailabilityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MemberAvailabilityDTO.class);
        MemberAvailabilityDTO memberAvailabilityDTO1 = new MemberAvailabilityDTO();
        memberAvailabilityDTO1.setId(1L);
        MemberAvailabilityDTO memberAvailabilityDTO2 = new MemberAvailabilityDTO();
        assertThat(memberAvailabilityDTO1).isNotEqualTo(memberAvailabilityDTO2);
        memberAvailabilityDTO2.setId(memberAvailabilityDTO1.getId());
        assertThat(memberAvailabilityDTO1).isEqualTo(memberAvailabilityDTO2);
        memberAvailabilityDTO2.setId(2L);
        assertThat(memberAvailabilityDTO1).isNotEqualTo(memberAvailabilityDTO2);
        memberAvailabilityDTO1.setId(null);
        assertThat(memberAvailabilityDTO1).isNotEqualTo(memberAvailabilityDTO2);
    }
}

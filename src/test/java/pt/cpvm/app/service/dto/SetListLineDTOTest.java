package pt.cpvm.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pt.cpvm.app.web.rest.TestUtil;

class SetListLineDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SetListLineDTO.class);
        SetListLineDTO setListLineDTO1 = new SetListLineDTO();
        setListLineDTO1.setId(1L);
        SetListLineDTO setListLineDTO2 = new SetListLineDTO();
        assertThat(setListLineDTO1).isNotEqualTo(setListLineDTO2);
        setListLineDTO2.setId(setListLineDTO1.getId());
        assertThat(setListLineDTO1).isEqualTo(setListLineDTO2);
        setListLineDTO2.setId(2L);
        assertThat(setListLineDTO1).isNotEqualTo(setListLineDTO2);
        setListLineDTO1.setId(null);
        assertThat(setListLineDTO1).isNotEqualTo(setListLineDTO2);
    }
}

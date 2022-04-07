package pt.cpvm.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pt.cpvm.app.web.rest.TestUtil;

class SetListDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SetListDTO.class);
        SetListDTO setListDTO1 = new SetListDTO();
        setListDTO1.setId(1L);
        SetListDTO setListDTO2 = new SetListDTO();
        assertThat(setListDTO1).isNotEqualTo(setListDTO2);
        setListDTO2.setId(setListDTO1.getId());
        assertThat(setListDTO1).isEqualTo(setListDTO2);
        setListDTO2.setId(2L);
        assertThat(setListDTO1).isNotEqualTo(setListDTO2);
        setListDTO1.setId(null);
        assertThat(setListDTO1).isNotEqualTo(setListDTO2);
    }
}

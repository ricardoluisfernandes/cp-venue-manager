package pt.cpvm.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pt.cpvm.app.web.rest.TestUtil;

class SongDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SongDTO.class);
        SongDTO songDTO1 = new SongDTO();
        songDTO1.setId(1L);
        SongDTO songDTO2 = new SongDTO();
        assertThat(songDTO1).isNotEqualTo(songDTO2);
        songDTO2.setId(songDTO1.getId());
        assertThat(songDTO1).isEqualTo(songDTO2);
        songDTO2.setId(2L);
        assertThat(songDTO1).isNotEqualTo(songDTO2);
        songDTO1.setId(null);
        assertThat(songDTO1).isNotEqualTo(songDTO2);
    }
}

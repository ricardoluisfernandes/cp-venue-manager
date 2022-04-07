package pt.cpvm.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pt.cpvm.app.web.rest.TestUtil;

class SetListLineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SetListLine.class);
        SetListLine setListLine1 = new SetListLine();
        setListLine1.setId(1L);
        SetListLine setListLine2 = new SetListLine();
        setListLine2.setId(setListLine1.getId());
        assertThat(setListLine1).isEqualTo(setListLine2);
        setListLine2.setId(2L);
        assertThat(setListLine1).isNotEqualTo(setListLine2);
        setListLine1.setId(null);
        assertThat(setListLine1).isNotEqualTo(setListLine2);
    }
}

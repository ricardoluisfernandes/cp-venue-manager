package pt.cpvm.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pt.cpvm.app.web.rest.TestUtil;

class SetListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SetList.class);
        SetList setList1 = new SetList();
        setList1.setId(1L);
        SetList setList2 = new SetList();
        setList2.setId(setList1.getId());
        assertThat(setList1).isEqualTo(setList2);
        setList2.setId(2L);
        assertThat(setList1).isNotEqualTo(setList2);
        setList1.setId(null);
        assertThat(setList1).isNotEqualTo(setList2);
    }
}

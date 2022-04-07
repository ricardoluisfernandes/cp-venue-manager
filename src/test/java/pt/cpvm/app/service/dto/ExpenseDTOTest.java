package pt.cpvm.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pt.cpvm.app.web.rest.TestUtil;

class ExpenseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpenseDTO.class);
        ExpenseDTO expenseDTO1 = new ExpenseDTO();
        expenseDTO1.setId(1L);
        ExpenseDTO expenseDTO2 = new ExpenseDTO();
        assertThat(expenseDTO1).isNotEqualTo(expenseDTO2);
        expenseDTO2.setId(expenseDTO1.getId());
        assertThat(expenseDTO1).isEqualTo(expenseDTO2);
        expenseDTO2.setId(2L);
        assertThat(expenseDTO1).isNotEqualTo(expenseDTO2);
        expenseDTO1.setId(null);
        assertThat(expenseDTO1).isNotEqualTo(expenseDTO2);
    }
}

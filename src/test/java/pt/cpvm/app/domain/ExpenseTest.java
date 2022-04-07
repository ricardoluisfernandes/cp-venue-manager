package pt.cpvm.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pt.cpvm.app.web.rest.TestUtil;

class ExpenseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Expense.class);
        Expense expense1 = new Expense();
        expense1.setId(1L);
        Expense expense2 = new Expense();
        expense2.setId(expense1.getId());
        assertThat(expense1).isEqualTo(expense2);
        expense2.setId(2L);
        assertThat(expense1).isNotEqualTo(expense2);
        expense1.setId(null);
        assertThat(expense1).isNotEqualTo(expense2);
    }
}

package pt.cpvm.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pt.cpvm.app.web.rest.TestUtil;

class ContractorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContractorDTO.class);
        ContractorDTO contractorDTO1 = new ContractorDTO();
        contractorDTO1.setId(1L);
        ContractorDTO contractorDTO2 = new ContractorDTO();
        assertThat(contractorDTO1).isNotEqualTo(contractorDTO2);
        contractorDTO2.setId(contractorDTO1.getId());
        assertThat(contractorDTO1).isEqualTo(contractorDTO2);
        contractorDTO2.setId(2L);
        assertThat(contractorDTO1).isNotEqualTo(contractorDTO2);
        contractorDTO1.setId(null);
        assertThat(contractorDTO1).isNotEqualTo(contractorDTO2);
    }
}

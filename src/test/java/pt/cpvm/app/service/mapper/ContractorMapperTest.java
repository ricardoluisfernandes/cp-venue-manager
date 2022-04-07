package pt.cpvm.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContractorMapperTest {

    private ContractorMapper contractorMapper;

    @BeforeEach
    public void setUp() {
        contractorMapper = new ContractorMapperImpl();
    }
}

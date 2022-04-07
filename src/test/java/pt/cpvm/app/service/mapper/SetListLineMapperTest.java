package pt.cpvm.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SetListLineMapperTest {

    private SetListLineMapper setListLineMapper;

    @BeforeEach
    public void setUp() {
        setListLineMapper = new SetListLineMapperImpl();
    }
}

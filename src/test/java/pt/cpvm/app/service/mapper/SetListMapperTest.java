package pt.cpvm.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SetListMapperTest {

    private SetListMapper setListMapper;

    @BeforeEach
    public void setUp() {
        setListMapper = new SetListMapperImpl();
    }
}

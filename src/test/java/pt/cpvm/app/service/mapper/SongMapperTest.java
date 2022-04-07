package pt.cpvm.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SongMapperTest {

    private SongMapper songMapper;

    @BeforeEach
    public void setUp() {
        songMapper = new SongMapperImpl();
    }
}

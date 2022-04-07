package pt.cpvm.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VenueMapperTest {

    private VenueMapper venueMapper;

    @BeforeEach
    public void setUp() {
        venueMapper = new VenueMapperImpl();
    }
}

package pt.cpvm.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemberAvailabilityMapperTest {

    private MemberAvailabilityMapper memberAvailabilityMapper;

    @BeforeEach
    public void setUp() {
        memberAvailabilityMapper = new MemberAvailabilityMapperImpl();
    }
}

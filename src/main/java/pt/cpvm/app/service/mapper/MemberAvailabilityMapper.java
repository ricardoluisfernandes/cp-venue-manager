package pt.cpvm.app.service.mapper;

import org.mapstruct.*;
import pt.cpvm.app.domain.Member;
import pt.cpvm.app.domain.MemberAvailability;
import pt.cpvm.app.domain.Venue;
import pt.cpvm.app.service.dto.MemberAvailabilityDTO;
import pt.cpvm.app.service.dto.MemberDTO;
import pt.cpvm.app.service.dto.VenueDTO;

/**
 * Mapper for the entity {@link MemberAvailability} and its DTO {@link MemberAvailabilityDTO}.
 */
@Mapper(componentModel = "spring")
public interface MemberAvailabilityMapper extends EntityMapper<MemberAvailabilityDTO, MemberAvailability> {
    @Mapping(target = "member", source = "member", qualifiedByName = "memberId")
    @Mapping(target = "venue", source = "venue", qualifiedByName = "venueId")
    MemberAvailabilityDTO toDto(MemberAvailability s);

    @Named("memberId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MemberDTO toDtoMemberId(Member member);

    @Named("venueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VenueDTO toDtoVenueId(Venue venue);
}

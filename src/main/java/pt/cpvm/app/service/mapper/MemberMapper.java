package pt.cpvm.app.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import pt.cpvm.app.domain.Member;
import pt.cpvm.app.domain.Venue;
import pt.cpvm.app.service.dto.MemberDTO;
import pt.cpvm.app.service.dto.VenueDTO;

/**
 * Mapper for the entity {@link Member} and its DTO {@link MemberDTO}.
 */
@Mapper(componentModel = "spring")
public interface MemberMapper extends EntityMapper<MemberDTO, Member> {
    @Mapping(target = "venues", source = "venues", qualifiedByName = "venueIdSet")
    MemberDTO toDto(Member s);

    @Mapping(target = "removeVenue", ignore = true)
    Member toEntity(MemberDTO memberDTO);

    @Named("venueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VenueDTO toDtoVenueId(Venue venue);

    @Named("venueIdSet")
    default Set<VenueDTO> toDtoVenueIdSet(Set<Venue> venue) {
        return venue.stream().map(this::toDtoVenueId).collect(Collectors.toSet());
    }
}

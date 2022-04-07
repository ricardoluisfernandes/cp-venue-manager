package pt.cpvm.app.service.mapper;

import org.mapstruct.*;
import pt.cpvm.app.domain.SetList;
import pt.cpvm.app.domain.Venue;
import pt.cpvm.app.service.dto.SetListDTO;
import pt.cpvm.app.service.dto.VenueDTO;

/**
 * Mapper for the entity {@link SetList} and its DTO {@link SetListDTO}.
 */
@Mapper(componentModel = "spring")
public interface SetListMapper extends EntityMapper<SetListDTO, SetList> {
    @Mapping(target = "venue", source = "venue", qualifiedByName = "venueId")
    SetListDTO toDto(SetList s);

    @Named("venueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VenueDTO toDtoVenueId(Venue venue);
}

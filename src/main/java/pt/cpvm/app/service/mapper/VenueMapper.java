package pt.cpvm.app.service.mapper;

import org.mapstruct.*;
import pt.cpvm.app.domain.Contractor;
import pt.cpvm.app.domain.Venue;
import pt.cpvm.app.service.dto.ContractorDTO;
import pt.cpvm.app.service.dto.VenueDTO;

/**
 * Mapper for the entity {@link Venue} and its DTO {@link VenueDTO}.
 */
@Mapper(componentModel = "spring")
public interface VenueMapper extends EntityMapper<VenueDTO, Venue> {
    @Mapping(target = "contractor", source = "contractor", qualifiedByName = "contractorId")
    VenueDTO toDto(Venue s);

    @Named("contractorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContractorDTO toDtoContractorId(Contractor contractor);
}

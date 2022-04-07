package pt.cpvm.app.service.mapper;

import org.mapstruct.*;
import pt.cpvm.app.domain.Contractor;
import pt.cpvm.app.service.dto.ContractorDTO;

/**
 * Mapper for the entity {@link Contractor} and its DTO {@link ContractorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContractorMapper extends EntityMapper<ContractorDTO, Contractor> {}

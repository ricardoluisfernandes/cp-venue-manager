package pt.cpvm.app.service.mapper;

import org.mapstruct.*;
import pt.cpvm.app.domain.Member;
import pt.cpvm.app.domain.Payment;
import pt.cpvm.app.domain.Venue;
import pt.cpvm.app.service.dto.MemberDTO;
import pt.cpvm.app.service.dto.PaymentDTO;
import pt.cpvm.app.service.dto.VenueDTO;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "member", source = "member", qualifiedByName = "memberId")
    @Mapping(target = "venue", source = "venue", qualifiedByName = "venueId")
    PaymentDTO toDto(Payment s);

    @Named("memberId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MemberDTO toDtoMemberId(Member member);

    @Named("venueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VenueDTO toDtoVenueId(Venue venue);
}

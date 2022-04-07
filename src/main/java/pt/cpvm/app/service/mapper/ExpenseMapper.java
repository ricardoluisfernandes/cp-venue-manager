package pt.cpvm.app.service.mapper;

import org.mapstruct.*;
import pt.cpvm.app.domain.Expense;
import pt.cpvm.app.domain.Member;
import pt.cpvm.app.domain.Payment;
import pt.cpvm.app.domain.Venue;
import pt.cpvm.app.service.dto.ExpenseDTO;
import pt.cpvm.app.service.dto.MemberDTO;
import pt.cpvm.app.service.dto.PaymentDTO;
import pt.cpvm.app.service.dto.VenueDTO;

/**
 * Mapper for the entity {@link Expense} and its DTO {@link ExpenseDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExpenseMapper extends EntityMapper<ExpenseDTO, Expense> {
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentId")
    @Mapping(target = "member", source = "member", qualifiedByName = "memberId")
    @Mapping(target = "venue", source = "venue", qualifiedByName = "venueId")
    ExpenseDTO toDto(Expense s);

    @Named("paymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoPaymentId(Payment payment);

    @Named("memberId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MemberDTO toDtoMemberId(Member member);

    @Named("venueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VenueDTO toDtoVenueId(Venue venue);
}

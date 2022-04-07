package pt.cpvm.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;
import pt.cpvm.app.domain.enumeration.ExpenseStatus;
import pt.cpvm.app.domain.enumeration.ExpenseType;

/**
 * A DTO for the {@link pt.cpvm.app.domain.Expense} entity.
 */
public class ExpenseDTO implements Serializable {

    private Long id;

    @NotNull
    private ExpenseType type;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal value;

    @NotNull
    private ExpenseStatus status;

    private PaymentDTO payment;

    private MemberDTO member;

    private VenueDTO venue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExpenseType getType() {
        return type;
    }

    public void setType(ExpenseType type) {
        this.type = type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public ExpenseStatus getStatus() {
        return status;
    }

    public void setStatus(ExpenseStatus status) {
        this.status = status;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }

    public MemberDTO getMember() {
        return member;
    }

    public void setMember(MemberDTO member) {
        this.member = member;
    }

    public VenueDTO getVenue() {
        return venue;
    }

    public void setVenue(VenueDTO venue) {
        this.venue = venue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExpenseDTO)) {
            return false;
        }

        ExpenseDTO expenseDTO = (ExpenseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, expenseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExpenseDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", value=" + getValue() +
            ", status='" + getStatus() + "'" +
            ", payment=" + getPayment() +
            ", member=" + getMember() +
            ", venue=" + getVenue() +
            "}";
    }
}

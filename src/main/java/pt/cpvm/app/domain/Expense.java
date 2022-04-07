package pt.cpvm.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import pt.cpvm.app.domain.enumeration.ExpenseStatus;
import pt.cpvm.app.domain.enumeration.ExpenseType;

/**
 * A Expense.
 */
@Entity
@Table(name = "expense")
public class Expense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ExpenseType type;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "value", precision = 21, scale = 2, nullable = false)
    private BigDecimal value;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ExpenseStatus status;

    @JsonIgnoreProperties(value = { "expense", "member", "venue" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Payment payment;

    @ManyToOne
    @JsonIgnoreProperties(value = { "memberAvailabilities", "expenses", "payments", "venues" }, allowSetters = true)
    private Member member;

    @ManyToOne
    @JsonIgnoreProperties(value = { "memberAvailabilities", "expenses", "payments", "contractor", "members" }, allowSetters = true)
    private Venue venue;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Expense id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExpenseType getType() {
        return this.type;
    }

    public Expense type(ExpenseType type) {
        this.setType(type);
        return this;
    }

    public void setType(ExpenseType type) {
        this.type = type;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public Expense value(BigDecimal value) {
        this.setValue(value);
        return this;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public ExpenseStatus getStatus() {
        return this.status;
    }

    public Expense status(ExpenseStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ExpenseStatus status) {
        this.status = status;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Expense payment(Payment payment) {
        this.setPayment(payment);
        return this;
    }

    public Member getMember() {
        return this.member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Expense member(Member member) {
        this.setMember(member);
        return this;
    }

    public Venue getVenue() {
        return this.venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Expense venue(Venue venue) {
        this.setVenue(venue);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Expense)) {
            return false;
        }
        return id != null && id.equals(((Expense) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Expense{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", value=" + getValue() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}

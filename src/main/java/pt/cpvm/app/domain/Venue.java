package pt.cpvm.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import pt.cpvm.app.domain.enumeration.Type;
import pt.cpvm.app.domain.enumeration.VenueStatus;

/**
 * A Venue.
 */
@Entity
@Table(name = "venue")
public class Venue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "instant", nullable = false)
    private Instant instant;

    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    @NotNull
    @Column(name = "distance", precision = 21, scale = 2, nullable = false)
    private BigDecimal distance;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VenueStatus status;

    @Column(name = "total_duration")
    private Duration totalDuration;

    @DecimalMin(value = "0")
    @Column(name = "value", precision = 21, scale = 2)
    private BigDecimal value;

    @Column(name = "consider_travel_expenses")
    private Boolean considerTravelExpenses;

    @Column(name = "travel_expenses_value", precision = 21, scale = 2)
    private BigDecimal travelExpensesValue;

    @Column(name = "do_value_retention")
    private Boolean doValueRetention;

    @Column(name = "retention_percentage", precision = 21, scale = 2)
    private BigDecimal retentionPercentage;

    @Column(name = "retention_value", precision = 21, scale = 2)
    private BigDecimal retentionValue;

    @Column(name = "member_value", precision = 21, scale = 2)
    private BigDecimal memberValue;

    @Column(name = "comments")
    private String comments;

    @OneToMany(mappedBy = "venue")
    @JsonIgnoreProperties(value = { "member", "venue" }, allowSetters = true)
    private Set<MemberAvailability> memberAvailabilities = new HashSet<>();

    @OneToMany(mappedBy = "venue")
    @JsonIgnoreProperties(value = { "payment", "member", "venue" }, allowSetters = true)
    private Set<Expense> expenses = new HashSet<>();

    @OneToMany(mappedBy = "venue")
    @JsonIgnoreProperties(value = { "expense", "member", "venue" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    @ManyToOne
    private Contractor contractor;

    @ManyToMany(mappedBy = "venues")
    @JsonIgnoreProperties(value = { "memberAvailabilities", "expenses", "payments", "venues" }, allowSetters = true)
    private Set<Member> members = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Venue id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getInstant() {
        return this.instant;
    }

    public Venue instant(Instant instant) {
        this.setInstant(instant);
        return this;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public String getLocation() {
        return this.location;
    }

    public Venue location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getDistance() {
        return this.distance;
    }

    public Venue distance(BigDecimal distance) {
        this.setDistance(distance);
        return this;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    public Type getType() {
        return this.type;
    }

    public Venue type(Type type) {
        this.setType(type);
        return this;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public VenueStatus getStatus() {
        return this.status;
    }

    public Venue status(VenueStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(VenueStatus status) {
        this.status = status;
    }

    public Duration getTotalDuration() {
        return this.totalDuration;
    }

    public Venue totalDuration(Duration totalDuration) {
        this.setTotalDuration(totalDuration);
        return this;
    }

    public void setTotalDuration(Duration totalDuration) {
        this.totalDuration = totalDuration;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public Venue value(BigDecimal value) {
        this.setValue(value);
        return this;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Boolean getConsiderTravelExpenses() {
        return this.considerTravelExpenses;
    }

    public Venue considerTravelExpenses(Boolean considerTravelExpenses) {
        this.setConsiderTravelExpenses(considerTravelExpenses);
        return this;
    }

    public void setConsiderTravelExpenses(Boolean considerTravelExpenses) {
        this.considerTravelExpenses = considerTravelExpenses;
    }

    public BigDecimal getTravelExpensesValue() {
        return this.travelExpensesValue;
    }

    public Venue travelExpensesValue(BigDecimal travelExpensesValue) {
        this.setTravelExpensesValue(travelExpensesValue);
        return this;
    }

    public void setTravelExpensesValue(BigDecimal travelExpensesValue) {
        this.travelExpensesValue = travelExpensesValue;
    }

    public Boolean getDoValueRetention() {
        return this.doValueRetention;
    }

    public Venue doValueRetention(Boolean doValueRetention) {
        this.setDoValueRetention(doValueRetention);
        return this;
    }

    public void setDoValueRetention(Boolean doValueRetention) {
        this.doValueRetention = doValueRetention;
    }

    public BigDecimal getRetentionPercentage() {
        return this.retentionPercentage;
    }

    public Venue retentionPercentage(BigDecimal retentionPercentage) {
        this.setRetentionPercentage(retentionPercentage);
        return this;
    }

    public void setRetentionPercentage(BigDecimal retentionPercentage) {
        this.retentionPercentage = retentionPercentage;
    }

    public BigDecimal getRetentionValue() {
        return this.retentionValue;
    }

    public Venue retentionValue(BigDecimal retentionValue) {
        this.setRetentionValue(retentionValue);
        return this;
    }

    public void setRetentionValue(BigDecimal retentionValue) {
        this.retentionValue = retentionValue;
    }

    public BigDecimal getMemberValue() {
        return this.memberValue;
    }

    public Venue memberValue(BigDecimal memberValue) {
        this.setMemberValue(memberValue);
        return this;
    }

    public void setMemberValue(BigDecimal memberValue) {
        this.memberValue = memberValue;
    }

    public String getComments() {
        return this.comments;
    }

    public Venue comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Set<MemberAvailability> getMemberAvailabilities() {
        return this.memberAvailabilities;
    }

    public void setMemberAvailabilities(Set<MemberAvailability> memberAvailabilities) {
        if (this.memberAvailabilities != null) {
            this.memberAvailabilities.forEach(i -> i.setVenue(null));
        }
        if (memberAvailabilities != null) {
            memberAvailabilities.forEach(i -> i.setVenue(this));
        }
        this.memberAvailabilities = memberAvailabilities;
    }

    public Venue memberAvailabilities(Set<MemberAvailability> memberAvailabilities) {
        this.setMemberAvailabilities(memberAvailabilities);
        return this;
    }

    public Venue addMemberAvailability(MemberAvailability memberAvailability) {
        this.memberAvailabilities.add(memberAvailability);
        memberAvailability.setVenue(this);
        return this;
    }

    public Venue removeMemberAvailability(MemberAvailability memberAvailability) {
        this.memberAvailabilities.remove(memberAvailability);
        memberAvailability.setVenue(null);
        return this;
    }

    public Set<Expense> getExpenses() {
        return this.expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
        if (this.expenses != null) {
            this.expenses.forEach(i -> i.setVenue(null));
        }
        if (expenses != null) {
            expenses.forEach(i -> i.setVenue(this));
        }
        this.expenses = expenses;
    }

    public Venue expenses(Set<Expense> expenses) {
        this.setExpenses(expenses);
        return this;
    }

    public Venue addExpense(Expense expense) {
        this.expenses.add(expense);
        expense.setVenue(this);
        return this;
    }

    public Venue removeExpense(Expense expense) {
        this.expenses.remove(expense);
        expense.setVenue(null);
        return this;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setVenue(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setVenue(this));
        }
        this.payments = payments;
    }

    public Venue payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Venue addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setVenue(this);
        return this;
    }

    public Venue removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setVenue(null);
        return this;
    }

    public Contractor getContractor() {
        return this.contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
    }

    public Venue contractor(Contractor contractor) {
        this.setContractor(contractor);
        return this;
    }

    public Set<Member> getMembers() {
        return this.members;
    }

    public void setMembers(Set<Member> members) {
        if (this.members != null) {
            this.members.forEach(i -> i.removeVenue(this));
        }
        if (members != null) {
            members.forEach(i -> i.addVenue(this));
        }
        this.members = members;
    }

    public Venue members(Set<Member> members) {
        this.setMembers(members);
        return this;
    }

    public Venue addMember(Member member) {
        this.members.add(member);
        member.getVenues().add(this);
        return this;
    }

    public Venue removeMember(Member member) {
        this.members.remove(member);
        member.getVenues().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Venue)) {
            return false;
        }
        return id != null && id.equals(((Venue) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Venue{" +
            "id=" + getId() +
            ", instant='" + getInstant() + "'" +
            ", location='" + getLocation() + "'" +
            ", distance=" + getDistance() +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            ", totalDuration='" + getTotalDuration() + "'" +
            ", value=" + getValue() +
            ", considerTravelExpenses='" + getConsiderTravelExpenses() + "'" +
            ", travelExpensesValue=" + getTravelExpensesValue() +
            ", doValueRetention='" + getDoValueRetention() + "'" +
            ", retentionPercentage=" + getRetentionPercentage() +
            ", retentionValue=" + getRetentionValue() +
            ", memberValue=" + getMemberValue() +
            ", comments='" + getComments() + "'" +
            "}";
    }
}

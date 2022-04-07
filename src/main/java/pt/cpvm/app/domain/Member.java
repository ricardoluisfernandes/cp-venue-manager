package pt.cpvm.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import pt.cpvm.app.domain.enumeration.VoiceType;

/**
 * A Member.
 */
@Entity
@Table(name = "member")
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "voice_type", nullable = false)
    private VoiceType voiceType;

    @OneToMany(mappedBy = "member")
    @JsonIgnoreProperties(value = { "member", "venue" }, allowSetters = true)
    private Set<MemberAvailability> memberAvailabilities = new HashSet<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnoreProperties(value = { "payment", "member", "venue" }, allowSetters = true)
    private Set<Expense> expenses = new HashSet<>();

    @OneToMany(mappedBy = "member")
    @JsonIgnoreProperties(value = { "expense", "member", "venue" }, allowSetters = true)
    private Set<Payment> payments = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_member__venue",
        joinColumns = @JoinColumn(name = "member_id"),
        inverseJoinColumns = @JoinColumn(name = "venue_id")
    )
    @JsonIgnoreProperties(value = { "memberAvailabilities", "expenses", "payments", "contractor", "members" }, allowSetters = true)
    private Set<Venue> venues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Member id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Member firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Member lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public Member email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Member phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public VoiceType getVoiceType() {
        return this.voiceType;
    }

    public Member voiceType(VoiceType voiceType) {
        this.setVoiceType(voiceType);
        return this;
    }

    public void setVoiceType(VoiceType voiceType) {
        this.voiceType = voiceType;
    }

    public Set<MemberAvailability> getMemberAvailabilities() {
        return this.memberAvailabilities;
    }

    public void setMemberAvailabilities(Set<MemberAvailability> memberAvailabilities) {
        if (this.memberAvailabilities != null) {
            this.memberAvailabilities.forEach(i -> i.setMember(null));
        }
        if (memberAvailabilities != null) {
            memberAvailabilities.forEach(i -> i.setMember(this));
        }
        this.memberAvailabilities = memberAvailabilities;
    }

    public Member memberAvailabilities(Set<MemberAvailability> memberAvailabilities) {
        this.setMemberAvailabilities(memberAvailabilities);
        return this;
    }

    public Member addMemberAvailability(MemberAvailability memberAvailability) {
        this.memberAvailabilities.add(memberAvailability);
        memberAvailability.setMember(this);
        return this;
    }

    public Member removeMemberAvailability(MemberAvailability memberAvailability) {
        this.memberAvailabilities.remove(memberAvailability);
        memberAvailability.setMember(null);
        return this;
    }

    public Set<Expense> getExpenses() {
        return this.expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
        if (this.expenses != null) {
            this.expenses.forEach(i -> i.setMember(null));
        }
        if (expenses != null) {
            expenses.forEach(i -> i.setMember(this));
        }
        this.expenses = expenses;
    }

    public Member expenses(Set<Expense> expenses) {
        this.setExpenses(expenses);
        return this;
    }

    public Member addExpense(Expense expense) {
        this.expenses.add(expense);
        expense.setMember(this);
        return this;
    }

    public Member removeExpense(Expense expense) {
        this.expenses.remove(expense);
        expense.setMember(null);
        return this;
    }

    public Set<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(Set<Payment> payments) {
        if (this.payments != null) {
            this.payments.forEach(i -> i.setMember(null));
        }
        if (payments != null) {
            payments.forEach(i -> i.setMember(this));
        }
        this.payments = payments;
    }

    public Member payments(Set<Payment> payments) {
        this.setPayments(payments);
        return this;
    }

    public Member addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setMember(this);
        return this;
    }

    public Member removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setMember(null);
        return this;
    }

    public Set<Venue> getVenues() {
        return this.venues;
    }

    public void setVenues(Set<Venue> venues) {
        this.venues = venues;
    }

    public Member venues(Set<Venue> venues) {
        this.setVenues(venues);
        return this;
    }

    public Member addVenue(Venue venue) {
        this.venues.add(venue);
        venue.getMembers().add(this);
        return this;
    }

    public Member removeVenue(Venue venue) {
        this.venues.remove(venue);
        venue.getMembers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member)) {
            return false;
        }
        return id != null && id.equals(((Member) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Member{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", voiceType='" + getVoiceType() + "'" +
            "}";
    }
}

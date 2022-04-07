package pt.cpvm.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import pt.cpvm.app.domain.enumeration.Availability;

/**
 * A MemberAvailability.
 */
@Entity
@Table(name = "member_availability")
public class MemberAvailability implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "availability", nullable = false)
    private Availability availability;

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

    public MemberAvailability id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Availability getAvailability() {
        return this.availability;
    }

    public MemberAvailability availability(Availability availability) {
        this.setAvailability(availability);
        return this;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public Member getMember() {
        return this.member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public MemberAvailability member(Member member) {
        this.setMember(member);
        return this;
    }

    public Venue getVenue() {
        return this.venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public MemberAvailability venue(Venue venue) {
        this.setVenue(venue);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberAvailability)) {
            return false;
        }
        return id != null && id.equals(((MemberAvailability) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemberAvailability{" +
            "id=" + getId() +
            ", availability='" + getAvailability() + "'" +
            "}";
    }
}

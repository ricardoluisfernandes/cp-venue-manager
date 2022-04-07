package pt.cpvm.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;
import pt.cpvm.app.domain.enumeration.Availability;

/**
 * A DTO for the {@link pt.cpvm.app.domain.MemberAvailability} entity.
 */
public class MemberAvailabilityDTO implements Serializable {

    private Long id;

    @NotNull
    private Availability availability;

    private MemberDTO member;

    private VenueDTO venue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
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
        if (!(o instanceof MemberAvailabilityDTO)) {
            return false;
        }

        MemberAvailabilityDTO memberAvailabilityDTO = (MemberAvailabilityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, memberAvailabilityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemberAvailabilityDTO{" +
            "id=" + getId() +
            ", availability='" + getAvailability() + "'" +
            ", member=" + getMember() +
            ", venue=" + getVenue() +
            "}";
    }
}

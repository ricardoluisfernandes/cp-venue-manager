package pt.cpvm.app.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;
import pt.cpvm.app.domain.enumeration.VoiceType;

/**
 * A DTO for the {@link pt.cpvm.app.domain.Member} entity.
 */
public class MemberDTO implements Serializable {

    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String email;

    private String phoneNumber;

    @NotNull
    private VoiceType voiceType;

    private Set<VenueDTO> venues = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public VoiceType getVoiceType() {
        return voiceType;
    }

    public void setVoiceType(VoiceType voiceType) {
        this.voiceType = voiceType;
    }

    public Set<VenueDTO> getVenues() {
        return venues;
    }

    public void setVenues(Set<VenueDTO> venues) {
        this.venues = venues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberDTO)) {
            return false;
        }

        MemberDTO memberDTO = (MemberDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, memberDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemberDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", voiceType='" + getVoiceType() + "'" +
            ", venues=" + getVenues() +
            "}";
    }
}

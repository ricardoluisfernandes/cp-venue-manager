package pt.cpvm.app.service.dto;

import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link pt.cpvm.app.domain.SetList} entity.
 */
public class SetListDTO implements Serializable {

    private Long id;

    @NotNull
    private String description;

    private Duration totalDuration;

    private VenueDTO venue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Duration getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Duration totalDuration) {
        this.totalDuration = totalDuration;
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
        if (!(o instanceof SetListDTO)) {
            return false;
        }

        SetListDTO setListDTO = (SetListDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, setListDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SetListDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", totalDuration='" + getTotalDuration() + "'" +
            ", venue=" + getVenue() +
            "}";
    }
}

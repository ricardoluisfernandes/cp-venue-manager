package pt.cpvm.app.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;
import pt.cpvm.app.domain.enumeration.Type;
import pt.cpvm.app.domain.enumeration.VenueStatus;

/**
 * A DTO for the {@link pt.cpvm.app.domain.Venue} entity.
 */
public class VenueDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant instant;

    @NotNull
    private String location;

    @NotNull
    private BigDecimal distance;

    @NotNull
    private Type type;

    @NotNull
    private VenueStatus status;

    private Duration totalDuration;

    @DecimalMin(value = "0")
    private BigDecimal value;

    private Boolean considerTravelExpenses;

    private BigDecimal travelExpensesValue;

    private Boolean doValueRetention;

    private BigDecimal retentionPercentage;

    private BigDecimal retentionValue;

    private BigDecimal memberValue;

    private String comments;

    private ContractorDTO contractor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public VenueStatus getStatus() {
        return status;
    }

    public void setStatus(VenueStatus status) {
        this.status = status;
    }

    public Duration getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Duration totalDuration) {
        this.totalDuration = totalDuration;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Boolean getConsiderTravelExpenses() {
        return considerTravelExpenses;
    }

    public void setConsiderTravelExpenses(Boolean considerTravelExpenses) {
        this.considerTravelExpenses = considerTravelExpenses;
    }

    public BigDecimal getTravelExpensesValue() {
        return travelExpensesValue;
    }

    public void setTravelExpensesValue(BigDecimal travelExpensesValue) {
        this.travelExpensesValue = travelExpensesValue;
    }

    public Boolean getDoValueRetention() {
        return doValueRetention;
    }

    public void setDoValueRetention(Boolean doValueRetention) {
        this.doValueRetention = doValueRetention;
    }

    public BigDecimal getRetentionPercentage() {
        return retentionPercentage;
    }

    public void setRetentionPercentage(BigDecimal retentionPercentage) {
        this.retentionPercentage = retentionPercentage;
    }

    public BigDecimal getRetentionValue() {
        return retentionValue;
    }

    public void setRetentionValue(BigDecimal retentionValue) {
        this.retentionValue = retentionValue;
    }

    public BigDecimal getMemberValue() {
        return memberValue;
    }

    public void setMemberValue(BigDecimal memberValue) {
        this.memberValue = memberValue;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public ContractorDTO getContractor() {
        return contractor;
    }

    public void setContractor(ContractorDTO contractor) {
        this.contractor = contractor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VenueDTO)) {
            return false;
        }

        VenueDTO venueDTO = (VenueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, venueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VenueDTO{" +
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
            ", contractor=" + getContractor() +
            "}";
    }
}

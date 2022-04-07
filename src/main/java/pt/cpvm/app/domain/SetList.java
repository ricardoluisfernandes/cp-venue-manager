package pt.cpvm.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A SetList.
 */
@Entity
@Table(name = "set_list")
public class SetList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "total_duration")
    private Duration totalDuration;

    @JsonIgnoreProperties(value = { "memberAvailabilities", "expenses", "payments", "contractor", "members" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Venue venue;

    @OneToMany(mappedBy = "setList")
    @JsonIgnoreProperties(value = { "song", "setList" }, allowSetters = true)
    private Set<SetListLine> setListLines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SetList id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public SetList description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Duration getTotalDuration() {
        return this.totalDuration;
    }

    public SetList totalDuration(Duration totalDuration) {
        this.setTotalDuration(totalDuration);
        return this;
    }

    public void setTotalDuration(Duration totalDuration) {
        this.totalDuration = totalDuration;
    }

    public Venue getVenue() {
        return this.venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public SetList venue(Venue venue) {
        this.setVenue(venue);
        return this;
    }

    public Set<SetListLine> getSetListLines() {
        return this.setListLines;
    }

    public void setSetListLines(Set<SetListLine> setListLines) {
        if (this.setListLines != null) {
            this.setListLines.forEach(i -> i.setSetList(null));
        }
        if (setListLines != null) {
            setListLines.forEach(i -> i.setSetList(this));
        }
        this.setListLines = setListLines;
    }

    public SetList setListLines(Set<SetListLine> setListLines) {
        this.setSetListLines(setListLines);
        return this;
    }

    public SetList addSetListLine(SetListLine setListLine) {
        this.setListLines.add(setListLine);
        setListLine.setSetList(this);
        return this;
    }

    public SetList removeSetListLine(SetListLine setListLine) {
        this.setListLines.remove(setListLine);
        setListLine.setSetList(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SetList)) {
            return false;
        }
        return id != null && id.equals(((SetList) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SetList{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", totalDuration='" + getTotalDuration() + "'" +
            "}";
    }
}

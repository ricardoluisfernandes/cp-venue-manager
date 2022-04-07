package pt.cpvm.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A SetListLine.
 */
@Entity
@Table(name = "set_list_line")
public class SetListLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "jhi_order", nullable = false)
    private Integer order;

    @ManyToOne
    private Song song;

    @ManyToOne
    @JsonIgnoreProperties(value = { "venue", "setListLines" }, allowSetters = true)
    private SetList setList;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SetListLine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrder() {
        return this.order;
    }

    public SetListLine order(Integer order) {
        this.setOrder(order);
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Song getSong() {
        return this.song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public SetListLine song(Song song) {
        this.setSong(song);
        return this;
    }

    public SetList getSetList() {
        return this.setList;
    }

    public void setSetList(SetList setList) {
        this.setList = setList;
    }

    public SetListLine setList(SetList setList) {
        this.setSetList(setList);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SetListLine)) {
            return false;
        }
        return id != null && id.equals(((SetListLine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SetListLine{" +
            "id=" + getId() +
            ", order=" + getOrder() +
            "}";
    }
}

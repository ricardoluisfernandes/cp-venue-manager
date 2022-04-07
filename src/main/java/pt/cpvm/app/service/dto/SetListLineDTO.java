package pt.cpvm.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link pt.cpvm.app.domain.SetListLine} entity.
 */
public class SetListLineDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer order;

    private SongDTO song;

    private SetListDTO setList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public SongDTO getSong() {
        return song;
    }

    public void setSong(SongDTO song) {
        this.song = song;
    }

    public SetListDTO getSetList() {
        return setList;
    }

    public void setSetList(SetListDTO setList) {
        this.setList = setList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SetListLineDTO)) {
            return false;
        }

        SetListLineDTO setListLineDTO = (SetListLineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, setListLineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SetListLineDTO{" +
            "id=" + getId() +
            ", order=" + getOrder() +
            ", song=" + getSong() +
            ", setList=" + getSetList() +
            "}";
    }
}

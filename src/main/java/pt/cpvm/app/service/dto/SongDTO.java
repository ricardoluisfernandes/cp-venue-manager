package pt.cpvm.app.service.dto;

import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link pt.cpvm.app.domain.Song} entity.
 */
public class SongDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Duration duration;

    private String score;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SongDTO)) {
            return false;
        }

        SongDTO songDTO = (SongDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, songDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SongDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", duration='" + getDuration() + "'" +
            ", score='" + getScore() + "'" +
            "}";
    }
}

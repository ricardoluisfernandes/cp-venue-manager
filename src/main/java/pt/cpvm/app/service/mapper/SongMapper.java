package pt.cpvm.app.service.mapper;

import org.mapstruct.*;
import pt.cpvm.app.domain.Song;
import pt.cpvm.app.service.dto.SongDTO;

/**
 * Mapper for the entity {@link Song} and its DTO {@link SongDTO}.
 */
@Mapper(componentModel = "spring")
public interface SongMapper extends EntityMapper<SongDTO, Song> {}

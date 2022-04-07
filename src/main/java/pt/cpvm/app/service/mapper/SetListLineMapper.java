package pt.cpvm.app.service.mapper;

import org.mapstruct.*;
import pt.cpvm.app.domain.SetList;
import pt.cpvm.app.domain.SetListLine;
import pt.cpvm.app.domain.Song;
import pt.cpvm.app.service.dto.SetListDTO;
import pt.cpvm.app.service.dto.SetListLineDTO;
import pt.cpvm.app.service.dto.SongDTO;

/**
 * Mapper for the entity {@link SetListLine} and its DTO {@link SetListLineDTO}.
 */
@Mapper(componentModel = "spring")
public interface SetListLineMapper extends EntityMapper<SetListLineDTO, SetListLine> {
    @Mapping(target = "song", source = "song", qualifiedByName = "songId")
    @Mapping(target = "setList", source = "setList", qualifiedByName = "setListId")
    SetListLineDTO toDto(SetListLine s);

    @Named("songId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SongDTO toDtoSongId(Song song);

    @Named("setListId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SetListDTO toDtoSetListId(SetList setList);
}

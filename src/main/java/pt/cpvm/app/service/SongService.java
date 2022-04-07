package pt.cpvm.app.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.cpvm.app.domain.Song;
import pt.cpvm.app.repository.SongRepository;
import pt.cpvm.app.service.dto.SongDTO;
import pt.cpvm.app.service.mapper.SongMapper;

/**
 * Service Implementation for managing {@link Song}.
 */
@Service
@Transactional
public class SongService {

    private final Logger log = LoggerFactory.getLogger(SongService.class);

    private final SongRepository songRepository;

    private final SongMapper songMapper;

    public SongService(SongRepository songRepository, SongMapper songMapper) {
        this.songRepository = songRepository;
        this.songMapper = songMapper;
    }

    /**
     * Save a song.
     *
     * @param songDTO the entity to save.
     * @return the persisted entity.
     */
    public SongDTO save(SongDTO songDTO) {
        log.debug("Request to save Song : {}", songDTO);
        Song song = songMapper.toEntity(songDTO);
        song = songRepository.save(song);
        return songMapper.toDto(song);
    }

    /**
     * Update a song.
     *
     * @param songDTO the entity to save.
     * @return the persisted entity.
     */
    public SongDTO update(SongDTO songDTO) {
        log.debug("Request to save Song : {}", songDTO);
        Song song = songMapper.toEntity(songDTO);
        song = songRepository.save(song);
        return songMapper.toDto(song);
    }

    /**
     * Partially update a song.
     *
     * @param songDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SongDTO> partialUpdate(SongDTO songDTO) {
        log.debug("Request to partially update Song : {}", songDTO);

        return songRepository
            .findById(songDTO.getId())
            .map(existingSong -> {
                songMapper.partialUpdate(existingSong, songDTO);

                return existingSong;
            })
            .map(songRepository::save)
            .map(songMapper::toDto);
    }

    /**
     * Get all the songs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SongDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Songs");
        return songRepository.findAll(pageable).map(songMapper::toDto);
    }

    /**
     * Get one song by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SongDTO> findOne(Long id) {
        log.debug("Request to get Song : {}", id);
        return songRepository.findById(id).map(songMapper::toDto);
    }

    /**
     * Delete the song by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Song : {}", id);
        songRepository.deleteById(id);
    }
}

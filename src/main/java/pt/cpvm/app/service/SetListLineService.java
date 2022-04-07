package pt.cpvm.app.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.cpvm.app.domain.SetListLine;
import pt.cpvm.app.repository.SetListLineRepository;
import pt.cpvm.app.service.dto.SetListLineDTO;
import pt.cpvm.app.service.mapper.SetListLineMapper;

/**
 * Service Implementation for managing {@link SetListLine}.
 */
@Service
@Transactional
public class SetListLineService {

    private final Logger log = LoggerFactory.getLogger(SetListLineService.class);

    private final SetListLineRepository setListLineRepository;

    private final SetListLineMapper setListLineMapper;

    public SetListLineService(SetListLineRepository setListLineRepository, SetListLineMapper setListLineMapper) {
        this.setListLineRepository = setListLineRepository;
        this.setListLineMapper = setListLineMapper;
    }

    /**
     * Save a setListLine.
     *
     * @param setListLineDTO the entity to save.
     * @return the persisted entity.
     */
    public SetListLineDTO save(SetListLineDTO setListLineDTO) {
        log.debug("Request to save SetListLine : {}", setListLineDTO);
        SetListLine setListLine = setListLineMapper.toEntity(setListLineDTO);
        setListLine = setListLineRepository.save(setListLine);
        return setListLineMapper.toDto(setListLine);
    }

    /**
     * Update a setListLine.
     *
     * @param setListLineDTO the entity to save.
     * @return the persisted entity.
     */
    public SetListLineDTO update(SetListLineDTO setListLineDTO) {
        log.debug("Request to save SetListLine : {}", setListLineDTO);
        SetListLine setListLine = setListLineMapper.toEntity(setListLineDTO);
        setListLine = setListLineRepository.save(setListLine);
        return setListLineMapper.toDto(setListLine);
    }

    /**
     * Partially update a setListLine.
     *
     * @param setListLineDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SetListLineDTO> partialUpdate(SetListLineDTO setListLineDTO) {
        log.debug("Request to partially update SetListLine : {}", setListLineDTO);

        return setListLineRepository
            .findById(setListLineDTO.getId())
            .map(existingSetListLine -> {
                setListLineMapper.partialUpdate(existingSetListLine, setListLineDTO);

                return existingSetListLine;
            })
            .map(setListLineRepository::save)
            .map(setListLineMapper::toDto);
    }

    /**
     * Get all the setListLines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SetListLineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SetListLines");
        return setListLineRepository.findAll(pageable).map(setListLineMapper::toDto);
    }

    /**
     * Get one setListLine by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SetListLineDTO> findOne(Long id) {
        log.debug("Request to get SetListLine : {}", id);
        return setListLineRepository.findById(id).map(setListLineMapper::toDto);
    }

    /**
     * Delete the setListLine by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SetListLine : {}", id);
        setListLineRepository.deleteById(id);
    }
}

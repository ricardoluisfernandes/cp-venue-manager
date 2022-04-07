package pt.cpvm.app.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.cpvm.app.domain.SetList;
import pt.cpvm.app.repository.SetListRepository;
import pt.cpvm.app.service.dto.SetListDTO;
import pt.cpvm.app.service.mapper.SetListMapper;

/**
 * Service Implementation for managing {@link SetList}.
 */
@Service
@Transactional
public class SetListService {

    private final Logger log = LoggerFactory.getLogger(SetListService.class);

    private final SetListRepository setListRepository;

    private final SetListMapper setListMapper;

    public SetListService(SetListRepository setListRepository, SetListMapper setListMapper) {
        this.setListRepository = setListRepository;
        this.setListMapper = setListMapper;
    }

    /**
     * Save a setList.
     *
     * @param setListDTO the entity to save.
     * @return the persisted entity.
     */
    public SetListDTO save(SetListDTO setListDTO) {
        log.debug("Request to save SetList : {}", setListDTO);
        SetList setList = setListMapper.toEntity(setListDTO);
        setList = setListRepository.save(setList);
        return setListMapper.toDto(setList);
    }

    /**
     * Update a setList.
     *
     * @param setListDTO the entity to save.
     * @return the persisted entity.
     */
    public SetListDTO update(SetListDTO setListDTO) {
        log.debug("Request to save SetList : {}", setListDTO);
        SetList setList = setListMapper.toEntity(setListDTO);
        setList = setListRepository.save(setList);
        return setListMapper.toDto(setList);
    }

    /**
     * Partially update a setList.
     *
     * @param setListDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SetListDTO> partialUpdate(SetListDTO setListDTO) {
        log.debug("Request to partially update SetList : {}", setListDTO);

        return setListRepository
            .findById(setListDTO.getId())
            .map(existingSetList -> {
                setListMapper.partialUpdate(existingSetList, setListDTO);

                return existingSetList;
            })
            .map(setListRepository::save)
            .map(setListMapper::toDto);
    }

    /**
     * Get all the setLists.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SetListDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SetLists");
        return setListRepository.findAll(pageable).map(setListMapper::toDto);
    }

    /**
     * Get one setList by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SetListDTO> findOne(Long id) {
        log.debug("Request to get SetList : {}", id);
        return setListRepository.findById(id).map(setListMapper::toDto);
    }

    /**
     * Delete the setList by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SetList : {}", id);
        setListRepository.deleteById(id);
    }
}

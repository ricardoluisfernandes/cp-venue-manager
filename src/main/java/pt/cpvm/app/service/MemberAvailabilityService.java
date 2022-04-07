package pt.cpvm.app.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.cpvm.app.domain.MemberAvailability;
import pt.cpvm.app.repository.MemberAvailabilityRepository;
import pt.cpvm.app.service.dto.MemberAvailabilityDTO;
import pt.cpvm.app.service.mapper.MemberAvailabilityMapper;

/**
 * Service Implementation for managing {@link MemberAvailability}.
 */
@Service
@Transactional
public class MemberAvailabilityService {

    private final Logger log = LoggerFactory.getLogger(MemberAvailabilityService.class);

    private final MemberAvailabilityRepository memberAvailabilityRepository;

    private final MemberAvailabilityMapper memberAvailabilityMapper;

    public MemberAvailabilityService(
        MemberAvailabilityRepository memberAvailabilityRepository,
        MemberAvailabilityMapper memberAvailabilityMapper
    ) {
        this.memberAvailabilityRepository = memberAvailabilityRepository;
        this.memberAvailabilityMapper = memberAvailabilityMapper;
    }

    /**
     * Save a memberAvailability.
     *
     * @param memberAvailabilityDTO the entity to save.
     * @return the persisted entity.
     */
    public MemberAvailabilityDTO save(MemberAvailabilityDTO memberAvailabilityDTO) {
        log.debug("Request to save MemberAvailability : {}", memberAvailabilityDTO);
        MemberAvailability memberAvailability = memberAvailabilityMapper.toEntity(memberAvailabilityDTO);
        memberAvailability = memberAvailabilityRepository.save(memberAvailability);
        return memberAvailabilityMapper.toDto(memberAvailability);
    }

    /**
     * Update a memberAvailability.
     *
     * @param memberAvailabilityDTO the entity to save.
     * @return the persisted entity.
     */
    public MemberAvailabilityDTO update(MemberAvailabilityDTO memberAvailabilityDTO) {
        log.debug("Request to save MemberAvailability : {}", memberAvailabilityDTO);
        MemberAvailability memberAvailability = memberAvailabilityMapper.toEntity(memberAvailabilityDTO);
        memberAvailability = memberAvailabilityRepository.save(memberAvailability);
        return memberAvailabilityMapper.toDto(memberAvailability);
    }

    /**
     * Partially update a memberAvailability.
     *
     * @param memberAvailabilityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MemberAvailabilityDTO> partialUpdate(MemberAvailabilityDTO memberAvailabilityDTO) {
        log.debug("Request to partially update MemberAvailability : {}", memberAvailabilityDTO);

        return memberAvailabilityRepository
            .findById(memberAvailabilityDTO.getId())
            .map(existingMemberAvailability -> {
                memberAvailabilityMapper.partialUpdate(existingMemberAvailability, memberAvailabilityDTO);

                return existingMemberAvailability;
            })
            .map(memberAvailabilityRepository::save)
            .map(memberAvailabilityMapper::toDto);
    }

    /**
     * Get all the memberAvailabilities.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MemberAvailabilityDTO> findAll() {
        log.debug("Request to get all MemberAvailabilities");
        return memberAvailabilityRepository
            .findAll()
            .stream()
            .map(memberAvailabilityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one memberAvailability by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MemberAvailabilityDTO> findOne(Long id) {
        log.debug("Request to get MemberAvailability : {}", id);
        return memberAvailabilityRepository.findById(id).map(memberAvailabilityMapper::toDto);
    }

    /**
     * Delete the memberAvailability by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MemberAvailability : {}", id);
        memberAvailabilityRepository.deleteById(id);
    }
}

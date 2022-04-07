package pt.cpvm.app.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.cpvm.app.domain.Contractor;
import pt.cpvm.app.repository.ContractorRepository;
import pt.cpvm.app.service.dto.ContractorDTO;
import pt.cpvm.app.service.mapper.ContractorMapper;

/**
 * Service Implementation for managing {@link Contractor}.
 */
@Service
@Transactional
public class ContractorService {

    private final Logger log = LoggerFactory.getLogger(ContractorService.class);

    private final ContractorRepository contractorRepository;

    private final ContractorMapper contractorMapper;

    public ContractorService(ContractorRepository contractorRepository, ContractorMapper contractorMapper) {
        this.contractorRepository = contractorRepository;
        this.contractorMapper = contractorMapper;
    }

    /**
     * Save a contractor.
     *
     * @param contractorDTO the entity to save.
     * @return the persisted entity.
     */
    public ContractorDTO save(ContractorDTO contractorDTO) {
        log.debug("Request to save Contractor : {}", contractorDTO);
        Contractor contractor = contractorMapper.toEntity(contractorDTO);
        contractor = contractorRepository.save(contractor);
        return contractorMapper.toDto(contractor);
    }

    /**
     * Update a contractor.
     *
     * @param contractorDTO the entity to save.
     * @return the persisted entity.
     */
    public ContractorDTO update(ContractorDTO contractorDTO) {
        log.debug("Request to save Contractor : {}", contractorDTO);
        Contractor contractor = contractorMapper.toEntity(contractorDTO);
        contractor = contractorRepository.save(contractor);
        return contractorMapper.toDto(contractor);
    }

    /**
     * Partially update a contractor.
     *
     * @param contractorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContractorDTO> partialUpdate(ContractorDTO contractorDTO) {
        log.debug("Request to partially update Contractor : {}", contractorDTO);

        return contractorRepository
            .findById(contractorDTO.getId())
            .map(existingContractor -> {
                contractorMapper.partialUpdate(existingContractor, contractorDTO);

                return existingContractor;
            })
            .map(contractorRepository::save)
            .map(contractorMapper::toDto);
    }

    /**
     * Get all the contractors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ContractorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contractors");
        return contractorRepository.findAll(pageable).map(contractorMapper::toDto);
    }

    /**
     * Get one contractor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContractorDTO> findOne(Long id) {
        log.debug("Request to get Contractor : {}", id);
        return contractorRepository.findById(id).map(contractorMapper::toDto);
    }

    /**
     * Delete the contractor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Contractor : {}", id);
        contractorRepository.deleteById(id);
    }
}

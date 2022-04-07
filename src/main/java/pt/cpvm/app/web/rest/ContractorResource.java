package pt.cpvm.app.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.cpvm.app.repository.ContractorRepository;
import pt.cpvm.app.service.ContractorService;
import pt.cpvm.app.service.dto.ContractorDTO;
import pt.cpvm.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pt.cpvm.app.domain.Contractor}.
 */
@RestController
@RequestMapping("/api")
public class ContractorResource {

    private final Logger log = LoggerFactory.getLogger(ContractorResource.class);

    private static final String ENTITY_NAME = "contractor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContractorService contractorService;

    private final ContractorRepository contractorRepository;

    public ContractorResource(ContractorService contractorService, ContractorRepository contractorRepository) {
        this.contractorService = contractorService;
        this.contractorRepository = contractorRepository;
    }

    /**
     * {@code POST  /contractors} : Create a new contractor.
     *
     * @param contractorDTO the contractorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contractorDTO, or with status {@code 400 (Bad Request)} if the contractor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contractors")
    public ResponseEntity<ContractorDTO> createContractor(@Valid @RequestBody ContractorDTO contractorDTO) throws URISyntaxException {
        log.debug("REST request to save Contractor : {}", contractorDTO);
        if (contractorDTO.getId() != null) {
            throw new BadRequestAlertException("A new contractor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContractorDTO result = contractorService.save(contractorDTO);
        return ResponseEntity
            .created(new URI("/api/contractors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contractors/:id} : Updates an existing contractor.
     *
     * @param id the id of the contractorDTO to save.
     * @param contractorDTO the contractorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractorDTO,
     * or with status {@code 400 (Bad Request)} if the contractorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contractorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contractors/{id}")
    public ResponseEntity<ContractorDTO> updateContractor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContractorDTO contractorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Contractor : {}, {}", id, contractorDTO);
        if (contractorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContractorDTO result = contractorService.update(contractorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contractorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contractors/:id} : Partial updates given fields of an existing contractor, field will ignore if it is null
     *
     * @param id the id of the contractorDTO to save.
     * @param contractorDTO the contractorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractorDTO,
     * or with status {@code 400 (Bad Request)} if the contractorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contractorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contractorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contractors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContractorDTO> partialUpdateContractor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContractorDTO contractorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Contractor partially : {}, {}", id, contractorDTO);
        if (contractorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContractorDTO> result = contractorService.partialUpdate(contractorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contractorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contractors} : get all the contractors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contractors in body.
     */
    @GetMapping("/contractors")
    public ResponseEntity<List<ContractorDTO>> getAllContractors(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Contractors");
        Page<ContractorDTO> page = contractorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contractors/:id} : get the "id" contractor.
     *
     * @param id the id of the contractorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contractorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contractors/{id}")
    public ResponseEntity<ContractorDTO> getContractor(@PathVariable Long id) {
        log.debug("REST request to get Contractor : {}", id);
        Optional<ContractorDTO> contractorDTO = contractorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contractorDTO);
    }

    /**
     * {@code DELETE  /contractors/:id} : delete the "id" contractor.
     *
     * @param id the id of the contractorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contractors/{id}")
    public ResponseEntity<Void> deleteContractor(@PathVariable Long id) {
        log.debug("REST request to delete Contractor : {}", id);
        contractorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

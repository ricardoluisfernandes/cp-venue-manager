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
import pt.cpvm.app.repository.SetListLineRepository;
import pt.cpvm.app.service.SetListLineService;
import pt.cpvm.app.service.dto.SetListLineDTO;
import pt.cpvm.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pt.cpvm.app.domain.SetListLine}.
 */
@RestController
@RequestMapping("/api")
public class SetListLineResource {

    private final Logger log = LoggerFactory.getLogger(SetListLineResource.class);

    private static final String ENTITY_NAME = "setListLine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SetListLineService setListLineService;

    private final SetListLineRepository setListLineRepository;

    public SetListLineResource(SetListLineService setListLineService, SetListLineRepository setListLineRepository) {
        this.setListLineService = setListLineService;
        this.setListLineRepository = setListLineRepository;
    }

    /**
     * {@code POST  /set-list-lines} : Create a new setListLine.
     *
     * @param setListLineDTO the setListLineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new setListLineDTO, or with status {@code 400 (Bad Request)} if the setListLine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/set-list-lines")
    public ResponseEntity<SetListLineDTO> createSetListLine(@Valid @RequestBody SetListLineDTO setListLineDTO) throws URISyntaxException {
        log.debug("REST request to save SetListLine : {}", setListLineDTO);
        if (setListLineDTO.getId() != null) {
            throw new BadRequestAlertException("A new setListLine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SetListLineDTO result = setListLineService.save(setListLineDTO);
        return ResponseEntity
            .created(new URI("/api/set-list-lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /set-list-lines/:id} : Updates an existing setListLine.
     *
     * @param id the id of the setListLineDTO to save.
     * @param setListLineDTO the setListLineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated setListLineDTO,
     * or with status {@code 400 (Bad Request)} if the setListLineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the setListLineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/set-list-lines/{id}")
    public ResponseEntity<SetListLineDTO> updateSetListLine(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SetListLineDTO setListLineDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SetListLine : {}, {}", id, setListLineDTO);
        if (setListLineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, setListLineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!setListLineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SetListLineDTO result = setListLineService.update(setListLineDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, setListLineDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /set-list-lines/:id} : Partial updates given fields of an existing setListLine, field will ignore if it is null
     *
     * @param id the id of the setListLineDTO to save.
     * @param setListLineDTO the setListLineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated setListLineDTO,
     * or with status {@code 400 (Bad Request)} if the setListLineDTO is not valid,
     * or with status {@code 404 (Not Found)} if the setListLineDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the setListLineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/set-list-lines/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SetListLineDTO> partialUpdateSetListLine(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SetListLineDTO setListLineDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SetListLine partially : {}, {}", id, setListLineDTO);
        if (setListLineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, setListLineDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!setListLineRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SetListLineDTO> result = setListLineService.partialUpdate(setListLineDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, setListLineDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /set-list-lines} : get all the setListLines.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of setListLines in body.
     */
    @GetMapping("/set-list-lines")
    public ResponseEntity<List<SetListLineDTO>> getAllSetListLines(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of SetListLines");
        Page<SetListLineDTO> page = setListLineService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /set-list-lines/:id} : get the "id" setListLine.
     *
     * @param id the id of the setListLineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the setListLineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/set-list-lines/{id}")
    public ResponseEntity<SetListLineDTO> getSetListLine(@PathVariable Long id) {
        log.debug("REST request to get SetListLine : {}", id);
        Optional<SetListLineDTO> setListLineDTO = setListLineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(setListLineDTO);
    }

    /**
     * {@code DELETE  /set-list-lines/:id} : delete the "id" setListLine.
     *
     * @param id the id of the setListLineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/set-list-lines/{id}")
    public ResponseEntity<Void> deleteSetListLine(@PathVariable Long id) {
        log.debug("REST request to delete SetListLine : {}", id);
        setListLineService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

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
import pt.cpvm.app.repository.SetListRepository;
import pt.cpvm.app.service.SetListService;
import pt.cpvm.app.service.dto.SetListDTO;
import pt.cpvm.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pt.cpvm.app.domain.SetList}.
 */
@RestController
@RequestMapping("/api")
public class SetListResource {

    private final Logger log = LoggerFactory.getLogger(SetListResource.class);

    private static final String ENTITY_NAME = "setList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SetListService setListService;

    private final SetListRepository setListRepository;

    public SetListResource(SetListService setListService, SetListRepository setListRepository) {
        this.setListService = setListService;
        this.setListRepository = setListRepository;
    }

    /**
     * {@code POST  /set-lists} : Create a new setList.
     *
     * @param setListDTO the setListDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new setListDTO, or with status {@code 400 (Bad Request)} if the setList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/set-lists")
    public ResponseEntity<SetListDTO> createSetList(@Valid @RequestBody SetListDTO setListDTO) throws URISyntaxException {
        log.debug("REST request to save SetList : {}", setListDTO);
        if (setListDTO.getId() != null) {
            throw new BadRequestAlertException("A new setList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SetListDTO result = setListService.save(setListDTO);
        return ResponseEntity
            .created(new URI("/api/set-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /set-lists/:id} : Updates an existing setList.
     *
     * @param id the id of the setListDTO to save.
     * @param setListDTO the setListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated setListDTO,
     * or with status {@code 400 (Bad Request)} if the setListDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the setListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/set-lists/{id}")
    public ResponseEntity<SetListDTO> updateSetList(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SetListDTO setListDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SetList : {}, {}", id, setListDTO);
        if (setListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, setListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!setListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SetListDTO result = setListService.update(setListDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, setListDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /set-lists/:id} : Partial updates given fields of an existing setList, field will ignore if it is null
     *
     * @param id the id of the setListDTO to save.
     * @param setListDTO the setListDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated setListDTO,
     * or with status {@code 400 (Bad Request)} if the setListDTO is not valid,
     * or with status {@code 404 (Not Found)} if the setListDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the setListDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/set-lists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SetListDTO> partialUpdateSetList(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SetListDTO setListDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SetList partially : {}, {}", id, setListDTO);
        if (setListDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, setListDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!setListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SetListDTO> result = setListService.partialUpdate(setListDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, setListDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /set-lists} : get all the setLists.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of setLists in body.
     */
    @GetMapping("/set-lists")
    public ResponseEntity<List<SetListDTO>> getAllSetLists(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of SetLists");
        Page<SetListDTO> page = setListService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /set-lists/:id} : get the "id" setList.
     *
     * @param id the id of the setListDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the setListDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/set-lists/{id}")
    public ResponseEntity<SetListDTO> getSetList(@PathVariable Long id) {
        log.debug("REST request to get SetList : {}", id);
        Optional<SetListDTO> setListDTO = setListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(setListDTO);
    }

    /**
     * {@code DELETE  /set-lists/:id} : delete the "id" setList.
     *
     * @param id the id of the setListDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/set-lists/{id}")
    public ResponseEntity<Void> deleteSetList(@PathVariable Long id) {
        log.debug("REST request to delete SetList : {}", id);
        setListService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

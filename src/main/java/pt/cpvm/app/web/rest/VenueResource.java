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
import pt.cpvm.app.repository.VenueRepository;
import pt.cpvm.app.service.VenueService;
import pt.cpvm.app.service.dto.VenueDTO;
import pt.cpvm.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pt.cpvm.app.domain.Venue}.
 */
@RestController
@RequestMapping("/api")
public class VenueResource {

    private final Logger log = LoggerFactory.getLogger(VenueResource.class);

    private static final String ENTITY_NAME = "venue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VenueService venueService;

    private final VenueRepository venueRepository;

    public VenueResource(VenueService venueService, VenueRepository venueRepository) {
        this.venueService = venueService;
        this.venueRepository = venueRepository;
    }

    /**
     * {@code POST  /venues} : Create a new venue.
     *
     * @param venueDTO the venueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new venueDTO, or with status {@code 400 (Bad Request)} if the venue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/venues")
    public ResponseEntity<VenueDTO> createVenue(@Valid @RequestBody VenueDTO venueDTO) throws URISyntaxException {
        log.debug("REST request to save Venue : {}", venueDTO);
        if (venueDTO.getId() != null) {
            throw new BadRequestAlertException("A new venue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VenueDTO result = venueService.save(venueDTO);
        return ResponseEntity
            .created(new URI("/api/venues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /venues/:id} : Updates an existing venue.
     *
     * @param id the id of the venueDTO to save.
     * @param venueDTO the venueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated venueDTO,
     * or with status {@code 400 (Bad Request)} if the venueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the venueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/venues/{id}")
    public ResponseEntity<VenueDTO> updateVenue(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody VenueDTO venueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Venue : {}, {}", id, venueDTO);
        if (venueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, venueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!venueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VenueDTO result = venueService.update(venueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, venueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /venues/:id} : Partial updates given fields of an existing venue, field will ignore if it is null
     *
     * @param id the id of the venueDTO to save.
     * @param venueDTO the venueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated venueDTO,
     * or with status {@code 400 (Bad Request)} if the venueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the venueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the venueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/venues/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VenueDTO> partialUpdateVenue(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody VenueDTO venueDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Venue partially : {}, {}", id, venueDTO);
        if (venueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, venueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!venueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VenueDTO> result = venueService.partialUpdate(venueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, venueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /venues} : get all the venues.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of venues in body.
     */
    @GetMapping("/venues")
    public ResponseEntity<List<VenueDTO>> getAllVenues(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Venues");
        Page<VenueDTO> page = venueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /venues/:id} : get the "id" venue.
     *
     * @param id the id of the venueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the venueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/venues/{id}")
    public ResponseEntity<VenueDTO> getVenue(@PathVariable Long id) {
        log.debug("REST request to get Venue : {}", id);
        Optional<VenueDTO> venueDTO = venueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(venueDTO);
    }

    /**
     * {@code DELETE  /venues/:id} : delete the "id" venue.
     *
     * @param id the id of the venueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/venues/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        log.debug("REST request to delete Venue : {}", id);
        venueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.cpvm.app.repository.MemberAvailabilityRepository;
import pt.cpvm.app.service.MemberAvailabilityService;
import pt.cpvm.app.service.dto.MemberAvailabilityDTO;
import pt.cpvm.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link pt.cpvm.app.domain.MemberAvailability}.
 */
@RestController
@RequestMapping("/api")
public class MemberAvailabilityResource {

    private final Logger log = LoggerFactory.getLogger(MemberAvailabilityResource.class);

    private static final String ENTITY_NAME = "memberAvailability";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MemberAvailabilityService memberAvailabilityService;

    private final MemberAvailabilityRepository memberAvailabilityRepository;

    public MemberAvailabilityResource(
        MemberAvailabilityService memberAvailabilityService,
        MemberAvailabilityRepository memberAvailabilityRepository
    ) {
        this.memberAvailabilityService = memberAvailabilityService;
        this.memberAvailabilityRepository = memberAvailabilityRepository;
    }

    /**
     * {@code POST  /member-availabilities} : Create a new memberAvailability.
     *
     * @param memberAvailabilityDTO the memberAvailabilityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new memberAvailabilityDTO, or with status {@code 400 (Bad Request)} if the memberAvailability has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/member-availabilities")
    public ResponseEntity<MemberAvailabilityDTO> createMemberAvailability(@Valid @RequestBody MemberAvailabilityDTO memberAvailabilityDTO)
        throws URISyntaxException {
        log.debug("REST request to save MemberAvailability : {}", memberAvailabilityDTO);
        if (memberAvailabilityDTO.getId() != null) {
            throw new BadRequestAlertException("A new memberAvailability cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MemberAvailabilityDTO result = memberAvailabilityService.save(memberAvailabilityDTO);
        return ResponseEntity
            .created(new URI("/api/member-availabilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /member-availabilities/:id} : Updates an existing memberAvailability.
     *
     * @param id the id of the memberAvailabilityDTO to save.
     * @param memberAvailabilityDTO the memberAvailabilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memberAvailabilityDTO,
     * or with status {@code 400 (Bad Request)} if the memberAvailabilityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the memberAvailabilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/member-availabilities/{id}")
    public ResponseEntity<MemberAvailabilityDTO> updateMemberAvailability(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MemberAvailabilityDTO memberAvailabilityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MemberAvailability : {}, {}", id, memberAvailabilityDTO);
        if (memberAvailabilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, memberAvailabilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!memberAvailabilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MemberAvailabilityDTO result = memberAvailabilityService.update(memberAvailabilityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memberAvailabilityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /member-availabilities/:id} : Partial updates given fields of an existing memberAvailability, field will ignore if it is null
     *
     * @param id the id of the memberAvailabilityDTO to save.
     * @param memberAvailabilityDTO the memberAvailabilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memberAvailabilityDTO,
     * or with status {@code 400 (Bad Request)} if the memberAvailabilityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the memberAvailabilityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the memberAvailabilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/member-availabilities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MemberAvailabilityDTO> partialUpdateMemberAvailability(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MemberAvailabilityDTO memberAvailabilityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MemberAvailability partially : {}, {}", id, memberAvailabilityDTO);
        if (memberAvailabilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, memberAvailabilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!memberAvailabilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MemberAvailabilityDTO> result = memberAvailabilityService.partialUpdate(memberAvailabilityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memberAvailabilityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /member-availabilities} : get all the memberAvailabilities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of memberAvailabilities in body.
     */
    @GetMapping("/member-availabilities")
    public List<MemberAvailabilityDTO> getAllMemberAvailabilities() {
        log.debug("REST request to get all MemberAvailabilities");
        return memberAvailabilityService.findAll();
    }

    /**
     * {@code GET  /member-availabilities/:id} : get the "id" memberAvailability.
     *
     * @param id the id of the memberAvailabilityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the memberAvailabilityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/member-availabilities/{id}")
    public ResponseEntity<MemberAvailabilityDTO> getMemberAvailability(@PathVariable Long id) {
        log.debug("REST request to get MemberAvailability : {}", id);
        Optional<MemberAvailabilityDTO> memberAvailabilityDTO = memberAvailabilityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(memberAvailabilityDTO);
    }

    /**
     * {@code DELETE  /member-availabilities/:id} : delete the "id" memberAvailability.
     *
     * @param id the id of the memberAvailabilityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/member-availabilities/{id}")
    public ResponseEntity<Void> deleteMemberAvailability(@PathVariable Long id) {
        log.debug("REST request to delete MemberAvailability : {}", id);
        memberAvailabilityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

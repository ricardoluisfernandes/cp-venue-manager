package pt.cpvm.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pt.cpvm.app.IntegrationTest;
import pt.cpvm.app.domain.MemberAvailability;
import pt.cpvm.app.domain.enumeration.Availability;
import pt.cpvm.app.repository.MemberAvailabilityRepository;
import pt.cpvm.app.service.dto.MemberAvailabilityDTO;
import pt.cpvm.app.service.mapper.MemberAvailabilityMapper;

/**
 * Integration tests for the {@link MemberAvailabilityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MemberAvailabilityResourceIT {

    private static final Availability DEFAULT_AVAILABILITY = Availability.AVAILABLE;
    private static final Availability UPDATED_AVAILABILITY = Availability.UNAVAILABLE;

    private static final String ENTITY_API_URL = "/api/member-availabilities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MemberAvailabilityRepository memberAvailabilityRepository;

    @Autowired
    private MemberAvailabilityMapper memberAvailabilityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMemberAvailabilityMockMvc;

    private MemberAvailability memberAvailability;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemberAvailability createEntity(EntityManager em) {
        MemberAvailability memberAvailability = new MemberAvailability().availability(DEFAULT_AVAILABILITY);
        return memberAvailability;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MemberAvailability createUpdatedEntity(EntityManager em) {
        MemberAvailability memberAvailability = new MemberAvailability().availability(UPDATED_AVAILABILITY);
        return memberAvailability;
    }

    @BeforeEach
    public void initTest() {
        memberAvailability = createEntity(em);
    }

    @Test
    @Transactional
    void createMemberAvailability() throws Exception {
        int databaseSizeBeforeCreate = memberAvailabilityRepository.findAll().size();
        // Create the MemberAvailability
        MemberAvailabilityDTO memberAvailabilityDTO = memberAvailabilityMapper.toDto(memberAvailability);
        restMemberAvailabilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memberAvailabilityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the MemberAvailability in the database
        List<MemberAvailability> memberAvailabilityList = memberAvailabilityRepository.findAll();
        assertThat(memberAvailabilityList).hasSize(databaseSizeBeforeCreate + 1);
        MemberAvailability testMemberAvailability = memberAvailabilityList.get(memberAvailabilityList.size() - 1);
        assertThat(testMemberAvailability.getAvailability()).isEqualTo(DEFAULT_AVAILABILITY);
    }

    @Test
    @Transactional
    void createMemberAvailabilityWithExistingId() throws Exception {
        // Create the MemberAvailability with an existing ID
        memberAvailability.setId(1L);
        MemberAvailabilityDTO memberAvailabilityDTO = memberAvailabilityMapper.toDto(memberAvailability);

        int databaseSizeBeforeCreate = memberAvailabilityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberAvailabilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memberAvailabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberAvailability in the database
        List<MemberAvailability> memberAvailabilityList = memberAvailabilityRepository.findAll();
        assertThat(memberAvailabilityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAvailabilityIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberAvailabilityRepository.findAll().size();
        // set the field null
        memberAvailability.setAvailability(null);

        // Create the MemberAvailability, which fails.
        MemberAvailabilityDTO memberAvailabilityDTO = memberAvailabilityMapper.toDto(memberAvailability);

        restMemberAvailabilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memberAvailabilityDTO))
            )
            .andExpect(status().isBadRequest());

        List<MemberAvailability> memberAvailabilityList = memberAvailabilityRepository.findAll();
        assertThat(memberAvailabilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMemberAvailabilities() throws Exception {
        // Initialize the database
        memberAvailabilityRepository.saveAndFlush(memberAvailability);

        // Get all the memberAvailabilityList
        restMemberAvailabilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(memberAvailability.getId().intValue())))
            .andExpect(jsonPath("$.[*].availability").value(hasItem(DEFAULT_AVAILABILITY.toString())));
    }

    @Test
    @Transactional
    void getMemberAvailability() throws Exception {
        // Initialize the database
        memberAvailabilityRepository.saveAndFlush(memberAvailability);

        // Get the memberAvailability
        restMemberAvailabilityMockMvc
            .perform(get(ENTITY_API_URL_ID, memberAvailability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(memberAvailability.getId().intValue()))
            .andExpect(jsonPath("$.availability").value(DEFAULT_AVAILABILITY.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMemberAvailability() throws Exception {
        // Get the memberAvailability
        restMemberAvailabilityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMemberAvailability() throws Exception {
        // Initialize the database
        memberAvailabilityRepository.saveAndFlush(memberAvailability);

        int databaseSizeBeforeUpdate = memberAvailabilityRepository.findAll().size();

        // Update the memberAvailability
        MemberAvailability updatedMemberAvailability = memberAvailabilityRepository.findById(memberAvailability.getId()).get();
        // Disconnect from session so that the updates on updatedMemberAvailability are not directly saved in db
        em.detach(updatedMemberAvailability);
        updatedMemberAvailability.availability(UPDATED_AVAILABILITY);
        MemberAvailabilityDTO memberAvailabilityDTO = memberAvailabilityMapper.toDto(updatedMemberAvailability);

        restMemberAvailabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, memberAvailabilityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memberAvailabilityDTO))
            )
            .andExpect(status().isOk());

        // Validate the MemberAvailability in the database
        List<MemberAvailability> memberAvailabilityList = memberAvailabilityRepository.findAll();
        assertThat(memberAvailabilityList).hasSize(databaseSizeBeforeUpdate);
        MemberAvailability testMemberAvailability = memberAvailabilityList.get(memberAvailabilityList.size() - 1);
        assertThat(testMemberAvailability.getAvailability()).isEqualTo(UPDATED_AVAILABILITY);
    }

    @Test
    @Transactional
    void putNonExistingMemberAvailability() throws Exception {
        int databaseSizeBeforeUpdate = memberAvailabilityRepository.findAll().size();
        memberAvailability.setId(count.incrementAndGet());

        // Create the MemberAvailability
        MemberAvailabilityDTO memberAvailabilityDTO = memberAvailabilityMapper.toDto(memberAvailability);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberAvailabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, memberAvailabilityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memberAvailabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberAvailability in the database
        List<MemberAvailability> memberAvailabilityList = memberAvailabilityRepository.findAll();
        assertThat(memberAvailabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMemberAvailability() throws Exception {
        int databaseSizeBeforeUpdate = memberAvailabilityRepository.findAll().size();
        memberAvailability.setId(count.incrementAndGet());

        // Create the MemberAvailability
        MemberAvailabilityDTO memberAvailabilityDTO = memberAvailabilityMapper.toDto(memberAvailability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberAvailabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memberAvailabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberAvailability in the database
        List<MemberAvailability> memberAvailabilityList = memberAvailabilityRepository.findAll();
        assertThat(memberAvailabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMemberAvailability() throws Exception {
        int databaseSizeBeforeUpdate = memberAvailabilityRepository.findAll().size();
        memberAvailability.setId(count.incrementAndGet());

        // Create the MemberAvailability
        MemberAvailabilityDTO memberAvailabilityDTO = memberAvailabilityMapper.toDto(memberAvailability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberAvailabilityMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(memberAvailabilityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MemberAvailability in the database
        List<MemberAvailability> memberAvailabilityList = memberAvailabilityRepository.findAll();
        assertThat(memberAvailabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMemberAvailabilityWithPatch() throws Exception {
        // Initialize the database
        memberAvailabilityRepository.saveAndFlush(memberAvailability);

        int databaseSizeBeforeUpdate = memberAvailabilityRepository.findAll().size();

        // Update the memberAvailability using partial update
        MemberAvailability partialUpdatedMemberAvailability = new MemberAvailability();
        partialUpdatedMemberAvailability.setId(memberAvailability.getId());

        restMemberAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMemberAvailability.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMemberAvailability))
            )
            .andExpect(status().isOk());

        // Validate the MemberAvailability in the database
        List<MemberAvailability> memberAvailabilityList = memberAvailabilityRepository.findAll();
        assertThat(memberAvailabilityList).hasSize(databaseSizeBeforeUpdate);
        MemberAvailability testMemberAvailability = memberAvailabilityList.get(memberAvailabilityList.size() - 1);
        assertThat(testMemberAvailability.getAvailability()).isEqualTo(DEFAULT_AVAILABILITY);
    }

    @Test
    @Transactional
    void fullUpdateMemberAvailabilityWithPatch() throws Exception {
        // Initialize the database
        memberAvailabilityRepository.saveAndFlush(memberAvailability);

        int databaseSizeBeforeUpdate = memberAvailabilityRepository.findAll().size();

        // Update the memberAvailability using partial update
        MemberAvailability partialUpdatedMemberAvailability = new MemberAvailability();
        partialUpdatedMemberAvailability.setId(memberAvailability.getId());

        partialUpdatedMemberAvailability.availability(UPDATED_AVAILABILITY);

        restMemberAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMemberAvailability.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMemberAvailability))
            )
            .andExpect(status().isOk());

        // Validate the MemberAvailability in the database
        List<MemberAvailability> memberAvailabilityList = memberAvailabilityRepository.findAll();
        assertThat(memberAvailabilityList).hasSize(databaseSizeBeforeUpdate);
        MemberAvailability testMemberAvailability = memberAvailabilityList.get(memberAvailabilityList.size() - 1);
        assertThat(testMemberAvailability.getAvailability()).isEqualTo(UPDATED_AVAILABILITY);
    }

    @Test
    @Transactional
    void patchNonExistingMemberAvailability() throws Exception {
        int databaseSizeBeforeUpdate = memberAvailabilityRepository.findAll().size();
        memberAvailability.setId(count.incrementAndGet());

        // Create the MemberAvailability
        MemberAvailabilityDTO memberAvailabilityDTO = memberAvailabilityMapper.toDto(memberAvailability);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, memberAvailabilityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(memberAvailabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberAvailability in the database
        List<MemberAvailability> memberAvailabilityList = memberAvailabilityRepository.findAll();
        assertThat(memberAvailabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMemberAvailability() throws Exception {
        int databaseSizeBeforeUpdate = memberAvailabilityRepository.findAll().size();
        memberAvailability.setId(count.incrementAndGet());

        // Create the MemberAvailability
        MemberAvailabilityDTO memberAvailabilityDTO = memberAvailabilityMapper.toDto(memberAvailability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(memberAvailabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MemberAvailability in the database
        List<MemberAvailability> memberAvailabilityList = memberAvailabilityRepository.findAll();
        assertThat(memberAvailabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMemberAvailability() throws Exception {
        int databaseSizeBeforeUpdate = memberAvailabilityRepository.findAll().size();
        memberAvailability.setId(count.incrementAndGet());

        // Create the MemberAvailability
        MemberAvailabilityDTO memberAvailabilityDTO = memberAvailabilityMapper.toDto(memberAvailability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberAvailabilityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(memberAvailabilityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MemberAvailability in the database
        List<MemberAvailability> memberAvailabilityList = memberAvailabilityRepository.findAll();
        assertThat(memberAvailabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMemberAvailability() throws Exception {
        // Initialize the database
        memberAvailabilityRepository.saveAndFlush(memberAvailability);

        int databaseSizeBeforeDelete = memberAvailabilityRepository.findAll().size();

        // Delete the memberAvailability
        restMemberAvailabilityMockMvc
            .perform(delete(ENTITY_API_URL_ID, memberAvailability.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MemberAvailability> memberAvailabilityList = memberAvailabilityRepository.findAll();
        assertThat(memberAvailabilityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

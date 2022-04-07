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
import pt.cpvm.app.domain.Contractor;
import pt.cpvm.app.repository.ContractorRepository;
import pt.cpvm.app.service.dto.ContractorDTO;
import pt.cpvm.app.service.mapper.ContractorMapper;

/**
 * Integration tests for the {@link ContractorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContractorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/contractors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContractorRepository contractorRepository;

    @Autowired
    private ContractorMapper contractorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractorMockMvc;

    private Contractor contractor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contractor createEntity(EntityManager em) {
        Contractor contractor = new Contractor()
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .comments(DEFAULT_COMMENTS);
        return contractor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contractor createUpdatedEntity(EntityManager em) {
        Contractor contractor = new Contractor()
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .comments(UPDATED_COMMENTS);
        return contractor;
    }

    @BeforeEach
    public void initTest() {
        contractor = createEntity(em);
    }

    @Test
    @Transactional
    void createContractor() throws Exception {
        int databaseSizeBeforeCreate = contractorRepository.findAll().size();
        // Create the Contractor
        ContractorDTO contractorDTO = contractorMapper.toDto(contractor);
        restContractorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractorDTO)))
            .andExpect(status().isCreated());

        // Validate the Contractor in the database
        List<Contractor> contractorList = contractorRepository.findAll();
        assertThat(contractorList).hasSize(databaseSizeBeforeCreate + 1);
        Contractor testContractor = contractorList.get(contractorList.size() - 1);
        assertThat(testContractor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContractor.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testContractor.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testContractor.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    void createContractorWithExistingId() throws Exception {
        // Create the Contractor with an existing ID
        contractor.setId(1L);
        ContractorDTO contractorDTO = contractorMapper.toDto(contractor);

        int databaseSizeBeforeCreate = contractorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contractor in the database
        List<Contractor> contractorList = contractorRepository.findAll();
        assertThat(contractorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = contractorRepository.findAll().size();
        // set the field null
        contractor.setName(null);

        // Create the Contractor, which fails.
        ContractorDTO contractorDTO = contractorMapper.toDto(contractor);

        restContractorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractorDTO)))
            .andExpect(status().isBadRequest());

        List<Contractor> contractorList = contractorRepository.findAll();
        assertThat(contractorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContractors() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get all the contractorList
        restContractorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }

    @Test
    @Transactional
    void getContractor() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        // Get the contractor
        restContractorMockMvc
            .perform(get(ENTITY_API_URL_ID, contractor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contractor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }

    @Test
    @Transactional
    void getNonExistingContractor() throws Exception {
        // Get the contractor
        restContractorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewContractor() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        int databaseSizeBeforeUpdate = contractorRepository.findAll().size();

        // Update the contractor
        Contractor updatedContractor = contractorRepository.findById(contractor.getId()).get();
        // Disconnect from session so that the updates on updatedContractor are not directly saved in db
        em.detach(updatedContractor);
        updatedContractor.name(UPDATED_NAME).email(UPDATED_EMAIL).phoneNumber(UPDATED_PHONE_NUMBER).comments(UPDATED_COMMENTS);
        ContractorDTO contractorDTO = contractorMapper.toDto(updatedContractor);

        restContractorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Contractor in the database
        List<Contractor> contractorList = contractorRepository.findAll();
        assertThat(contractorList).hasSize(databaseSizeBeforeUpdate);
        Contractor testContractor = contractorList.get(contractorList.size() - 1);
        assertThat(testContractor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContractor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContractor.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testContractor.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void putNonExistingContractor() throws Exception {
        int databaseSizeBeforeUpdate = contractorRepository.findAll().size();
        contractor.setId(count.incrementAndGet());

        // Create the Contractor
        ContractorDTO contractorDTO = contractorMapper.toDto(contractor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contractor in the database
        List<Contractor> contractorList = contractorRepository.findAll();
        assertThat(contractorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContractor() throws Exception {
        int databaseSizeBeforeUpdate = contractorRepository.findAll().size();
        contractor.setId(count.incrementAndGet());

        // Create the Contractor
        ContractorDTO contractorDTO = contractorMapper.toDto(contractor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contractorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contractor in the database
        List<Contractor> contractorList = contractorRepository.findAll();
        assertThat(contractorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContractor() throws Exception {
        int databaseSizeBeforeUpdate = contractorRepository.findAll().size();
        contractor.setId(count.incrementAndGet());

        // Create the Contractor
        ContractorDTO contractorDTO = contractorMapper.toDto(contractor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(contractorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contractor in the database
        List<Contractor> contractorList = contractorRepository.findAll();
        assertThat(contractorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContractorWithPatch() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        int databaseSizeBeforeUpdate = contractorRepository.findAll().size();

        // Update the contractor using partial update
        Contractor partialUpdatedContractor = new Contractor();
        partialUpdatedContractor.setId(contractor.getId());

        partialUpdatedContractor.email(UPDATED_EMAIL);

        restContractorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContractor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContractor))
            )
            .andExpect(status().isOk());

        // Validate the Contractor in the database
        List<Contractor> contractorList = contractorRepository.findAll();
        assertThat(contractorList).hasSize(databaseSizeBeforeUpdate);
        Contractor testContractor = contractorList.get(contractorList.size() - 1);
        assertThat(testContractor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testContractor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContractor.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testContractor.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    void fullUpdateContractorWithPatch() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        int databaseSizeBeforeUpdate = contractorRepository.findAll().size();

        // Update the contractor using partial update
        Contractor partialUpdatedContractor = new Contractor();
        partialUpdatedContractor.setId(contractor.getId());

        partialUpdatedContractor.name(UPDATED_NAME).email(UPDATED_EMAIL).phoneNumber(UPDATED_PHONE_NUMBER).comments(UPDATED_COMMENTS);

        restContractorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContractor.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContractor))
            )
            .andExpect(status().isOk());

        // Validate the Contractor in the database
        List<Contractor> contractorList = contractorRepository.findAll();
        assertThat(contractorList).hasSize(databaseSizeBeforeUpdate);
        Contractor testContractor = contractorList.get(contractorList.size() - 1);
        assertThat(testContractor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testContractor.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testContractor.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testContractor.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void patchNonExistingContractor() throws Exception {
        int databaseSizeBeforeUpdate = contractorRepository.findAll().size();
        contractor.setId(count.incrementAndGet());

        // Create the Contractor
        ContractorDTO contractorDTO = contractorMapper.toDto(contractor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contractorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contractorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contractor in the database
        List<Contractor> contractorList = contractorRepository.findAll();
        assertThat(contractorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContractor() throws Exception {
        int databaseSizeBeforeUpdate = contractorRepository.findAll().size();
        contractor.setId(count.incrementAndGet());

        // Create the Contractor
        ContractorDTO contractorDTO = contractorMapper.toDto(contractor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contractorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contractor in the database
        List<Contractor> contractorList = contractorRepository.findAll();
        assertThat(contractorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContractor() throws Exception {
        int databaseSizeBeforeUpdate = contractorRepository.findAll().size();
        contractor.setId(count.incrementAndGet());

        // Create the Contractor
        ContractorDTO contractorDTO = contractorMapper.toDto(contractor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(contractorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contractor in the database
        List<Contractor> contractorList = contractorRepository.findAll();
        assertThat(contractorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContractor() throws Exception {
        // Initialize the database
        contractorRepository.saveAndFlush(contractor);

        int databaseSizeBeforeDelete = contractorRepository.findAll().size();

        // Delete the contractor
        restContractorMockMvc
            .perform(delete(ENTITY_API_URL_ID, contractor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contractor> contractorList = contractorRepository.findAll();
        assertThat(contractorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

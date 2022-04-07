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
import pt.cpvm.app.domain.SetListLine;
import pt.cpvm.app.repository.SetListLineRepository;
import pt.cpvm.app.service.dto.SetListLineDTO;
import pt.cpvm.app.service.mapper.SetListLineMapper;

/**
 * Integration tests for the {@link SetListLineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SetListLineResourceIT {

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    private static final String ENTITY_API_URL = "/api/set-list-lines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SetListLineRepository setListLineRepository;

    @Autowired
    private SetListLineMapper setListLineMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSetListLineMockMvc;

    private SetListLine setListLine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SetListLine createEntity(EntityManager em) {
        SetListLine setListLine = new SetListLine().order(DEFAULT_ORDER);
        return setListLine;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SetListLine createUpdatedEntity(EntityManager em) {
        SetListLine setListLine = new SetListLine().order(UPDATED_ORDER);
        return setListLine;
    }

    @BeforeEach
    public void initTest() {
        setListLine = createEntity(em);
    }

    @Test
    @Transactional
    void createSetListLine() throws Exception {
        int databaseSizeBeforeCreate = setListLineRepository.findAll().size();
        // Create the SetListLine
        SetListLineDTO setListLineDTO = setListLineMapper.toDto(setListLine);
        restSetListLineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(setListLineDTO))
            )
            .andExpect(status().isCreated());

        // Validate the SetListLine in the database
        List<SetListLine> setListLineList = setListLineRepository.findAll();
        assertThat(setListLineList).hasSize(databaseSizeBeforeCreate + 1);
        SetListLine testSetListLine = setListLineList.get(setListLineList.size() - 1);
        assertThat(testSetListLine.getOrder()).isEqualTo(DEFAULT_ORDER);
    }

    @Test
    @Transactional
    void createSetListLineWithExistingId() throws Exception {
        // Create the SetListLine with an existing ID
        setListLine.setId(1L);
        SetListLineDTO setListLineDTO = setListLineMapper.toDto(setListLine);

        int databaseSizeBeforeCreate = setListLineRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSetListLineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(setListLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SetListLine in the database
        List<SetListLine> setListLineList = setListLineRepository.findAll();
        assertThat(setListLineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = setListLineRepository.findAll().size();
        // set the field null
        setListLine.setOrder(null);

        // Create the SetListLine, which fails.
        SetListLineDTO setListLineDTO = setListLineMapper.toDto(setListLine);

        restSetListLineMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(setListLineDTO))
            )
            .andExpect(status().isBadRequest());

        List<SetListLine> setListLineList = setListLineRepository.findAll();
        assertThat(setListLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSetListLines() throws Exception {
        // Initialize the database
        setListLineRepository.saveAndFlush(setListLine);

        // Get all the setListLineList
        restSetListLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(setListLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)));
    }

    @Test
    @Transactional
    void getSetListLine() throws Exception {
        // Initialize the database
        setListLineRepository.saveAndFlush(setListLine);

        // Get the setListLine
        restSetListLineMockMvc
            .perform(get(ENTITY_API_URL_ID, setListLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(setListLine.getId().intValue()))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER));
    }

    @Test
    @Transactional
    void getNonExistingSetListLine() throws Exception {
        // Get the setListLine
        restSetListLineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSetListLine() throws Exception {
        // Initialize the database
        setListLineRepository.saveAndFlush(setListLine);

        int databaseSizeBeforeUpdate = setListLineRepository.findAll().size();

        // Update the setListLine
        SetListLine updatedSetListLine = setListLineRepository.findById(setListLine.getId()).get();
        // Disconnect from session so that the updates on updatedSetListLine are not directly saved in db
        em.detach(updatedSetListLine);
        updatedSetListLine.order(UPDATED_ORDER);
        SetListLineDTO setListLineDTO = setListLineMapper.toDto(updatedSetListLine);

        restSetListLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, setListLineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(setListLineDTO))
            )
            .andExpect(status().isOk());

        // Validate the SetListLine in the database
        List<SetListLine> setListLineList = setListLineRepository.findAll();
        assertThat(setListLineList).hasSize(databaseSizeBeforeUpdate);
        SetListLine testSetListLine = setListLineList.get(setListLineList.size() - 1);
        assertThat(testSetListLine.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    @Transactional
    void putNonExistingSetListLine() throws Exception {
        int databaseSizeBeforeUpdate = setListLineRepository.findAll().size();
        setListLine.setId(count.incrementAndGet());

        // Create the SetListLine
        SetListLineDTO setListLineDTO = setListLineMapper.toDto(setListLine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSetListLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, setListLineDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(setListLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SetListLine in the database
        List<SetListLine> setListLineList = setListLineRepository.findAll();
        assertThat(setListLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSetListLine() throws Exception {
        int databaseSizeBeforeUpdate = setListLineRepository.findAll().size();
        setListLine.setId(count.incrementAndGet());

        // Create the SetListLine
        SetListLineDTO setListLineDTO = setListLineMapper.toDto(setListLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSetListLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(setListLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SetListLine in the database
        List<SetListLine> setListLineList = setListLineRepository.findAll();
        assertThat(setListLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSetListLine() throws Exception {
        int databaseSizeBeforeUpdate = setListLineRepository.findAll().size();
        setListLine.setId(count.incrementAndGet());

        // Create the SetListLine
        SetListLineDTO setListLineDTO = setListLineMapper.toDto(setListLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSetListLineMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(setListLineDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SetListLine in the database
        List<SetListLine> setListLineList = setListLineRepository.findAll();
        assertThat(setListLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSetListLineWithPatch() throws Exception {
        // Initialize the database
        setListLineRepository.saveAndFlush(setListLine);

        int databaseSizeBeforeUpdate = setListLineRepository.findAll().size();

        // Update the setListLine using partial update
        SetListLine partialUpdatedSetListLine = new SetListLine();
        partialUpdatedSetListLine.setId(setListLine.getId());

        partialUpdatedSetListLine.order(UPDATED_ORDER);

        restSetListLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSetListLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSetListLine))
            )
            .andExpect(status().isOk());

        // Validate the SetListLine in the database
        List<SetListLine> setListLineList = setListLineRepository.findAll();
        assertThat(setListLineList).hasSize(databaseSizeBeforeUpdate);
        SetListLine testSetListLine = setListLineList.get(setListLineList.size() - 1);
        assertThat(testSetListLine.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    @Transactional
    void fullUpdateSetListLineWithPatch() throws Exception {
        // Initialize the database
        setListLineRepository.saveAndFlush(setListLine);

        int databaseSizeBeforeUpdate = setListLineRepository.findAll().size();

        // Update the setListLine using partial update
        SetListLine partialUpdatedSetListLine = new SetListLine();
        partialUpdatedSetListLine.setId(setListLine.getId());

        partialUpdatedSetListLine.order(UPDATED_ORDER);

        restSetListLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSetListLine.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSetListLine))
            )
            .andExpect(status().isOk());

        // Validate the SetListLine in the database
        List<SetListLine> setListLineList = setListLineRepository.findAll();
        assertThat(setListLineList).hasSize(databaseSizeBeforeUpdate);
        SetListLine testSetListLine = setListLineList.get(setListLineList.size() - 1);
        assertThat(testSetListLine.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    @Transactional
    void patchNonExistingSetListLine() throws Exception {
        int databaseSizeBeforeUpdate = setListLineRepository.findAll().size();
        setListLine.setId(count.incrementAndGet());

        // Create the SetListLine
        SetListLineDTO setListLineDTO = setListLineMapper.toDto(setListLine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSetListLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, setListLineDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(setListLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SetListLine in the database
        List<SetListLine> setListLineList = setListLineRepository.findAll();
        assertThat(setListLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSetListLine() throws Exception {
        int databaseSizeBeforeUpdate = setListLineRepository.findAll().size();
        setListLine.setId(count.incrementAndGet());

        // Create the SetListLine
        SetListLineDTO setListLineDTO = setListLineMapper.toDto(setListLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSetListLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(setListLineDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SetListLine in the database
        List<SetListLine> setListLineList = setListLineRepository.findAll();
        assertThat(setListLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSetListLine() throws Exception {
        int databaseSizeBeforeUpdate = setListLineRepository.findAll().size();
        setListLine.setId(count.incrementAndGet());

        // Create the SetListLine
        SetListLineDTO setListLineDTO = setListLineMapper.toDto(setListLine);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSetListLineMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(setListLineDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SetListLine in the database
        List<SetListLine> setListLineList = setListLineRepository.findAll();
        assertThat(setListLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSetListLine() throws Exception {
        // Initialize the database
        setListLineRepository.saveAndFlush(setListLine);

        int databaseSizeBeforeDelete = setListLineRepository.findAll().size();

        // Delete the setListLine
        restSetListLineMockMvc
            .perform(delete(ENTITY_API_URL_ID, setListLine.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SetListLine> setListLineList = setListLineRepository.findAll();
        assertThat(setListLineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

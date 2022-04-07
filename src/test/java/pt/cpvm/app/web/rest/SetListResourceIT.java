package pt.cpvm.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Duration;
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
import pt.cpvm.app.domain.SetList;
import pt.cpvm.app.repository.SetListRepository;
import pt.cpvm.app.service.dto.SetListDTO;
import pt.cpvm.app.service.mapper.SetListMapper;

/**
 * Integration tests for the {@link SetListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SetListResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Duration DEFAULT_TOTAL_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_TOTAL_DURATION = Duration.ofHours(12);

    private static final String ENTITY_API_URL = "/api/set-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SetListRepository setListRepository;

    @Autowired
    private SetListMapper setListMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSetListMockMvc;

    private SetList setList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SetList createEntity(EntityManager em) {
        SetList setList = new SetList().description(DEFAULT_DESCRIPTION).totalDuration(DEFAULT_TOTAL_DURATION);
        return setList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SetList createUpdatedEntity(EntityManager em) {
        SetList setList = new SetList().description(UPDATED_DESCRIPTION).totalDuration(UPDATED_TOTAL_DURATION);
        return setList;
    }

    @BeforeEach
    public void initTest() {
        setList = createEntity(em);
    }

    @Test
    @Transactional
    void createSetList() throws Exception {
        int databaseSizeBeforeCreate = setListRepository.findAll().size();
        // Create the SetList
        SetListDTO setListDTO = setListMapper.toDto(setList);
        restSetListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(setListDTO)))
            .andExpect(status().isCreated());

        // Validate the SetList in the database
        List<SetList> setListList = setListRepository.findAll();
        assertThat(setListList).hasSize(databaseSizeBeforeCreate + 1);
        SetList testSetList = setListList.get(setListList.size() - 1);
        assertThat(testSetList.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSetList.getTotalDuration()).isEqualTo(DEFAULT_TOTAL_DURATION);
    }

    @Test
    @Transactional
    void createSetListWithExistingId() throws Exception {
        // Create the SetList with an existing ID
        setList.setId(1L);
        SetListDTO setListDTO = setListMapper.toDto(setList);

        int databaseSizeBeforeCreate = setListRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSetListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(setListDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SetList in the database
        List<SetList> setListList = setListRepository.findAll();
        assertThat(setListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = setListRepository.findAll().size();
        // set the field null
        setList.setDescription(null);

        // Create the SetList, which fails.
        SetListDTO setListDTO = setListMapper.toDto(setList);

        restSetListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(setListDTO)))
            .andExpect(status().isBadRequest());

        List<SetList> setListList = setListRepository.findAll();
        assertThat(setListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSetLists() throws Exception {
        // Initialize the database
        setListRepository.saveAndFlush(setList);

        // Get all the setListList
        restSetListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(setList.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].totalDuration").value(hasItem(DEFAULT_TOTAL_DURATION.toString())));
    }

    @Test
    @Transactional
    void getSetList() throws Exception {
        // Initialize the database
        setListRepository.saveAndFlush(setList);

        // Get the setList
        restSetListMockMvc
            .perform(get(ENTITY_API_URL_ID, setList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(setList.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.totalDuration").value(DEFAULT_TOTAL_DURATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSetList() throws Exception {
        // Get the setList
        restSetListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSetList() throws Exception {
        // Initialize the database
        setListRepository.saveAndFlush(setList);

        int databaseSizeBeforeUpdate = setListRepository.findAll().size();

        // Update the setList
        SetList updatedSetList = setListRepository.findById(setList.getId()).get();
        // Disconnect from session so that the updates on updatedSetList are not directly saved in db
        em.detach(updatedSetList);
        updatedSetList.description(UPDATED_DESCRIPTION).totalDuration(UPDATED_TOTAL_DURATION);
        SetListDTO setListDTO = setListMapper.toDto(updatedSetList);

        restSetListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, setListDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(setListDTO))
            )
            .andExpect(status().isOk());

        // Validate the SetList in the database
        List<SetList> setListList = setListRepository.findAll();
        assertThat(setListList).hasSize(databaseSizeBeforeUpdate);
        SetList testSetList = setListList.get(setListList.size() - 1);
        assertThat(testSetList.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSetList.getTotalDuration()).isEqualTo(UPDATED_TOTAL_DURATION);
    }

    @Test
    @Transactional
    void putNonExistingSetList() throws Exception {
        int databaseSizeBeforeUpdate = setListRepository.findAll().size();
        setList.setId(count.incrementAndGet());

        // Create the SetList
        SetListDTO setListDTO = setListMapper.toDto(setList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSetListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, setListDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(setListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SetList in the database
        List<SetList> setListList = setListRepository.findAll();
        assertThat(setListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSetList() throws Exception {
        int databaseSizeBeforeUpdate = setListRepository.findAll().size();
        setList.setId(count.incrementAndGet());

        // Create the SetList
        SetListDTO setListDTO = setListMapper.toDto(setList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSetListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(setListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SetList in the database
        List<SetList> setListList = setListRepository.findAll();
        assertThat(setListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSetList() throws Exception {
        int databaseSizeBeforeUpdate = setListRepository.findAll().size();
        setList.setId(count.incrementAndGet());

        // Create the SetList
        SetListDTO setListDTO = setListMapper.toDto(setList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSetListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(setListDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SetList in the database
        List<SetList> setListList = setListRepository.findAll();
        assertThat(setListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSetListWithPatch() throws Exception {
        // Initialize the database
        setListRepository.saveAndFlush(setList);

        int databaseSizeBeforeUpdate = setListRepository.findAll().size();

        // Update the setList using partial update
        SetList partialUpdatedSetList = new SetList();
        partialUpdatedSetList.setId(setList.getId());

        partialUpdatedSetList.totalDuration(UPDATED_TOTAL_DURATION);

        restSetListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSetList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSetList))
            )
            .andExpect(status().isOk());

        // Validate the SetList in the database
        List<SetList> setListList = setListRepository.findAll();
        assertThat(setListList).hasSize(databaseSizeBeforeUpdate);
        SetList testSetList = setListList.get(setListList.size() - 1);
        assertThat(testSetList.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSetList.getTotalDuration()).isEqualTo(UPDATED_TOTAL_DURATION);
    }

    @Test
    @Transactional
    void fullUpdateSetListWithPatch() throws Exception {
        // Initialize the database
        setListRepository.saveAndFlush(setList);

        int databaseSizeBeforeUpdate = setListRepository.findAll().size();

        // Update the setList using partial update
        SetList partialUpdatedSetList = new SetList();
        partialUpdatedSetList.setId(setList.getId());

        partialUpdatedSetList.description(UPDATED_DESCRIPTION).totalDuration(UPDATED_TOTAL_DURATION);

        restSetListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSetList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSetList))
            )
            .andExpect(status().isOk());

        // Validate the SetList in the database
        List<SetList> setListList = setListRepository.findAll();
        assertThat(setListList).hasSize(databaseSizeBeforeUpdate);
        SetList testSetList = setListList.get(setListList.size() - 1);
        assertThat(testSetList.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSetList.getTotalDuration()).isEqualTo(UPDATED_TOTAL_DURATION);
    }

    @Test
    @Transactional
    void patchNonExistingSetList() throws Exception {
        int databaseSizeBeforeUpdate = setListRepository.findAll().size();
        setList.setId(count.incrementAndGet());

        // Create the SetList
        SetListDTO setListDTO = setListMapper.toDto(setList);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSetListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, setListDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(setListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SetList in the database
        List<SetList> setListList = setListRepository.findAll();
        assertThat(setListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSetList() throws Exception {
        int databaseSizeBeforeUpdate = setListRepository.findAll().size();
        setList.setId(count.incrementAndGet());

        // Create the SetList
        SetListDTO setListDTO = setListMapper.toDto(setList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSetListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(setListDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SetList in the database
        List<SetList> setListList = setListRepository.findAll();
        assertThat(setListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSetList() throws Exception {
        int databaseSizeBeforeUpdate = setListRepository.findAll().size();
        setList.setId(count.incrementAndGet());

        // Create the SetList
        SetListDTO setListDTO = setListMapper.toDto(setList);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSetListMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(setListDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SetList in the database
        List<SetList> setListList = setListRepository.findAll();
        assertThat(setListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSetList() throws Exception {
        // Initialize the database
        setListRepository.saveAndFlush(setList);

        int databaseSizeBeforeDelete = setListRepository.findAll().size();

        // Delete the setList
        restSetListMockMvc
            .perform(delete(ENTITY_API_URL_ID, setList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SetList> setListList = setListRepository.findAll();
        assertThat(setListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

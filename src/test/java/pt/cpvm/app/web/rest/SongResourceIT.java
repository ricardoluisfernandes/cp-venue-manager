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
import pt.cpvm.app.domain.Song;
import pt.cpvm.app.repository.SongRepository;
import pt.cpvm.app.service.dto.SongDTO;
import pt.cpvm.app.service.mapper.SongMapper;

/**
 * Integration tests for the {@link SongResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SongResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Duration DEFAULT_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_DURATION = Duration.ofHours(12);

    private static final String DEFAULT_SCORE = "AAAAAAAAAA";
    private static final String UPDATED_SCORE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/songs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private SongMapper songMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSongMockMvc;

    private Song song;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Song createEntity(EntityManager em) {
        Song song = new Song().name(DEFAULT_NAME).duration(DEFAULT_DURATION).score(DEFAULT_SCORE);
        return song;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Song createUpdatedEntity(EntityManager em) {
        Song song = new Song().name(UPDATED_NAME).duration(UPDATED_DURATION).score(UPDATED_SCORE);
        return song;
    }

    @BeforeEach
    public void initTest() {
        song = createEntity(em);
    }

    @Test
    @Transactional
    void createSong() throws Exception {
        int databaseSizeBeforeCreate = songRepository.findAll().size();
        // Create the Song
        SongDTO songDTO = songMapper.toDto(song);
        restSongMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(songDTO)))
            .andExpect(status().isCreated());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeCreate + 1);
        Song testSong = songList.get(songList.size() - 1);
        assertThat(testSong.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSong.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testSong.getScore()).isEqualTo(DEFAULT_SCORE);
    }

    @Test
    @Transactional
    void createSongWithExistingId() throws Exception {
        // Create the Song with an existing ID
        song.setId(1L);
        SongDTO songDTO = songMapper.toDto(song);

        int databaseSizeBeforeCreate = songRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSongMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(songDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = songRepository.findAll().size();
        // set the field null
        song.setName(null);

        // Create the Song, which fails.
        SongDTO songDTO = songMapper.toDto(song);

        restSongMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(songDTO)))
            .andExpect(status().isBadRequest());

        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = songRepository.findAll().size();
        // set the field null
        song.setDuration(null);

        // Create the Song, which fails.
        SongDTO songDTO = songMapper.toDto(song);

        restSongMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(songDTO)))
            .andExpect(status().isBadRequest());

        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSongs() throws Exception {
        // Initialize the database
        songRepository.saveAndFlush(song);

        // Get all the songList
        restSongMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(song.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.toString())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)));
    }

    @Test
    @Transactional
    void getSong() throws Exception {
        // Initialize the database
        songRepository.saveAndFlush(song);

        // Get the song
        restSongMockMvc
            .perform(get(ENTITY_API_URL_ID, song.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(song.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.toString()))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE));
    }

    @Test
    @Transactional
    void getNonExistingSong() throws Exception {
        // Get the song
        restSongMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSong() throws Exception {
        // Initialize the database
        songRepository.saveAndFlush(song);

        int databaseSizeBeforeUpdate = songRepository.findAll().size();

        // Update the song
        Song updatedSong = songRepository.findById(song.getId()).get();
        // Disconnect from session so that the updates on updatedSong are not directly saved in db
        em.detach(updatedSong);
        updatedSong.name(UPDATED_NAME).duration(UPDATED_DURATION).score(UPDATED_SCORE);
        SongDTO songDTO = songMapper.toDto(updatedSong);

        restSongMockMvc
            .perform(
                put(ENTITY_API_URL_ID, songDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(songDTO))
            )
            .andExpect(status().isOk());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeUpdate);
        Song testSong = songList.get(songList.size() - 1);
        assertThat(testSong.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSong.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSong.getScore()).isEqualTo(UPDATED_SCORE);
    }

    @Test
    @Transactional
    void putNonExistingSong() throws Exception {
        int databaseSizeBeforeUpdate = songRepository.findAll().size();
        song.setId(count.incrementAndGet());

        // Create the Song
        SongDTO songDTO = songMapper.toDto(song);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSongMockMvc
            .perform(
                put(ENTITY_API_URL_ID, songDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(songDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSong() throws Exception {
        int databaseSizeBeforeUpdate = songRepository.findAll().size();
        song.setId(count.incrementAndGet());

        // Create the Song
        SongDTO songDTO = songMapper.toDto(song);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSongMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(songDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSong() throws Exception {
        int databaseSizeBeforeUpdate = songRepository.findAll().size();
        song.setId(count.incrementAndGet());

        // Create the Song
        SongDTO songDTO = songMapper.toDto(song);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSongMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(songDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSongWithPatch() throws Exception {
        // Initialize the database
        songRepository.saveAndFlush(song);

        int databaseSizeBeforeUpdate = songRepository.findAll().size();

        // Update the song using partial update
        Song partialUpdatedSong = new Song();
        partialUpdatedSong.setId(song.getId());

        partialUpdatedSong.name(UPDATED_NAME).duration(UPDATED_DURATION).score(UPDATED_SCORE);

        restSongMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSong.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSong))
            )
            .andExpect(status().isOk());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeUpdate);
        Song testSong = songList.get(songList.size() - 1);
        assertThat(testSong.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSong.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSong.getScore()).isEqualTo(UPDATED_SCORE);
    }

    @Test
    @Transactional
    void fullUpdateSongWithPatch() throws Exception {
        // Initialize the database
        songRepository.saveAndFlush(song);

        int databaseSizeBeforeUpdate = songRepository.findAll().size();

        // Update the song using partial update
        Song partialUpdatedSong = new Song();
        partialUpdatedSong.setId(song.getId());

        partialUpdatedSong.name(UPDATED_NAME).duration(UPDATED_DURATION).score(UPDATED_SCORE);

        restSongMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSong.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSong))
            )
            .andExpect(status().isOk());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeUpdate);
        Song testSong = songList.get(songList.size() - 1);
        assertThat(testSong.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSong.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSong.getScore()).isEqualTo(UPDATED_SCORE);
    }

    @Test
    @Transactional
    void patchNonExistingSong() throws Exception {
        int databaseSizeBeforeUpdate = songRepository.findAll().size();
        song.setId(count.incrementAndGet());

        // Create the Song
        SongDTO songDTO = songMapper.toDto(song);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSongMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, songDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(songDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSong() throws Exception {
        int databaseSizeBeforeUpdate = songRepository.findAll().size();
        song.setId(count.incrementAndGet());

        // Create the Song
        SongDTO songDTO = songMapper.toDto(song);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSongMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(songDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSong() throws Exception {
        int databaseSizeBeforeUpdate = songRepository.findAll().size();
        song.setId(count.incrementAndGet());

        // Create the Song
        SongDTO songDTO = songMapper.toDto(song);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSongMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(songDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Song in the database
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSong() throws Exception {
        // Initialize the database
        songRepository.saveAndFlush(song);

        int databaseSizeBeforeDelete = songRepository.findAll().size();

        // Delete the song
        restSongMockMvc
            .perform(delete(ENTITY_API_URL_ID, song.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Song> songList = songRepository.findAll();
        assertThat(songList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

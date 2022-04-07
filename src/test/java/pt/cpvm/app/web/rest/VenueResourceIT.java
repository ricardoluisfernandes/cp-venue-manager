package pt.cpvm.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pt.cpvm.app.web.rest.TestUtil.sameNumber;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import pt.cpvm.app.domain.Venue;
import pt.cpvm.app.domain.enumeration.Type;
import pt.cpvm.app.domain.enumeration.VenueStatus;
import pt.cpvm.app.repository.VenueRepository;
import pt.cpvm.app.service.dto.VenueDTO;
import pt.cpvm.app.service.mapper.VenueMapper;

/**
 * Integration tests for the {@link VenueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VenueResourceIT {

    private static final Instant DEFAULT_INSTANT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INSTANT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DISTANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISTANCE = new BigDecimal(2);

    private static final Type DEFAULT_TYPE = Type.CONCERT;
    private static final Type UPDATED_TYPE = Type.WEDDING;

    private static final VenueStatus DEFAULT_STATUS = VenueStatus.DRAFT;
    private static final VenueStatus UPDATED_STATUS = VenueStatus.PENDING_AVAILABILITY;

    private static final Duration DEFAULT_TOTAL_DURATION = Duration.ofHours(6);
    private static final Duration UPDATED_TOTAL_DURATION = Duration.ofHours(12);

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(0);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(1);

    private static final Boolean DEFAULT_CONSIDER_TRAVEL_EXPENSES = false;
    private static final Boolean UPDATED_CONSIDER_TRAVEL_EXPENSES = true;

    private static final BigDecimal DEFAULT_TRAVEL_EXPENSES_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TRAVEL_EXPENSES_VALUE = new BigDecimal(2);

    private static final Boolean DEFAULT_DO_VALUE_RETENTION = false;
    private static final Boolean UPDATED_DO_VALUE_RETENTION = true;

    private static final BigDecimal DEFAULT_RETENTION_PERCENTAGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_RETENTION_PERCENTAGE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_RETENTION_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_RETENTION_VALUE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_MEMBER_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_MEMBER_VALUE = new BigDecimal(2);

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/venues";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private VenueMapper venueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVenueMockMvc;

    private Venue venue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Venue createEntity(EntityManager em) {
        Venue venue = new Venue()
            .instant(DEFAULT_INSTANT)
            .location(DEFAULT_LOCATION)
            .distance(DEFAULT_DISTANCE)
            .type(DEFAULT_TYPE)
            .status(DEFAULT_STATUS)
            .totalDuration(DEFAULT_TOTAL_DURATION)
            .value(DEFAULT_VALUE)
            .considerTravelExpenses(DEFAULT_CONSIDER_TRAVEL_EXPENSES)
            .travelExpensesValue(DEFAULT_TRAVEL_EXPENSES_VALUE)
            .doValueRetention(DEFAULT_DO_VALUE_RETENTION)
            .retentionPercentage(DEFAULT_RETENTION_PERCENTAGE)
            .retentionValue(DEFAULT_RETENTION_VALUE)
            .memberValue(DEFAULT_MEMBER_VALUE)
            .comments(DEFAULT_COMMENTS);
        return venue;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Venue createUpdatedEntity(EntityManager em) {
        Venue venue = new Venue()
            .instant(UPDATED_INSTANT)
            .location(UPDATED_LOCATION)
            .distance(UPDATED_DISTANCE)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .totalDuration(UPDATED_TOTAL_DURATION)
            .value(UPDATED_VALUE)
            .considerTravelExpenses(UPDATED_CONSIDER_TRAVEL_EXPENSES)
            .travelExpensesValue(UPDATED_TRAVEL_EXPENSES_VALUE)
            .doValueRetention(UPDATED_DO_VALUE_RETENTION)
            .retentionPercentage(UPDATED_RETENTION_PERCENTAGE)
            .retentionValue(UPDATED_RETENTION_VALUE)
            .memberValue(UPDATED_MEMBER_VALUE)
            .comments(UPDATED_COMMENTS);
        return venue;
    }

    @BeforeEach
    public void initTest() {
        venue = createEntity(em);
    }

    @Test
    @Transactional
    void createVenue() throws Exception {
        int databaseSizeBeforeCreate = venueRepository.findAll().size();
        // Create the Venue
        VenueDTO venueDTO = venueMapper.toDto(venue);
        restVenueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venueDTO)))
            .andExpect(status().isCreated());

        // Validate the Venue in the database
        List<Venue> venueList = venueRepository.findAll();
        assertThat(venueList).hasSize(databaseSizeBeforeCreate + 1);
        Venue testVenue = venueList.get(venueList.size() - 1);
        assertThat(testVenue.getInstant()).isEqualTo(DEFAULT_INSTANT);
        assertThat(testVenue.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testVenue.getDistance()).isEqualByComparingTo(DEFAULT_DISTANCE);
        assertThat(testVenue.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testVenue.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testVenue.getTotalDuration()).isEqualTo(DEFAULT_TOTAL_DURATION);
        assertThat(testVenue.getValue()).isEqualByComparingTo(DEFAULT_VALUE);
        assertThat(testVenue.getConsiderTravelExpenses()).isEqualTo(DEFAULT_CONSIDER_TRAVEL_EXPENSES);
        assertThat(testVenue.getTravelExpensesValue()).isEqualByComparingTo(DEFAULT_TRAVEL_EXPENSES_VALUE);
        assertThat(testVenue.getDoValueRetention()).isEqualTo(DEFAULT_DO_VALUE_RETENTION);
        assertThat(testVenue.getRetentionPercentage()).isEqualByComparingTo(DEFAULT_RETENTION_PERCENTAGE);
        assertThat(testVenue.getRetentionValue()).isEqualByComparingTo(DEFAULT_RETENTION_VALUE);
        assertThat(testVenue.getMemberValue()).isEqualByComparingTo(DEFAULT_MEMBER_VALUE);
        assertThat(testVenue.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    void createVenueWithExistingId() throws Exception {
        // Create the Venue with an existing ID
        venue.setId(1L);
        VenueDTO venueDTO = venueMapper.toDto(venue);

        int databaseSizeBeforeCreate = venueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVenueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Venue in the database
        List<Venue> venueList = venueRepository.findAll();
        assertThat(venueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInstantIsRequired() throws Exception {
        int databaseSizeBeforeTest = venueRepository.findAll().size();
        // set the field null
        venue.setInstant(null);

        // Create the Venue, which fails.
        VenueDTO venueDTO = venueMapper.toDto(venue);

        restVenueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venueDTO)))
            .andExpect(status().isBadRequest());

        List<Venue> venueList = venueRepository.findAll();
        assertThat(venueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = venueRepository.findAll().size();
        // set the field null
        venue.setLocation(null);

        // Create the Venue, which fails.
        VenueDTO venueDTO = venueMapper.toDto(venue);

        restVenueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venueDTO)))
            .andExpect(status().isBadRequest());

        List<Venue> venueList = venueRepository.findAll();
        assertThat(venueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDistanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = venueRepository.findAll().size();
        // set the field null
        venue.setDistance(null);

        // Create the Venue, which fails.
        VenueDTO venueDTO = venueMapper.toDto(venue);

        restVenueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venueDTO)))
            .andExpect(status().isBadRequest());

        List<Venue> venueList = venueRepository.findAll();
        assertThat(venueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = venueRepository.findAll().size();
        // set the field null
        venue.setType(null);

        // Create the Venue, which fails.
        VenueDTO venueDTO = venueMapper.toDto(venue);

        restVenueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venueDTO)))
            .andExpect(status().isBadRequest());

        List<Venue> venueList = venueRepository.findAll();
        assertThat(venueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = venueRepository.findAll().size();
        // set the field null
        venue.setStatus(null);

        // Create the Venue, which fails.
        VenueDTO venueDTO = venueMapper.toDto(venue);

        restVenueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venueDTO)))
            .andExpect(status().isBadRequest());

        List<Venue> venueList = venueRepository.findAll();
        assertThat(venueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVenues() throws Exception {
        // Initialize the database
        venueRepository.saveAndFlush(venue);

        // Get all the venueList
        restVenueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(venue.getId().intValue())))
            .andExpect(jsonPath("$.[*].instant").value(hasItem(DEFAULT_INSTANT.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(sameNumber(DEFAULT_DISTANCE))))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].totalDuration").value(hasItem(DEFAULT_TOTAL_DURATION.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(sameNumber(DEFAULT_VALUE))))
            .andExpect(jsonPath("$.[*].considerTravelExpenses").value(hasItem(DEFAULT_CONSIDER_TRAVEL_EXPENSES.booleanValue())))
            .andExpect(jsonPath("$.[*].travelExpensesValue").value(hasItem(sameNumber(DEFAULT_TRAVEL_EXPENSES_VALUE))))
            .andExpect(jsonPath("$.[*].doValueRetention").value(hasItem(DEFAULT_DO_VALUE_RETENTION.booleanValue())))
            .andExpect(jsonPath("$.[*].retentionPercentage").value(hasItem(sameNumber(DEFAULT_RETENTION_PERCENTAGE))))
            .andExpect(jsonPath("$.[*].retentionValue").value(hasItem(sameNumber(DEFAULT_RETENTION_VALUE))))
            .andExpect(jsonPath("$.[*].memberValue").value(hasItem(sameNumber(DEFAULT_MEMBER_VALUE))))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }

    @Test
    @Transactional
    void getVenue() throws Exception {
        // Initialize the database
        venueRepository.saveAndFlush(venue);

        // Get the venue
        restVenueMockMvc
            .perform(get(ENTITY_API_URL_ID, venue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(venue.getId().intValue()))
            .andExpect(jsonPath("$.instant").value(DEFAULT_INSTANT.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.distance").value(sameNumber(DEFAULT_DISTANCE)))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.totalDuration").value(DEFAULT_TOTAL_DURATION.toString()))
            .andExpect(jsonPath("$.value").value(sameNumber(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.considerTravelExpenses").value(DEFAULT_CONSIDER_TRAVEL_EXPENSES.booleanValue()))
            .andExpect(jsonPath("$.travelExpensesValue").value(sameNumber(DEFAULT_TRAVEL_EXPENSES_VALUE)))
            .andExpect(jsonPath("$.doValueRetention").value(DEFAULT_DO_VALUE_RETENTION.booleanValue()))
            .andExpect(jsonPath("$.retentionPercentage").value(sameNumber(DEFAULT_RETENTION_PERCENTAGE)))
            .andExpect(jsonPath("$.retentionValue").value(sameNumber(DEFAULT_RETENTION_VALUE)))
            .andExpect(jsonPath("$.memberValue").value(sameNumber(DEFAULT_MEMBER_VALUE)))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }

    @Test
    @Transactional
    void getNonExistingVenue() throws Exception {
        // Get the venue
        restVenueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVenue() throws Exception {
        // Initialize the database
        venueRepository.saveAndFlush(venue);

        int databaseSizeBeforeUpdate = venueRepository.findAll().size();

        // Update the venue
        Venue updatedVenue = venueRepository.findById(venue.getId()).get();
        // Disconnect from session so that the updates on updatedVenue are not directly saved in db
        em.detach(updatedVenue);
        updatedVenue
            .instant(UPDATED_INSTANT)
            .location(UPDATED_LOCATION)
            .distance(UPDATED_DISTANCE)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .totalDuration(UPDATED_TOTAL_DURATION)
            .value(UPDATED_VALUE)
            .considerTravelExpenses(UPDATED_CONSIDER_TRAVEL_EXPENSES)
            .travelExpensesValue(UPDATED_TRAVEL_EXPENSES_VALUE)
            .doValueRetention(UPDATED_DO_VALUE_RETENTION)
            .retentionPercentage(UPDATED_RETENTION_PERCENTAGE)
            .retentionValue(UPDATED_RETENTION_VALUE)
            .memberValue(UPDATED_MEMBER_VALUE)
            .comments(UPDATED_COMMENTS);
        VenueDTO venueDTO = venueMapper.toDto(updatedVenue);

        restVenueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, venueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(venueDTO))
            )
            .andExpect(status().isOk());

        // Validate the Venue in the database
        List<Venue> venueList = venueRepository.findAll();
        assertThat(venueList).hasSize(databaseSizeBeforeUpdate);
        Venue testVenue = venueList.get(venueList.size() - 1);
        assertThat(testVenue.getInstant()).isEqualTo(UPDATED_INSTANT);
        assertThat(testVenue.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testVenue.getDistance()).isEqualByComparingTo(UPDATED_DISTANCE);
        assertThat(testVenue.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testVenue.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testVenue.getTotalDuration()).isEqualTo(UPDATED_TOTAL_DURATION);
        assertThat(testVenue.getValue()).isEqualByComparingTo(UPDATED_VALUE);
        assertThat(testVenue.getConsiderTravelExpenses()).isEqualTo(UPDATED_CONSIDER_TRAVEL_EXPENSES);
        assertThat(testVenue.getTravelExpensesValue()).isEqualByComparingTo(UPDATED_TRAVEL_EXPENSES_VALUE);
        assertThat(testVenue.getDoValueRetention()).isEqualTo(UPDATED_DO_VALUE_RETENTION);
        assertThat(testVenue.getRetentionPercentage()).isEqualByComparingTo(UPDATED_RETENTION_PERCENTAGE);
        assertThat(testVenue.getRetentionValue()).isEqualByComparingTo(UPDATED_RETENTION_VALUE);
        assertThat(testVenue.getMemberValue()).isEqualByComparingTo(UPDATED_MEMBER_VALUE);
        assertThat(testVenue.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void putNonExistingVenue() throws Exception {
        int databaseSizeBeforeUpdate = venueRepository.findAll().size();
        venue.setId(count.incrementAndGet());

        // Create the Venue
        VenueDTO venueDTO = venueMapper.toDto(venue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVenueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, venueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(venueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venue in the database
        List<Venue> venueList = venueRepository.findAll();
        assertThat(venueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVenue() throws Exception {
        int databaseSizeBeforeUpdate = venueRepository.findAll().size();
        venue.setId(count.incrementAndGet());

        // Create the Venue
        VenueDTO venueDTO = venueMapper.toDto(venue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVenueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(venueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venue in the database
        List<Venue> venueList = venueRepository.findAll();
        assertThat(venueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVenue() throws Exception {
        int databaseSizeBeforeUpdate = venueRepository.findAll().size();
        venue.setId(count.incrementAndGet());

        // Create the Venue
        VenueDTO venueDTO = venueMapper.toDto(venue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVenueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(venueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Venue in the database
        List<Venue> venueList = venueRepository.findAll();
        assertThat(venueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVenueWithPatch() throws Exception {
        // Initialize the database
        venueRepository.saveAndFlush(venue);

        int databaseSizeBeforeUpdate = venueRepository.findAll().size();

        // Update the venue using partial update
        Venue partialUpdatedVenue = new Venue();
        partialUpdatedVenue.setId(venue.getId());

        partialUpdatedVenue
            .instant(UPDATED_INSTANT)
            .location(UPDATED_LOCATION)
            .distance(UPDATED_DISTANCE)
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE)
            .doValueRetention(UPDATED_DO_VALUE_RETENTION);

        restVenueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVenue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVenue))
            )
            .andExpect(status().isOk());

        // Validate the Venue in the database
        List<Venue> venueList = venueRepository.findAll();
        assertThat(venueList).hasSize(databaseSizeBeforeUpdate);
        Venue testVenue = venueList.get(venueList.size() - 1);
        assertThat(testVenue.getInstant()).isEqualTo(UPDATED_INSTANT);
        assertThat(testVenue.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testVenue.getDistance()).isEqualByComparingTo(UPDATED_DISTANCE);
        assertThat(testVenue.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testVenue.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testVenue.getTotalDuration()).isEqualTo(DEFAULT_TOTAL_DURATION);
        assertThat(testVenue.getValue()).isEqualByComparingTo(UPDATED_VALUE);
        assertThat(testVenue.getConsiderTravelExpenses()).isEqualTo(DEFAULT_CONSIDER_TRAVEL_EXPENSES);
        assertThat(testVenue.getTravelExpensesValue()).isEqualByComparingTo(DEFAULT_TRAVEL_EXPENSES_VALUE);
        assertThat(testVenue.getDoValueRetention()).isEqualTo(UPDATED_DO_VALUE_RETENTION);
        assertThat(testVenue.getRetentionPercentage()).isEqualByComparingTo(DEFAULT_RETENTION_PERCENTAGE);
        assertThat(testVenue.getRetentionValue()).isEqualByComparingTo(DEFAULT_RETENTION_VALUE);
        assertThat(testVenue.getMemberValue()).isEqualByComparingTo(DEFAULT_MEMBER_VALUE);
        assertThat(testVenue.getComments()).isEqualTo(DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    void fullUpdateVenueWithPatch() throws Exception {
        // Initialize the database
        venueRepository.saveAndFlush(venue);

        int databaseSizeBeforeUpdate = venueRepository.findAll().size();

        // Update the venue using partial update
        Venue partialUpdatedVenue = new Venue();
        partialUpdatedVenue.setId(venue.getId());

        partialUpdatedVenue
            .instant(UPDATED_INSTANT)
            .location(UPDATED_LOCATION)
            .distance(UPDATED_DISTANCE)
            .type(UPDATED_TYPE)
            .status(UPDATED_STATUS)
            .totalDuration(UPDATED_TOTAL_DURATION)
            .value(UPDATED_VALUE)
            .considerTravelExpenses(UPDATED_CONSIDER_TRAVEL_EXPENSES)
            .travelExpensesValue(UPDATED_TRAVEL_EXPENSES_VALUE)
            .doValueRetention(UPDATED_DO_VALUE_RETENTION)
            .retentionPercentage(UPDATED_RETENTION_PERCENTAGE)
            .retentionValue(UPDATED_RETENTION_VALUE)
            .memberValue(UPDATED_MEMBER_VALUE)
            .comments(UPDATED_COMMENTS);

        restVenueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVenue.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVenue))
            )
            .andExpect(status().isOk());

        // Validate the Venue in the database
        List<Venue> venueList = venueRepository.findAll();
        assertThat(venueList).hasSize(databaseSizeBeforeUpdate);
        Venue testVenue = venueList.get(venueList.size() - 1);
        assertThat(testVenue.getInstant()).isEqualTo(UPDATED_INSTANT);
        assertThat(testVenue.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testVenue.getDistance()).isEqualByComparingTo(UPDATED_DISTANCE);
        assertThat(testVenue.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testVenue.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testVenue.getTotalDuration()).isEqualTo(UPDATED_TOTAL_DURATION);
        assertThat(testVenue.getValue()).isEqualByComparingTo(UPDATED_VALUE);
        assertThat(testVenue.getConsiderTravelExpenses()).isEqualTo(UPDATED_CONSIDER_TRAVEL_EXPENSES);
        assertThat(testVenue.getTravelExpensesValue()).isEqualByComparingTo(UPDATED_TRAVEL_EXPENSES_VALUE);
        assertThat(testVenue.getDoValueRetention()).isEqualTo(UPDATED_DO_VALUE_RETENTION);
        assertThat(testVenue.getRetentionPercentage()).isEqualByComparingTo(UPDATED_RETENTION_PERCENTAGE);
        assertThat(testVenue.getRetentionValue()).isEqualByComparingTo(UPDATED_RETENTION_VALUE);
        assertThat(testVenue.getMemberValue()).isEqualByComparingTo(UPDATED_MEMBER_VALUE);
        assertThat(testVenue.getComments()).isEqualTo(UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void patchNonExistingVenue() throws Exception {
        int databaseSizeBeforeUpdate = venueRepository.findAll().size();
        venue.setId(count.incrementAndGet());

        // Create the Venue
        VenueDTO venueDTO = venueMapper.toDto(venue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVenueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, venueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(venueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venue in the database
        List<Venue> venueList = venueRepository.findAll();
        assertThat(venueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVenue() throws Exception {
        int databaseSizeBeforeUpdate = venueRepository.findAll().size();
        venue.setId(count.incrementAndGet());

        // Create the Venue
        VenueDTO venueDTO = venueMapper.toDto(venue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVenueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(venueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Venue in the database
        List<Venue> venueList = venueRepository.findAll();
        assertThat(venueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVenue() throws Exception {
        int databaseSizeBeforeUpdate = venueRepository.findAll().size();
        venue.setId(count.incrementAndGet());

        // Create the Venue
        VenueDTO venueDTO = venueMapper.toDto(venue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVenueMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(venueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Venue in the database
        List<Venue> venueList = venueRepository.findAll();
        assertThat(venueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVenue() throws Exception {
        // Initialize the database
        venueRepository.saveAndFlush(venue);

        int databaseSizeBeforeDelete = venueRepository.findAll().size();

        // Delete the venue
        restVenueMockMvc
            .perform(delete(ENTITY_API_URL_ID, venue.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Venue> venueList = venueRepository.findAll();
        assertThat(venueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

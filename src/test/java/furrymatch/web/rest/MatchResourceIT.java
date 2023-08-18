package furrymatch.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import furrymatch.IntegrationTest;
import furrymatch.domain.Match;
import furrymatch.repository.MatchRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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

/**
 * Integration tests for the {@link MatchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MatchResourceIT {

    private static final Boolean DEFAULT_NOTIFY_MATCH = false;
    private static final Boolean UPDATED_NOTIFY_MATCH = true;

    private static final LocalDate DEFAULT_DATE_MATCH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MATCH = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/matches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMatchMockMvc;

    private Match match;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Match createEntity(EntityManager em) {
        Match match = new Match().notifyMatch(DEFAULT_NOTIFY_MATCH).dateMatch(DEFAULT_DATE_MATCH);
        return match;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Match createUpdatedEntity(EntityManager em) {
        Match match = new Match().notifyMatch(UPDATED_NOTIFY_MATCH).dateMatch(UPDATED_DATE_MATCH);
        return match;
    }

    @BeforeEach
    public void initTest() {
        match = createEntity(em);
    }

    @Test
    @Transactional
    void createMatch() throws Exception {
        int databaseSizeBeforeCreate = matchRepository.findAll().size();
        // Create the Match
        restMatchMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(match))
            )
            .andExpect(status().isCreated());

        // Validate the Match in the database
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeCreate + 1);
        Match testMatch = matchList.get(matchList.size() - 1);
        assertThat(testMatch.getNotifyMatch()).isEqualTo(DEFAULT_NOTIFY_MATCH);
        assertThat(testMatch.getDateMatch()).isEqualTo(DEFAULT_DATE_MATCH);
    }

    @Test
    @Transactional
    void createMatchWithExistingId() throws Exception {
        // Create the Match with an existing ID
        match.setId(1L);

        int databaseSizeBeforeCreate = matchRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMatchMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(match))
            )
            .andExpect(status().isBadRequest());

        // Validate the Match in the database
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMatches() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get all the matchList
        restMatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(match.getId().intValue())))
            .andExpect(jsonPath("$.[*].notifyMatch").value(hasItem(DEFAULT_NOTIFY_MATCH.booleanValue())))
            .andExpect(jsonPath("$.[*].dateMatch").value(hasItem(DEFAULT_DATE_MATCH.toString())));
    }

    @Test
    @Transactional
    void getMatch() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        // Get the match
        restMatchMockMvc
            .perform(get(ENTITY_API_URL_ID, match.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(match.getId().intValue()))
            .andExpect(jsonPath("$.notifyMatch").value(DEFAULT_NOTIFY_MATCH.booleanValue()))
            .andExpect(jsonPath("$.dateMatch").value(DEFAULT_DATE_MATCH.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMatch() throws Exception {
        // Get the match
        restMatchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMatch() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        int databaseSizeBeforeUpdate = matchRepository.findAll().size();

        // Update the match
        Match updatedMatch = matchRepository.findById(match.getId()).get();
        // Disconnect from session so that the updates on updatedMatch are not directly saved in db
        em.detach(updatedMatch);
        updatedMatch.notifyMatch(UPDATED_NOTIFY_MATCH).dateMatch(UPDATED_DATE_MATCH);

        restMatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMatch.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMatch))
            )
            .andExpect(status().isOk());

        // Validate the Match in the database
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeUpdate);
        Match testMatch = matchList.get(matchList.size() - 1);
        assertThat(testMatch.getNotifyMatch()).isEqualTo(UPDATED_NOTIFY_MATCH);
        assertThat(testMatch.getDateMatch()).isEqualTo(UPDATED_DATE_MATCH);
    }

    @Test
    @Transactional
    void putNonExistingMatch() throws Exception {
        int databaseSizeBeforeUpdate = matchRepository.findAll().size();
        match.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, match.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(match))
            )
            .andExpect(status().isBadRequest());

        // Validate the Match in the database
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMatch() throws Exception {
        int databaseSizeBeforeUpdate = matchRepository.findAll().size();
        match.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(match))
            )
            .andExpect(status().isBadRequest());

        // Validate the Match in the database
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMatch() throws Exception {
        int databaseSizeBeforeUpdate = matchRepository.findAll().size();
        match.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatchMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(match))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Match in the database
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMatchWithPatch() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        int databaseSizeBeforeUpdate = matchRepository.findAll().size();

        // Update the match using partial update
        Match partialUpdatedMatch = new Match();
        partialUpdatedMatch.setId(match.getId());

        partialUpdatedMatch.dateMatch(UPDATED_DATE_MATCH);

        restMatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatch.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatch))
            )
            .andExpect(status().isOk());

        // Validate the Match in the database
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeUpdate);
        Match testMatch = matchList.get(matchList.size() - 1);
        assertThat(testMatch.getNotifyMatch()).isEqualTo(DEFAULT_NOTIFY_MATCH);
        assertThat(testMatch.getDateMatch()).isEqualTo(UPDATED_DATE_MATCH);
    }

    @Test
    @Transactional
    void fullUpdateMatchWithPatch() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        int databaseSizeBeforeUpdate = matchRepository.findAll().size();

        // Update the match using partial update
        Match partialUpdatedMatch = new Match();
        partialUpdatedMatch.setId(match.getId());

        partialUpdatedMatch.notifyMatch(UPDATED_NOTIFY_MATCH).dateMatch(UPDATED_DATE_MATCH);

        restMatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMatch.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMatch))
            )
            .andExpect(status().isOk());

        // Validate the Match in the database
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeUpdate);
        Match testMatch = matchList.get(matchList.size() - 1);
        assertThat(testMatch.getNotifyMatch()).isEqualTo(UPDATED_NOTIFY_MATCH);
        assertThat(testMatch.getDateMatch()).isEqualTo(UPDATED_DATE_MATCH);
    }

    @Test
    @Transactional
    void patchNonExistingMatch() throws Exception {
        int databaseSizeBeforeUpdate = matchRepository.findAll().size();
        match.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, match.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(match))
            )
            .andExpect(status().isBadRequest());

        // Validate the Match in the database
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMatch() throws Exception {
        int databaseSizeBeforeUpdate = matchRepository.findAll().size();
        match.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(match))
            )
            .andExpect(status().isBadRequest());

        // Validate the Match in the database
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMatch() throws Exception {
        int databaseSizeBeforeUpdate = matchRepository.findAll().size();
        match.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMatchMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(match))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Match in the database
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMatch() throws Exception {
        // Initialize the database
        matchRepository.saveAndFlush(match);

        int databaseSizeBeforeDelete = matchRepository.findAll().size();

        // Delete the match
        restMatchMockMvc
            .perform(delete(ENTITY_API_URL_ID, match.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Match> matchList = matchRepository.findAll();
        assertThat(matchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

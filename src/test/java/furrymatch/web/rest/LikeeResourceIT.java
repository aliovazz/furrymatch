package furrymatch.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import furrymatch.IntegrationTest;
import furrymatch.domain.Likee;
import furrymatch.domain.enumeration.LikeType;
import furrymatch.repository.LikeeRepository;
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
 * Integration tests for the {@link LikeeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LikeeResourceIT {

    private static final LikeType DEFAULT_LIKE_STATE = LikeType.Like;
    private static final LikeType UPDATED_LIKE_STATE = LikeType.Dislike;

    private static final String ENTITY_API_URL = "/api/likees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LikeeRepository likeeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLikeeMockMvc;

    private Likee likee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Likee createEntity(EntityManager em) {
        Likee likee = new Likee().likeState(DEFAULT_LIKE_STATE);
        return likee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Likee createUpdatedEntity(EntityManager em) {
        Likee likee = new Likee().likeState(UPDATED_LIKE_STATE);
        return likee;
    }

    @BeforeEach
    public void initTest() {
        likee = createEntity(em);
    }

    @Test
    @Transactional
    void createLikee() throws Exception {
        int databaseSizeBeforeCreate = likeeRepository.findAll().size();
        // Create the Likee
        restLikeeMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likee))
            )
            .andExpect(status().isCreated());

        // Validate the Likee in the database
        List<Likee> likeeList = likeeRepository.findAll();
        assertThat(likeeList).hasSize(databaseSizeBeforeCreate + 1);
        Likee testLikee = likeeList.get(likeeList.size() - 1);
        assertThat(testLikee.getLikeState()).isEqualTo(DEFAULT_LIKE_STATE);
    }

    @Test
    @Transactional
    void createLikeeWithExistingId() throws Exception {
        // Create the Likee with an existing ID
        likee.setId(1L);

        int databaseSizeBeforeCreate = likeeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLikeeMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Likee in the database
        List<Likee> likeeList = likeeRepository.findAll();
        assertThat(likeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLikeStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = likeeRepository.findAll().size();
        // set the field null
        likee.setLikeState(null);

        // Create the Likee, which fails.

        restLikeeMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likee))
            )
            .andExpect(status().isBadRequest());

        List<Likee> likeeList = likeeRepository.findAll();
        assertThat(likeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLikees() throws Exception {
        // Initialize the database
        likeeRepository.saveAndFlush(likee);

        // Get all the likeeList
        restLikeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(likee.getId().intValue())))
            .andExpect(jsonPath("$.[*].likeState").value(hasItem(DEFAULT_LIKE_STATE.toString())));
    }

    @Test
    @Transactional
    void getLikee() throws Exception {
        // Initialize the database
        likeeRepository.saveAndFlush(likee);

        // Get the likee
        restLikeeMockMvc
            .perform(get(ENTITY_API_URL_ID, likee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(likee.getId().intValue()))
            .andExpect(jsonPath("$.likeState").value(DEFAULT_LIKE_STATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLikee() throws Exception {
        // Get the likee
        restLikeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLikee() throws Exception {
        // Initialize the database
        likeeRepository.saveAndFlush(likee);

        int databaseSizeBeforeUpdate = likeeRepository.findAll().size();

        // Update the likee
        Likee updatedLikee = likeeRepository.findById(likee.getId()).get();
        // Disconnect from session so that the updates on updatedLikee are not directly saved in db
        em.detach(updatedLikee);
        updatedLikee.likeState(UPDATED_LIKE_STATE);

        restLikeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLikee.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLikee))
            )
            .andExpect(status().isOk());

        // Validate the Likee in the database
        List<Likee> likeeList = likeeRepository.findAll();
        assertThat(likeeList).hasSize(databaseSizeBeforeUpdate);
        Likee testLikee = likeeList.get(likeeList.size() - 1);
        assertThat(testLikee.getLikeState()).isEqualTo(UPDATED_LIKE_STATE);
    }

    @Test
    @Transactional
    void putNonExistingLikee() throws Exception {
        int databaseSizeBeforeUpdate = likeeRepository.findAll().size();
        likee.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likee.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Likee in the database
        List<Likee> likeeList = likeeRepository.findAll();
        assertThat(likeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLikee() throws Exception {
        int databaseSizeBeforeUpdate = likeeRepository.findAll().size();
        likee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Likee in the database
        List<Likee> likeeList = likeeRepository.findAll();
        assertThat(likeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLikee() throws Exception {
        int databaseSizeBeforeUpdate = likeeRepository.findAll().size();
        likee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeeMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likee))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Likee in the database
        List<Likee> likeeList = likeeRepository.findAll();
        assertThat(likeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLikeeWithPatch() throws Exception {
        // Initialize the database
        likeeRepository.saveAndFlush(likee);

        int databaseSizeBeforeUpdate = likeeRepository.findAll().size();

        // Update the likee using partial update
        Likee partialUpdatedLikee = new Likee();
        partialUpdatedLikee.setId(likee.getId());

        partialUpdatedLikee.likeState(UPDATED_LIKE_STATE);

        restLikeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLikee))
            )
            .andExpect(status().isOk());

        // Validate the Likee in the database
        List<Likee> likeeList = likeeRepository.findAll();
        assertThat(likeeList).hasSize(databaseSizeBeforeUpdate);
        Likee testLikee = likeeList.get(likeeList.size() - 1);
        assertThat(testLikee.getLikeState()).isEqualTo(UPDATED_LIKE_STATE);
    }

    @Test
    @Transactional
    void fullUpdateLikeeWithPatch() throws Exception {
        // Initialize the database
        likeeRepository.saveAndFlush(likee);

        int databaseSizeBeforeUpdate = likeeRepository.findAll().size();

        // Update the likee using partial update
        Likee partialUpdatedLikee = new Likee();
        partialUpdatedLikee.setId(likee.getId());

        partialUpdatedLikee.likeState(UPDATED_LIKE_STATE);

        restLikeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLikee))
            )
            .andExpect(status().isOk());

        // Validate the Likee in the database
        List<Likee> likeeList = likeeRepository.findAll();
        assertThat(likeeList).hasSize(databaseSizeBeforeUpdate);
        Likee testLikee = likeeList.get(likeeList.size() - 1);
        assertThat(testLikee.getLikeState()).isEqualTo(UPDATED_LIKE_STATE);
    }

    @Test
    @Transactional
    void patchNonExistingLikee() throws Exception {
        int databaseSizeBeforeUpdate = likeeRepository.findAll().size();
        likee.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, likee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Likee in the database
        List<Likee> likeeList = likeeRepository.findAll();
        assertThat(likeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLikee() throws Exception {
        int databaseSizeBeforeUpdate = likeeRepository.findAll().size();
        likee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Likee in the database
        List<Likee> likeeList = likeeRepository.findAll();
        assertThat(likeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLikee() throws Exception {
        int databaseSizeBeforeUpdate = likeeRepository.findAll().size();
        likee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likee))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Likee in the database
        List<Likee> likeeList = likeeRepository.findAll();
        assertThat(likeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLikee() throws Exception {
        // Initialize the database
        likeeRepository.saveAndFlush(likee);

        int databaseSizeBeforeDelete = likeeRepository.findAll().size();

        // Delete the likee
        restLikeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, likee.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Likee> likeeList = likeeRepository.findAll();
        assertThat(likeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package furrymatch.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import furrymatch.IntegrationTest;
import furrymatch.domain.Breed;
import furrymatch.repository.BreedRepository;
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
 * Integration tests for the {@link BreedResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BreedResourceIT {

    private static final String DEFAULT_BREED = "AAAAAAAAAA";
    private static final String UPDATED_BREED = "BBBBBBBBBB";

    private static final String DEFAULT_BREED_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_BREED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/breeds";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BreedRepository breedRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBreedMockMvc;

    private Breed breed;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Breed createEntity(EntityManager em) {
        Breed breed = new Breed().breed(DEFAULT_BREED).breedType(DEFAULT_BREED_TYPE);
        return breed;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Breed createUpdatedEntity(EntityManager em) {
        Breed breed = new Breed().breed(UPDATED_BREED).breedType(UPDATED_BREED_TYPE);
        return breed;
    }

    @BeforeEach
    public void initTest() {
        breed = createEntity(em);
    }

    @Test
    @Transactional
    void createBreed() throws Exception {
        int databaseSizeBeforeCreate = breedRepository.findAll().size();
        // Create the Breed
        restBreedMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(breed))
            )
            .andExpect(status().isCreated());

        // Validate the Breed in the database
        List<Breed> breedList = breedRepository.findAll();
        assertThat(breedList).hasSize(databaseSizeBeforeCreate + 1);
        Breed testBreed = breedList.get(breedList.size() - 1);
        assertThat(testBreed.getBreed()).isEqualTo(DEFAULT_BREED);
        assertThat(testBreed.getBreedType()).isEqualTo(DEFAULT_BREED_TYPE);
    }

    @Test
    @Transactional
    void createBreedWithExistingId() throws Exception {
        // Create the Breed with an existing ID
        breed.setId(1L);

        int databaseSizeBeforeCreate = breedRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBreedMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(breed))
            )
            .andExpect(status().isBadRequest());

        // Validate the Breed in the database
        List<Breed> breedList = breedRepository.findAll();
        assertThat(breedList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBreedIsRequired() throws Exception {
        int databaseSizeBeforeTest = breedRepository.findAll().size();
        // set the field null
        breed.setBreed(null);

        // Create the Breed, which fails.

        restBreedMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(breed))
            )
            .andExpect(status().isBadRequest());

        List<Breed> breedList = breedRepository.findAll();
        assertThat(breedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBreedTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = breedRepository.findAll().size();
        // set the field null
        breed.setBreedType(null);

        // Create the Breed, which fails.

        restBreedMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(breed))
            )
            .andExpect(status().isBadRequest());

        List<Breed> breedList = breedRepository.findAll();
        assertThat(breedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBreeds() throws Exception {
        // Initialize the database
        breedRepository.saveAndFlush(breed);

        // Get all the breedList
        restBreedMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(breed.getId().intValue())))
            .andExpect(jsonPath("$.[*].breed").value(hasItem(DEFAULT_BREED)))
            .andExpect(jsonPath("$.[*].breedType").value(hasItem(DEFAULT_BREED_TYPE)));
    }

    @Test
    @Transactional
    void getBreed() throws Exception {
        // Initialize the database
        breedRepository.saveAndFlush(breed);

        // Get the breed
        restBreedMockMvc
            .perform(get(ENTITY_API_URL_ID, breed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(breed.getId().intValue()))
            .andExpect(jsonPath("$.breed").value(DEFAULT_BREED))
            .andExpect(jsonPath("$.breedType").value(DEFAULT_BREED_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingBreed() throws Exception {
        // Get the breed
        restBreedMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBreed() throws Exception {
        // Initialize the database
        breedRepository.saveAndFlush(breed);

        int databaseSizeBeforeUpdate = breedRepository.findAll().size();

        // Update the breed
        Breed updatedBreed = breedRepository.findById(breed.getId()).get();
        // Disconnect from session so that the updates on updatedBreed are not directly saved in db
        em.detach(updatedBreed);
        updatedBreed.breed(UPDATED_BREED).breedType(UPDATED_BREED_TYPE);

        restBreedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBreed.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBreed))
            )
            .andExpect(status().isOk());

        // Validate the Breed in the database
        List<Breed> breedList = breedRepository.findAll();
        assertThat(breedList).hasSize(databaseSizeBeforeUpdate);
        Breed testBreed = breedList.get(breedList.size() - 1);
        assertThat(testBreed.getBreed()).isEqualTo(UPDATED_BREED);
        assertThat(testBreed.getBreedType()).isEqualTo(UPDATED_BREED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingBreed() throws Exception {
        int databaseSizeBeforeUpdate = breedRepository.findAll().size();
        breed.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBreedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, breed.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(breed))
            )
            .andExpect(status().isBadRequest());

        // Validate the Breed in the database
        List<Breed> breedList = breedRepository.findAll();
        assertThat(breedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBreed() throws Exception {
        int databaseSizeBeforeUpdate = breedRepository.findAll().size();
        breed.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBreedMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(breed))
            )
            .andExpect(status().isBadRequest());

        // Validate the Breed in the database
        List<Breed> breedList = breedRepository.findAll();
        assertThat(breedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBreed() throws Exception {
        int databaseSizeBeforeUpdate = breedRepository.findAll().size();
        breed.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBreedMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(breed))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Breed in the database
        List<Breed> breedList = breedRepository.findAll();
        assertThat(breedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBreedWithPatch() throws Exception {
        // Initialize the database
        breedRepository.saveAndFlush(breed);

        int databaseSizeBeforeUpdate = breedRepository.findAll().size();

        // Update the breed using partial update
        Breed partialUpdatedBreed = new Breed();
        partialUpdatedBreed.setId(breed.getId());

        restBreedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBreed.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBreed))
            )
            .andExpect(status().isOk());

        // Validate the Breed in the database
        List<Breed> breedList = breedRepository.findAll();
        assertThat(breedList).hasSize(databaseSizeBeforeUpdate);
        Breed testBreed = breedList.get(breedList.size() - 1);
        assertThat(testBreed.getBreed()).isEqualTo(DEFAULT_BREED);
        assertThat(testBreed.getBreedType()).isEqualTo(DEFAULT_BREED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateBreedWithPatch() throws Exception {
        // Initialize the database
        breedRepository.saveAndFlush(breed);

        int databaseSizeBeforeUpdate = breedRepository.findAll().size();

        // Update the breed using partial update
        Breed partialUpdatedBreed = new Breed();
        partialUpdatedBreed.setId(breed.getId());

        partialUpdatedBreed.breed(UPDATED_BREED).breedType(UPDATED_BREED_TYPE);

        restBreedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBreed.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBreed))
            )
            .andExpect(status().isOk());

        // Validate the Breed in the database
        List<Breed> breedList = breedRepository.findAll();
        assertThat(breedList).hasSize(databaseSizeBeforeUpdate);
        Breed testBreed = breedList.get(breedList.size() - 1);
        assertThat(testBreed.getBreed()).isEqualTo(UPDATED_BREED);
        assertThat(testBreed.getBreedType()).isEqualTo(UPDATED_BREED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingBreed() throws Exception {
        int databaseSizeBeforeUpdate = breedRepository.findAll().size();
        breed.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBreedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, breed.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(breed))
            )
            .andExpect(status().isBadRequest());

        // Validate the Breed in the database
        List<Breed> breedList = breedRepository.findAll();
        assertThat(breedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBreed() throws Exception {
        int databaseSizeBeforeUpdate = breedRepository.findAll().size();
        breed.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBreedMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(breed))
            )
            .andExpect(status().isBadRequest());

        // Validate the Breed in the database
        List<Breed> breedList = breedRepository.findAll();
        assertThat(breedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBreed() throws Exception {
        int databaseSizeBeforeUpdate = breedRepository.findAll().size();
        breed.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBreedMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(breed))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Breed in the database
        List<Breed> breedList = breedRepository.findAll();
        assertThat(breedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBreed() throws Exception {
        // Initialize the database
        breedRepository.saveAndFlush(breed);

        int databaseSizeBeforeDelete = breedRepository.findAll().size();

        // Delete the breed
        restBreedMockMvc
            .perform(delete(ENTITY_API_URL_ID, breed.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Breed> breedList = breedRepository.findAll();
        assertThat(breedList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

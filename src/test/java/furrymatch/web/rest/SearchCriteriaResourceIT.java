package furrymatch.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import furrymatch.IntegrationTest;
import furrymatch.domain.SearchCriteria;
import furrymatch.domain.enumeration.Sex;
import furrymatch.repository.SearchCriteriaRepository;
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
 * Integration tests for the {@link SearchCriteriaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SearchCriteriaResourceIT {

    private static final String DEFAULT_FILTER_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FILTER_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_BREED = "AAAAAAAAAA";
    private static final String UPDATED_BREED = "BBBBBBBBBB";

    private static final String DEFAULT_TRADE_PUPS = "AAAAAAAAAA";
    private static final String UPDATED_TRADE_PUPS = "BBBBBBBBBB";

    private static final Sex DEFAULT_SEX = Sex.Hembra;
    private static final Sex UPDATED_SEX = Sex.Macho;

    private static final String DEFAULT_PEDIGREE = "AAAAAAAAAA";
    private static final String UPDATED_PEDIGREE = "BBBBBBBBBB";

    private static final String DEFAULT_TRADE_MONEY = "AAAAAAAAAA";
    private static final String UPDATED_TRADE_MONEY = "BBBBBBBBBB";

    private static final String DEFAULT_PROVICE = "AAAAAAAAAA";
    private static final String UPDATED_PROVICE = "BBBBBBBBBB";

    private static final String DEFAULT_CANTON = "AAAAAAAAAA";
    private static final String UPDATED_CANTON = "BBBBBBBBBB";

    private static final String DEFAULT_DISTRICT = "AAAAAAAAAA";
    private static final String UPDATED_DISTRICT = "BBBBBBBBBB";

    private static final String DEFAULT_OBJECTIVE = "AAAAAAAAAA";
    private static final String UPDATED_OBJECTIVE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/search-criteria";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SearchCriteriaRepository searchCriteriaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSearchCriteriaMockMvc;

    private SearchCriteria searchCriteria;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchCriteria createEntity(EntityManager em) {
        SearchCriteria searchCriteria = new SearchCriteria()
            .filterType(DEFAULT_FILTER_TYPE)
            .breed(DEFAULT_BREED)
            .tradePups(DEFAULT_TRADE_PUPS)
            .sex(DEFAULT_SEX)
            .pedigree(DEFAULT_PEDIGREE)
            .tradeMoney(DEFAULT_TRADE_MONEY)
            .provice(DEFAULT_PROVICE)
            .canton(DEFAULT_CANTON)
            .district(DEFAULT_DISTRICT)
            .objective(DEFAULT_OBJECTIVE);
        return searchCriteria;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchCriteria createUpdatedEntity(EntityManager em) {
        SearchCriteria searchCriteria = new SearchCriteria()
            .filterType(UPDATED_FILTER_TYPE)
            .breed(UPDATED_BREED)
            .tradePups(UPDATED_TRADE_PUPS)
            .sex(UPDATED_SEX)
            .pedigree(UPDATED_PEDIGREE)
            .tradeMoney(UPDATED_TRADE_MONEY)
            .provice(UPDATED_PROVICE)
            .canton(UPDATED_CANTON)
            .district(UPDATED_DISTRICT)
            .objective(UPDATED_OBJECTIVE);
        return searchCriteria;
    }

    @BeforeEach
    public void initTest() {
        searchCriteria = createEntity(em);
    }

    @Test
    @Transactional
    void createSearchCriteria() throws Exception {
        int databaseSizeBeforeCreate = searchCriteriaRepository.findAll().size();
        // Create the SearchCriteria
        restSearchCriteriaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(searchCriteria))
            )
            .andExpect(status().isCreated());

        // Validate the SearchCriteria in the database
        List<SearchCriteria> searchCriteriaList = searchCriteriaRepository.findAll();
        assertThat(searchCriteriaList).hasSize(databaseSizeBeforeCreate + 1);
        SearchCriteria testSearchCriteria = searchCriteriaList.get(searchCriteriaList.size() - 1);
        assertThat(testSearchCriteria.getFilterType()).isEqualTo(DEFAULT_FILTER_TYPE);
        assertThat(testSearchCriteria.getBreed()).isEqualTo(DEFAULT_BREED);
        assertThat(testSearchCriteria.getTradePups()).isEqualTo(DEFAULT_TRADE_PUPS);
        assertThat(testSearchCriteria.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testSearchCriteria.getPedigree()).isEqualTo(DEFAULT_PEDIGREE);
        assertThat(testSearchCriteria.getTradeMoney()).isEqualTo(DEFAULT_TRADE_MONEY);
        assertThat(testSearchCriteria.getProvice()).isEqualTo(DEFAULT_PROVICE);
        assertThat(testSearchCriteria.getCanton()).isEqualTo(DEFAULT_CANTON);
        assertThat(testSearchCriteria.getDistrict()).isEqualTo(DEFAULT_DISTRICT);
        assertThat(testSearchCriteria.getObjective()).isEqualTo(DEFAULT_OBJECTIVE);
    }

    @Test
    @Transactional
    void createSearchCriteriaWithExistingId() throws Exception {
        // Create the SearchCriteria with an existing ID
        searchCriteria.setId(1L);

        int databaseSizeBeforeCreate = searchCriteriaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSearchCriteriaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(searchCriteria))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchCriteria in the database
        List<SearchCriteria> searchCriteriaList = searchCriteriaRepository.findAll();
        assertThat(searchCriteriaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFilterTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = searchCriteriaRepository.findAll().size();
        // set the field null
        searchCriteria.setFilterType(null);

        // Create the SearchCriteria, which fails.

        restSearchCriteriaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(searchCriteria))
            )
            .andExpect(status().isBadRequest());

        List<SearchCriteria> searchCriteriaList = searchCriteriaRepository.findAll();
        assertThat(searchCriteriaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSearchCriteria() throws Exception {
        // Initialize the database
        searchCriteriaRepository.saveAndFlush(searchCriteria);

        // Get all the searchCriteriaList
        restSearchCriteriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchCriteria.getId().intValue())))
            .andExpect(jsonPath("$.[*].filterType").value(hasItem(DEFAULT_FILTER_TYPE)))
            .andExpect(jsonPath("$.[*].breed").value(hasItem(DEFAULT_BREED)))
            .andExpect(jsonPath("$.[*].tradePups").value(hasItem(DEFAULT_TRADE_PUPS)))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())))
            .andExpect(jsonPath("$.[*].pedigree").value(hasItem(DEFAULT_PEDIGREE)))
            .andExpect(jsonPath("$.[*].tradeMoney").value(hasItem(DEFAULT_TRADE_MONEY)))
            .andExpect(jsonPath("$.[*].provice").value(hasItem(DEFAULT_PROVICE)))
            .andExpect(jsonPath("$.[*].canton").value(hasItem(DEFAULT_CANTON)))
            .andExpect(jsonPath("$.[*].district").value(hasItem(DEFAULT_DISTRICT)))
            .andExpect(jsonPath("$.[*].objective").value(hasItem(DEFAULT_OBJECTIVE)));
    }

    @Test
    @Transactional
    void getSearchCriteria() throws Exception {
        // Initialize the database
        searchCriteriaRepository.saveAndFlush(searchCriteria);

        // Get the searchCriteria
        restSearchCriteriaMockMvc
            .perform(get(ENTITY_API_URL_ID, searchCriteria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(searchCriteria.getId().intValue()))
            .andExpect(jsonPath("$.filterType").value(DEFAULT_FILTER_TYPE))
            .andExpect(jsonPath("$.breed").value(DEFAULT_BREED))
            .andExpect(jsonPath("$.tradePups").value(DEFAULT_TRADE_PUPS))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.toString()))
            .andExpect(jsonPath("$.pedigree").value(DEFAULT_PEDIGREE))
            .andExpect(jsonPath("$.tradeMoney").value(DEFAULT_TRADE_MONEY))
            .andExpect(jsonPath("$.provice").value(DEFAULT_PROVICE))
            .andExpect(jsonPath("$.canton").value(DEFAULT_CANTON))
            .andExpect(jsonPath("$.district").value(DEFAULT_DISTRICT))
            .andExpect(jsonPath("$.objective").value(DEFAULT_OBJECTIVE));
    }

    @Test
    @Transactional
    void getNonExistingSearchCriteria() throws Exception {
        // Get the searchCriteria
        restSearchCriteriaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSearchCriteria() throws Exception {
        // Initialize the database
        searchCriteriaRepository.saveAndFlush(searchCriteria);

        int databaseSizeBeforeUpdate = searchCriteriaRepository.findAll().size();

        // Update the searchCriteria
        SearchCriteria updatedSearchCriteria = searchCriteriaRepository.findById(searchCriteria.getId()).get();
        // Disconnect from session so that the updates on updatedSearchCriteria are not directly saved in db
        em.detach(updatedSearchCriteria);
        updatedSearchCriteria
            .filterType(UPDATED_FILTER_TYPE)
            .breed(UPDATED_BREED)
            .tradePups(UPDATED_TRADE_PUPS)
            .sex(UPDATED_SEX)
            .pedigree(UPDATED_PEDIGREE)
            .tradeMoney(UPDATED_TRADE_MONEY)
            .provice(UPDATED_PROVICE)
            .canton(UPDATED_CANTON)
            .district(UPDATED_DISTRICT)
            .objective(UPDATED_OBJECTIVE);

        restSearchCriteriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSearchCriteria.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSearchCriteria))
            )
            .andExpect(status().isOk());

        // Validate the SearchCriteria in the database
        List<SearchCriteria> searchCriteriaList = searchCriteriaRepository.findAll();
        assertThat(searchCriteriaList).hasSize(databaseSizeBeforeUpdate);
        SearchCriteria testSearchCriteria = searchCriteriaList.get(searchCriteriaList.size() - 1);
        assertThat(testSearchCriteria.getFilterType()).isEqualTo(UPDATED_FILTER_TYPE);
        assertThat(testSearchCriteria.getBreed()).isEqualTo(UPDATED_BREED);
        assertThat(testSearchCriteria.getTradePups()).isEqualTo(UPDATED_TRADE_PUPS);
        assertThat(testSearchCriteria.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testSearchCriteria.getPedigree()).isEqualTo(UPDATED_PEDIGREE);
        assertThat(testSearchCriteria.getTradeMoney()).isEqualTo(UPDATED_TRADE_MONEY);
        assertThat(testSearchCriteria.getProvice()).isEqualTo(UPDATED_PROVICE);
        assertThat(testSearchCriteria.getCanton()).isEqualTo(UPDATED_CANTON);
        assertThat(testSearchCriteria.getDistrict()).isEqualTo(UPDATED_DISTRICT);
        assertThat(testSearchCriteria.getObjective()).isEqualTo(UPDATED_OBJECTIVE);
    }

    @Test
    @Transactional
    void putNonExistingSearchCriteria() throws Exception {
        int databaseSizeBeforeUpdate = searchCriteriaRepository.findAll().size();
        searchCriteria.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchCriteriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, searchCriteria.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(searchCriteria))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchCriteria in the database
        List<SearchCriteria> searchCriteriaList = searchCriteriaRepository.findAll();
        assertThat(searchCriteriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSearchCriteria() throws Exception {
        int databaseSizeBeforeUpdate = searchCriteriaRepository.findAll().size();
        searchCriteria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchCriteriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(searchCriteria))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchCriteria in the database
        List<SearchCriteria> searchCriteriaList = searchCriteriaRepository.findAll();
        assertThat(searchCriteriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSearchCriteria() throws Exception {
        int databaseSizeBeforeUpdate = searchCriteriaRepository.findAll().size();
        searchCriteria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchCriteriaMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(searchCriteria))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchCriteria in the database
        List<SearchCriteria> searchCriteriaList = searchCriteriaRepository.findAll();
        assertThat(searchCriteriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSearchCriteriaWithPatch() throws Exception {
        // Initialize the database
        searchCriteriaRepository.saveAndFlush(searchCriteria);

        int databaseSizeBeforeUpdate = searchCriteriaRepository.findAll().size();

        // Update the searchCriteria using partial update
        SearchCriteria partialUpdatedSearchCriteria = new SearchCriteria();
        partialUpdatedSearchCriteria.setId(searchCriteria.getId());

        partialUpdatedSearchCriteria
            .breed(UPDATED_BREED)
            .tradePups(UPDATED_TRADE_PUPS)
            .provice(UPDATED_PROVICE)
            .district(UPDATED_DISTRICT)
            .objective(UPDATED_OBJECTIVE);

        restSearchCriteriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearchCriteria.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSearchCriteria))
            )
            .andExpect(status().isOk());

        // Validate the SearchCriteria in the database
        List<SearchCriteria> searchCriteriaList = searchCriteriaRepository.findAll();
        assertThat(searchCriteriaList).hasSize(databaseSizeBeforeUpdate);
        SearchCriteria testSearchCriteria = searchCriteriaList.get(searchCriteriaList.size() - 1);
        assertThat(testSearchCriteria.getFilterType()).isEqualTo(DEFAULT_FILTER_TYPE);
        assertThat(testSearchCriteria.getBreed()).isEqualTo(UPDATED_BREED);
        assertThat(testSearchCriteria.getTradePups()).isEqualTo(UPDATED_TRADE_PUPS);
        assertThat(testSearchCriteria.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testSearchCriteria.getPedigree()).isEqualTo(DEFAULT_PEDIGREE);
        assertThat(testSearchCriteria.getTradeMoney()).isEqualTo(DEFAULT_TRADE_MONEY);
        assertThat(testSearchCriteria.getProvice()).isEqualTo(UPDATED_PROVICE);
        assertThat(testSearchCriteria.getCanton()).isEqualTo(DEFAULT_CANTON);
        assertThat(testSearchCriteria.getDistrict()).isEqualTo(UPDATED_DISTRICT);
        assertThat(testSearchCriteria.getObjective()).isEqualTo(UPDATED_OBJECTIVE);
    }

    @Test
    @Transactional
    void fullUpdateSearchCriteriaWithPatch() throws Exception {
        // Initialize the database
        searchCriteriaRepository.saveAndFlush(searchCriteria);

        int databaseSizeBeforeUpdate = searchCriteriaRepository.findAll().size();

        // Update the searchCriteria using partial update
        SearchCriteria partialUpdatedSearchCriteria = new SearchCriteria();
        partialUpdatedSearchCriteria.setId(searchCriteria.getId());

        partialUpdatedSearchCriteria
            .filterType(UPDATED_FILTER_TYPE)
            .breed(UPDATED_BREED)
            .tradePups(UPDATED_TRADE_PUPS)
            .sex(UPDATED_SEX)
            .pedigree(UPDATED_PEDIGREE)
            .tradeMoney(UPDATED_TRADE_MONEY)
            .provice(UPDATED_PROVICE)
            .canton(UPDATED_CANTON)
            .district(UPDATED_DISTRICT)
            .objective(UPDATED_OBJECTIVE);

        restSearchCriteriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearchCriteria.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSearchCriteria))
            )
            .andExpect(status().isOk());

        // Validate the SearchCriteria in the database
        List<SearchCriteria> searchCriteriaList = searchCriteriaRepository.findAll();
        assertThat(searchCriteriaList).hasSize(databaseSizeBeforeUpdate);
        SearchCriteria testSearchCriteria = searchCriteriaList.get(searchCriteriaList.size() - 1);
        assertThat(testSearchCriteria.getFilterType()).isEqualTo(UPDATED_FILTER_TYPE);
        assertThat(testSearchCriteria.getBreed()).isEqualTo(UPDATED_BREED);
        assertThat(testSearchCriteria.getTradePups()).isEqualTo(UPDATED_TRADE_PUPS);
        assertThat(testSearchCriteria.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testSearchCriteria.getPedigree()).isEqualTo(UPDATED_PEDIGREE);
        assertThat(testSearchCriteria.getTradeMoney()).isEqualTo(UPDATED_TRADE_MONEY);
        assertThat(testSearchCriteria.getProvice()).isEqualTo(UPDATED_PROVICE);
        assertThat(testSearchCriteria.getCanton()).isEqualTo(UPDATED_CANTON);
        assertThat(testSearchCriteria.getDistrict()).isEqualTo(UPDATED_DISTRICT);
        assertThat(testSearchCriteria.getObjective()).isEqualTo(UPDATED_OBJECTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingSearchCriteria() throws Exception {
        int databaseSizeBeforeUpdate = searchCriteriaRepository.findAll().size();
        searchCriteria.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchCriteriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, searchCriteria.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(searchCriteria))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchCriteria in the database
        List<SearchCriteria> searchCriteriaList = searchCriteriaRepository.findAll();
        assertThat(searchCriteriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSearchCriteria() throws Exception {
        int databaseSizeBeforeUpdate = searchCriteriaRepository.findAll().size();
        searchCriteria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchCriteriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(searchCriteria))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchCriteria in the database
        List<SearchCriteria> searchCriteriaList = searchCriteriaRepository.findAll();
        assertThat(searchCriteriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSearchCriteria() throws Exception {
        int databaseSizeBeforeUpdate = searchCriteriaRepository.findAll().size();
        searchCriteria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchCriteriaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(searchCriteria))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchCriteria in the database
        List<SearchCriteria> searchCriteriaList = searchCriteriaRepository.findAll();
        assertThat(searchCriteriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSearchCriteria() throws Exception {
        // Initialize the database
        searchCriteriaRepository.saveAndFlush(searchCriteria);

        int databaseSizeBeforeDelete = searchCriteriaRepository.findAll().size();

        // Delete the searchCriteria
        restSearchCriteriaMockMvc
            .perform(delete(ENTITY_API_URL_ID, searchCriteria.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SearchCriteria> searchCriteriaList = searchCriteriaRepository.findAll();
        assertThat(searchCriteriaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

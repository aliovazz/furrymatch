package furrymatch.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import furrymatch.IntegrationTest;
import furrymatch.domain.Contract;
import furrymatch.repository.ContractRepository;
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
 * Integration tests for the {@link ContractResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContractResourceIT {

    private static final String DEFAULT_TRADE_MONEY = "AAAAAAAAAA";
    private static final String UPDATED_TRADE_MONEY = "BBBBBBBBBB";

    private static final String DEFAULT_TRADE_PUPS = "AAAAAAAAAA";
    private static final String UPDATED_TRADE_PUPS = "BBBBBBBBBB";

    private static final String DEFAULT_PEDIGREE = "AAAAAAAAAA";
    private static final String UPDATED_PEDIGREE = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_NOTES = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CONTRACT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CONTRACT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractMockMvc;

    private Contract contract;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createEntity(EntityManager em) {
        Contract contract = new Contract()
            .tradeMoney(DEFAULT_TRADE_MONEY)
            .tradePups(DEFAULT_TRADE_PUPS)
            .pedigree(DEFAULT_PEDIGREE)
            .otherNotes(DEFAULT_OTHER_NOTES)
            .contractDate(DEFAULT_CONTRACT_DATE);
        return contract;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createUpdatedEntity(EntityManager em) {
        Contract contract = new Contract()
            .tradeMoney(UPDATED_TRADE_MONEY)
            .tradePups(UPDATED_TRADE_PUPS)
            .pedigree(UPDATED_PEDIGREE)
            .otherNotes(UPDATED_OTHER_NOTES)
            .contractDate(UPDATED_CONTRACT_DATE);
        return contract;
    }

    @BeforeEach
    public void initTest() {
        contract = createEntity(em);
    }

    @Test
    @Transactional
    void createContract() throws Exception {
        int databaseSizeBeforeCreate = contractRepository.findAll().size();
        // Create the Contract
        restContractMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contract))
            )
            .andExpect(status().isCreated());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate + 1);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getTradeMoney()).isEqualTo(DEFAULT_TRADE_MONEY);
        assertThat(testContract.getTradePups()).isEqualTo(DEFAULT_TRADE_PUPS);
        assertThat(testContract.getPedigree()).isEqualTo(DEFAULT_PEDIGREE);
        assertThat(testContract.getOtherNotes()).isEqualTo(DEFAULT_OTHER_NOTES);
        assertThat(testContract.getContractDate()).isEqualTo(DEFAULT_CONTRACT_DATE);
    }

    @Test
    @Transactional
    void createContractWithExistingId() throws Exception {
        // Create the Contract with an existing ID
        contract.setId(1L);

        int databaseSizeBeforeCreate = contractRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllContracts() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get all the contractList
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].tradeMoney").value(hasItem(DEFAULT_TRADE_MONEY)))
            .andExpect(jsonPath("$.[*].tradePups").value(hasItem(DEFAULT_TRADE_PUPS)))
            .andExpect(jsonPath("$.[*].pedigree").value(hasItem(DEFAULT_PEDIGREE)))
            .andExpect(jsonPath("$.[*].otherNotes").value(hasItem(DEFAULT_OTHER_NOTES)))
            .andExpect(jsonPath("$.[*].contractDate").value(hasItem(DEFAULT_CONTRACT_DATE.toString())));
    }

    @Test
    @Transactional
    void getContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        // Get the contract
        restContractMockMvc
            .perform(get(ENTITY_API_URL_ID, contract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contract.getId().intValue()))
            .andExpect(jsonPath("$.tradeMoney").value(DEFAULT_TRADE_MONEY))
            .andExpect(jsonPath("$.tradePups").value(DEFAULT_TRADE_PUPS))
            .andExpect(jsonPath("$.pedigree").value(DEFAULT_PEDIGREE))
            .andExpect(jsonPath("$.otherNotes").value(DEFAULT_OTHER_NOTES))
            .andExpect(jsonPath("$.contractDate").value(DEFAULT_CONTRACT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingContract() throws Exception {
        // Get the contract
        restContractMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract
        Contract updatedContract = contractRepository.findById(contract.getId()).get();
        // Disconnect from session so that the updates on updatedContract are not directly saved in db
        em.detach(updatedContract);
        updatedContract
            .tradeMoney(UPDATED_TRADE_MONEY)
            .tradePups(UPDATED_TRADE_PUPS)
            .pedigree(UPDATED_PEDIGREE)
            .otherNotes(UPDATED_OTHER_NOTES)
            .contractDate(UPDATED_CONTRACT_DATE);

        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedContract.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getTradeMoney()).isEqualTo(UPDATED_TRADE_MONEY);
        assertThat(testContract.getTradePups()).isEqualTo(UPDATED_TRADE_PUPS);
        assertThat(testContract.getPedigree()).isEqualTo(UPDATED_PEDIGREE);
        assertThat(testContract.getOtherNotes()).isEqualTo(UPDATED_OTHER_NOTES);
        assertThat(testContract.getContractDate()).isEqualTo(UPDATED_CONTRACT_DATE);
    }

    @Test
    @Transactional
    void putNonExistingContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contract.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contract))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContractWithPatch() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract.tradePups(UPDATED_TRADE_PUPS).pedigree(UPDATED_PEDIGREE);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getTradeMoney()).isEqualTo(DEFAULT_TRADE_MONEY);
        assertThat(testContract.getTradePups()).isEqualTo(UPDATED_TRADE_PUPS);
        assertThat(testContract.getPedigree()).isEqualTo(UPDATED_PEDIGREE);
        assertThat(testContract.getOtherNotes()).isEqualTo(DEFAULT_OTHER_NOTES);
        assertThat(testContract.getContractDate()).isEqualTo(DEFAULT_CONTRACT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateContractWithPatch() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeUpdate = contractRepository.findAll().size();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract
            .tradeMoney(UPDATED_TRADE_MONEY)
            .tradePups(UPDATED_TRADE_PUPS)
            .pedigree(UPDATED_PEDIGREE)
            .otherNotes(UPDATED_OTHER_NOTES)
            .contractDate(UPDATED_CONTRACT_DATE);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
        Contract testContract = contractList.get(contractList.size() - 1);
        assertThat(testContract.getTradeMoney()).isEqualTo(UPDATED_TRADE_MONEY);
        assertThat(testContract.getTradePups()).isEqualTo(UPDATED_TRADE_PUPS);
        assertThat(testContract.getPedigree()).isEqualTo(UPDATED_PEDIGREE);
        assertThat(testContract.getOtherNotes()).isEqualTo(UPDATED_OTHER_NOTES);
        assertThat(testContract.getContractDate()).isEqualTo(UPDATED_CONTRACT_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contract.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contract))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContract() throws Exception {
        int databaseSizeBeforeUpdate = contractRepository.findAll().size();
        contract.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contract))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContract() throws Exception {
        // Initialize the database
        contractRepository.saveAndFlush(contract);

        int databaseSizeBeforeDelete = contractRepository.findAll().size();

        // Delete the contract
        restContractMockMvc
            .perform(delete(ENTITY_API_URL_ID, contract.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Contract> contractList = contractRepository.findAll();
        assertThat(contractList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

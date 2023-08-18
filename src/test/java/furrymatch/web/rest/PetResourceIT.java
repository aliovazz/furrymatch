package furrymatch.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import furrymatch.IntegrationTest;
import furrymatch.domain.Pet;
import furrymatch.domain.enumeration.PetType;
import furrymatch.domain.enumeration.Sex;
import furrymatch.repository.PetRepository;
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
 * Integration tests for the {@link PetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PetResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final PetType DEFAULT_PET_TYPE = PetType.Perro;
    private static final PetType UPDATED_PET_TYPE = PetType.Gato;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Sex DEFAULT_SEX = Sex.Hembra;
    private static final Sex UPDATED_SEX = Sex.Macho;

    private static final Boolean DEFAULT_TRADE_MONEY = false;
    private static final Boolean UPDATED_TRADE_MONEY = true;

    private static final Boolean DEFAULT_TRADE_PUPS = false;
    private static final Boolean UPDATED_TRADE_PUPS = true;

    private static final Boolean DEFAULT_PEDIGREE = false;
    private static final Boolean UPDATED_PEDIGREE = true;

    private static final Double DEFAULT_DESIRE_AMMOUNT = 1D;
    private static final Double UPDATED_DESIRE_AMMOUNT = 2D;

    private static final String ENTITY_API_URL = "/api/pets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPetMockMvc;

    private Pet pet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pet createEntity(EntityManager em) {
        Pet pet = new Pet()
            .name(DEFAULT_NAME)
            .petType(DEFAULT_PET_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .sex(DEFAULT_SEX)
            .tradeMoney(DEFAULT_TRADE_MONEY)
            .tradePups(DEFAULT_TRADE_PUPS)
            .pedigree(DEFAULT_PEDIGREE)
            .desireAmmount(DEFAULT_DESIRE_AMMOUNT);
        return pet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pet createUpdatedEntity(EntityManager em) {
        Pet pet = new Pet()
            .name(UPDATED_NAME)
            .petType(UPDATED_PET_TYPE)
            .description(UPDATED_DESCRIPTION)
            .sex(UPDATED_SEX)
            .tradeMoney(UPDATED_TRADE_MONEY)
            .tradePups(UPDATED_TRADE_PUPS)
            .pedigree(UPDATED_PEDIGREE)
            .desireAmmount(UPDATED_DESIRE_AMMOUNT);
        return pet;
    }

    @BeforeEach
    public void initTest() {
        pet = createEntity(em);
    }

    @Test
    @Transactional
    void createPet() throws Exception {
        int databaseSizeBeforeCreate = petRepository.findAll().size();
        // Create the Pet
        restPetMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pet))
            )
            .andExpect(status().isCreated());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeCreate + 1);
        Pet testPet = petList.get(petList.size() - 1);
        assertThat(testPet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPet.getPetType()).isEqualTo(DEFAULT_PET_TYPE);
        assertThat(testPet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPet.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testPet.getTradeMoney()).isEqualTo(DEFAULT_TRADE_MONEY);
        assertThat(testPet.getTradePups()).isEqualTo(DEFAULT_TRADE_PUPS);
        assertThat(testPet.getPedigree()).isEqualTo(DEFAULT_PEDIGREE);
        assertThat(testPet.getDesireAmmount()).isEqualTo(DEFAULT_DESIRE_AMMOUNT);
    }

    @Test
    @Transactional
    void createPetWithExistingId() throws Exception {
        // Create the Pet with an existing ID
        pet.setId(1L);

        int databaseSizeBeforeCreate = petRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPetMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = petRepository.findAll().size();
        // set the field null
        pet.setName(null);

        // Create the Pet, which fails.

        restPetMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pet))
            )
            .andExpect(status().isBadRequest());

        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPetTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = petRepository.findAll().size();
        // set the field null
        pet.setPetType(null);

        // Create the Pet, which fails.

        restPetMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pet))
            )
            .andExpect(status().isBadRequest());

        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = petRepository.findAll().size();
        // set the field null
        pet.setDescription(null);

        // Create the Pet, which fails.

        restPetMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pet))
            )
            .andExpect(status().isBadRequest());

        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSexIsRequired() throws Exception {
        int databaseSizeBeforeTest = petRepository.findAll().size();
        // set the field null
        pet.setSex(null);

        // Create the Pet, which fails.

        restPetMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pet))
            )
            .andExpect(status().isBadRequest());

        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPets() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get all the petList
        restPetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pet.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].petType").value(hasItem(DEFAULT_PET_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())))
            .andExpect(jsonPath("$.[*].tradeMoney").value(hasItem(DEFAULT_TRADE_MONEY.booleanValue())))
            .andExpect(jsonPath("$.[*].tradePups").value(hasItem(DEFAULT_TRADE_PUPS.booleanValue())))
            .andExpect(jsonPath("$.[*].pedigree").value(hasItem(DEFAULT_PEDIGREE.booleanValue())))
            .andExpect(jsonPath("$.[*].desireAmmount").value(hasItem(DEFAULT_DESIRE_AMMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    void getPet() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        // Get the pet
        restPetMockMvc
            .perform(get(ENTITY_API_URL_ID, pet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pet.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.petType").value(DEFAULT_PET_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.toString()))
            .andExpect(jsonPath("$.tradeMoney").value(DEFAULT_TRADE_MONEY.booleanValue()))
            .andExpect(jsonPath("$.tradePups").value(DEFAULT_TRADE_PUPS.booleanValue()))
            .andExpect(jsonPath("$.pedigree").value(DEFAULT_PEDIGREE.booleanValue()))
            .andExpect(jsonPath("$.desireAmmount").value(DEFAULT_DESIRE_AMMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingPet() throws Exception {
        // Get the pet
        restPetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPet() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        int databaseSizeBeforeUpdate = petRepository.findAll().size();

        // Update the pet
        Pet updatedPet = petRepository.findById(pet.getId()).get();
        // Disconnect from session so that the updates on updatedPet are not directly saved in db
        em.detach(updatedPet);
        updatedPet
            .name(UPDATED_NAME)
            .petType(UPDATED_PET_TYPE)
            .description(UPDATED_DESCRIPTION)
            .sex(UPDATED_SEX)
            .tradeMoney(UPDATED_TRADE_MONEY)
            .tradePups(UPDATED_TRADE_PUPS)
            .pedigree(UPDATED_PEDIGREE)
            .desireAmmount(UPDATED_DESIRE_AMMOUNT);

        restPetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPet.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPet))
            )
            .andExpect(status().isOk());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
        Pet testPet = petList.get(petList.size() - 1);
        assertThat(testPet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPet.getPetType()).isEqualTo(UPDATED_PET_TYPE);
        assertThat(testPet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPet.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testPet.getTradeMoney()).isEqualTo(UPDATED_TRADE_MONEY);
        assertThat(testPet.getTradePups()).isEqualTo(UPDATED_TRADE_PUPS);
        assertThat(testPet.getPedigree()).isEqualTo(UPDATED_PEDIGREE);
        assertThat(testPet.getDesireAmmount()).isEqualTo(UPDATED_DESIRE_AMMOUNT);
    }

    @Test
    @Transactional
    void putNonExistingPet() throws Exception {
        int databaseSizeBeforeUpdate = petRepository.findAll().size();
        pet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pet.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPet() throws Exception {
        int databaseSizeBeforeUpdate = petRepository.findAll().size();
        pet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPet() throws Exception {
        int databaseSizeBeforeUpdate = petRepository.findAll().size();
        pet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePetWithPatch() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        int databaseSizeBeforeUpdate = petRepository.findAll().size();

        // Update the pet using partial update
        Pet partialUpdatedPet = new Pet();
        partialUpdatedPet.setId(pet.getId());

        partialUpdatedPet.tradePups(UPDATED_TRADE_PUPS).pedigree(UPDATED_PEDIGREE);

        restPetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPet))
            )
            .andExpect(status().isOk());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
        Pet testPet = petList.get(petList.size() - 1);
        assertThat(testPet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPet.getPetType()).isEqualTo(DEFAULT_PET_TYPE);
        assertThat(testPet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPet.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testPet.getTradeMoney()).isEqualTo(DEFAULT_TRADE_MONEY);
        assertThat(testPet.getTradePups()).isEqualTo(UPDATED_TRADE_PUPS);
        assertThat(testPet.getPedigree()).isEqualTo(UPDATED_PEDIGREE);
        assertThat(testPet.getDesireAmmount()).isEqualTo(DEFAULT_DESIRE_AMMOUNT);
    }

    @Test
    @Transactional
    void fullUpdatePetWithPatch() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        int databaseSizeBeforeUpdate = petRepository.findAll().size();

        // Update the pet using partial update
        Pet partialUpdatedPet = new Pet();
        partialUpdatedPet.setId(pet.getId());

        partialUpdatedPet
            .name(UPDATED_NAME)
            .petType(UPDATED_PET_TYPE)
            .description(UPDATED_DESCRIPTION)
            .sex(UPDATED_SEX)
            .tradeMoney(UPDATED_TRADE_MONEY)
            .tradePups(UPDATED_TRADE_PUPS)
            .pedigree(UPDATED_PEDIGREE)
            .desireAmmount(UPDATED_DESIRE_AMMOUNT);

        restPetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPet))
            )
            .andExpect(status().isOk());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
        Pet testPet = petList.get(petList.size() - 1);
        assertThat(testPet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPet.getPetType()).isEqualTo(UPDATED_PET_TYPE);
        assertThat(testPet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPet.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testPet.getTradeMoney()).isEqualTo(UPDATED_TRADE_MONEY);
        assertThat(testPet.getTradePups()).isEqualTo(UPDATED_TRADE_PUPS);
        assertThat(testPet.getPedigree()).isEqualTo(UPDATED_PEDIGREE);
        assertThat(testPet.getDesireAmmount()).isEqualTo(UPDATED_DESIRE_AMMOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingPet() throws Exception {
        int databaseSizeBeforeUpdate = petRepository.findAll().size();
        pet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPet() throws Exception {
        int databaseSizeBeforeUpdate = petRepository.findAll().size();
        pet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPet() throws Exception {
        int databaseSizeBeforeUpdate = petRepository.findAll().size();
        pet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pet in the database
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePet() throws Exception {
        // Initialize the database
        petRepository.saveAndFlush(pet);

        int databaseSizeBeforeDelete = petRepository.findAll().size();

        // Delete the pet
        restPetMockMvc
            .perform(delete(ENTITY_API_URL_ID, pet.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pet> petList = petRepository.findAll();
        assertThat(petList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

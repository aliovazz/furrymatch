package furrymatch.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import furrymatch.IntegrationTest;
import furrymatch.domain.Owner;
import furrymatch.repository.OwnerRepository;
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
 * Integration tests for the {@link OwnerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OwnerResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SECOND_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SECOND_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SECOND_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SECOND_LAST_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_PHONE_NUMBER = 1L;
    private static final Long UPDATED_PHONE_NUMBER = 2L;

    private static final String DEFAULT_PHOTO = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTITY_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_IDENTITY_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_PROVINCE = "BBBBBBBBBB";

    private static final String DEFAULT_CANTON = "AAAAAAAAAA";
    private static final String UPDATED_CANTON = "BBBBBBBBBB";

    private static final String DEFAULT_DISTRICT = "AAAAAAAAAA";
    private static final String UPDATED_DISTRICT = "BBBBBBBBBB";

    private static final String DEFAULT_OTP = "AAAAAAAAAA";
    private static final String UPDATED_OTP = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/owners";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOwnerMockMvc;

    private Owner owner;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Owner createEntity(EntityManager em) {
        Owner owner = new Owner()
            .firstName(DEFAULT_FIRST_NAME)
            .secondName(DEFAULT_SECOND_NAME)
            .firstLastName(DEFAULT_FIRST_LAST_NAME)
            .secondLastName(DEFAULT_SECOND_LAST_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .photo(DEFAULT_PHOTO)
            .identityNumber(DEFAULT_IDENTITY_NUMBER)
            .address(DEFAULT_ADDRESS)
            .province(DEFAULT_PROVINCE)
            .canton(DEFAULT_CANTON)
            .district(DEFAULT_DISTRICT)
            .otp(DEFAULT_OTP)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return owner;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Owner createUpdatedEntity(EntityManager em) {
        Owner owner = new Owner()
            .firstName(UPDATED_FIRST_NAME)
            .secondName(UPDATED_SECOND_NAME)
            .firstLastName(UPDATED_FIRST_LAST_NAME)
            .secondLastName(UPDATED_SECOND_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .photo(UPDATED_PHOTO)
            .identityNumber(UPDATED_IDENTITY_NUMBER)
            .address(UPDATED_ADDRESS)
            .province(UPDATED_PROVINCE)
            .canton(UPDATED_CANTON)
            .district(UPDATED_DISTRICT)
            .otp(UPDATED_OTP)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return owner;
    }

    @BeforeEach
    public void initTest() {
        owner = createEntity(em);
    }

    @Test
    @Transactional
    void createOwner() throws Exception {
        int databaseSizeBeforeCreate = ownerRepository.findAll().size();
        // Create the Owner
        restOwnerMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(owner))
            )
            .andExpect(status().isCreated());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeCreate + 1);
        Owner testOwner = ownerList.get(ownerList.size() - 1);
        assertThat(testOwner.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testOwner.getSecondName()).isEqualTo(DEFAULT_SECOND_NAME);
        assertThat(testOwner.getFirstLastName()).isEqualTo(DEFAULT_FIRST_LAST_NAME);
        assertThat(testOwner.getSecondLastName()).isEqualTo(DEFAULT_SECOND_LAST_NAME);
        assertThat(testOwner.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testOwner.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testOwner.getIdentityNumber()).isEqualTo(DEFAULT_IDENTITY_NUMBER);
        assertThat(testOwner.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testOwner.getProvince()).isEqualTo(DEFAULT_PROVINCE);
        assertThat(testOwner.getCanton()).isEqualTo(DEFAULT_CANTON);
        assertThat(testOwner.getDistrict()).isEqualTo(DEFAULT_DISTRICT);
        assertThat(testOwner.getOtp()).isEqualTo(DEFAULT_OTP);
        assertThat(testOwner.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testOwner.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createOwnerWithExistingId() throws Exception {
        // Create the Owner with an existing ID
        owner.setId(1L);

        int databaseSizeBeforeCreate = ownerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOwnerMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(owner))
            )
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ownerRepository.findAll().size();
        // set the field null
        owner.setFirstName(null);

        // Create the Owner, which fails.

        restOwnerMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(owner))
            )
            .andExpect(status().isBadRequest());

        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ownerRepository.findAll().size();
        // set the field null
        owner.setFirstLastName(null);

        // Create the Owner, which fails.

        restOwnerMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(owner))
            )
            .andExpect(status().isBadRequest());

        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSecondLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ownerRepository.findAll().size();
        // set the field null
        owner.setSecondLastName(null);

        // Create the Owner, which fails.

        restOwnerMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(owner))
            )
            .andExpect(status().isBadRequest());

        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = ownerRepository.findAll().size();
        // set the field null
        owner.setPhoneNumber(null);

        // Create the Owner, which fails.

        restOwnerMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(owner))
            )
            .andExpect(status().isBadRequest());

        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdentityNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = ownerRepository.findAll().size();
        // set the field null
        owner.setIdentityNumber(null);

        // Create the Owner, which fails.

        restOwnerMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(owner))
            )
            .andExpect(status().isBadRequest());

        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = ownerRepository.findAll().size();
        // set the field null
        owner.setAddress(null);

        // Create the Owner, which fails.

        restOwnerMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(owner))
            )
            .andExpect(status().isBadRequest());

        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProvinceIsRequired() throws Exception {
        int databaseSizeBeforeTest = ownerRepository.findAll().size();
        // set the field null
        owner.setProvince(null);

        // Create the Owner, which fails.

        restOwnerMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(owner))
            )
            .andExpect(status().isBadRequest());

        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCantonIsRequired() throws Exception {
        int databaseSizeBeforeTest = ownerRepository.findAll().size();
        // set the field null
        owner.setCanton(null);

        // Create the Owner, which fails.

        restOwnerMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(owner))
            )
            .andExpect(status().isBadRequest());

        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDistrictIsRequired() throws Exception {
        int databaseSizeBeforeTest = ownerRepository.findAll().size();
        // set the field null
        owner.setDistrict(null);

        // Create the Owner, which fails.

        restOwnerMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(owner))
            )
            .andExpect(status().isBadRequest());

        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOwners() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the ownerList
        restOwnerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(owner.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].secondName").value(hasItem(DEFAULT_SECOND_NAME)))
            .andExpect(jsonPath("$.[*].firstLastName").value(hasItem(DEFAULT_FIRST_LAST_NAME)))
            .andExpect(jsonPath("$.[*].secondLastName").value(hasItem(DEFAULT_SECOND_LAST_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.[*].identityNumber").value(hasItem(DEFAULT_IDENTITY_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].province").value(hasItem(DEFAULT_PROVINCE)))
            .andExpect(jsonPath("$.[*].canton").value(hasItem(DEFAULT_CANTON)))
            .andExpect(jsonPath("$.[*].district").value(hasItem(DEFAULT_DISTRICT)))
            .andExpect(jsonPath("$.[*].otp").value(hasItem(DEFAULT_OTP)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getOwner() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get the owner
        restOwnerMockMvc
            .perform(get(ENTITY_API_URL_ID, owner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(owner.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.secondName").value(DEFAULT_SECOND_NAME))
            .andExpect(jsonPath("$.firstLastName").value(DEFAULT_FIRST_LAST_NAME))
            .andExpect(jsonPath("$.secondLastName").value(DEFAULT_SECOND_LAST_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.intValue()))
            .andExpect(jsonPath("$.photo").value(DEFAULT_PHOTO))
            .andExpect(jsonPath("$.identityNumber").value(DEFAULT_IDENTITY_NUMBER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.province").value(DEFAULT_PROVINCE))
            .andExpect(jsonPath("$.canton").value(DEFAULT_CANTON))
            .andExpect(jsonPath("$.district").value(DEFAULT_DISTRICT))
            .andExpect(jsonPath("$.otp").value(DEFAULT_OTP))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOwner() throws Exception {
        // Get the owner
        restOwnerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOwner() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();

        // Update the owner
        Owner updatedOwner = ownerRepository.findById(owner.getId()).get();
        // Disconnect from session so that the updates on updatedOwner are not directly saved in db
        em.detach(updatedOwner);
        updatedOwner
            .firstName(UPDATED_FIRST_NAME)
            .secondName(UPDATED_SECOND_NAME)
            .firstLastName(UPDATED_FIRST_LAST_NAME)
            .secondLastName(UPDATED_SECOND_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .photo(UPDATED_PHOTO)
            .identityNumber(UPDATED_IDENTITY_NUMBER)
            .address(UPDATED_ADDRESS)
            .province(UPDATED_PROVINCE)
            .canton(UPDATED_CANTON)
            .district(UPDATED_DISTRICT)
            .otp(UPDATED_OTP)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOwner.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOwner))
            )
            .andExpect(status().isOk());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
        Owner testOwner = ownerList.get(ownerList.size() - 1);
        assertThat(testOwner.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testOwner.getSecondName()).isEqualTo(UPDATED_SECOND_NAME);
        assertThat(testOwner.getFirstLastName()).isEqualTo(UPDATED_FIRST_LAST_NAME);
        assertThat(testOwner.getSecondLastName()).isEqualTo(UPDATED_SECOND_LAST_NAME);
        assertThat(testOwner.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testOwner.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testOwner.getIdentityNumber()).isEqualTo(UPDATED_IDENTITY_NUMBER);
        assertThat(testOwner.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOwner.getProvince()).isEqualTo(UPDATED_PROVINCE);
        assertThat(testOwner.getCanton()).isEqualTo(UPDATED_CANTON);
        assertThat(testOwner.getDistrict()).isEqualTo(UPDATED_DISTRICT);
        assertThat(testOwner.getOtp()).isEqualTo(UPDATED_OTP);
        assertThat(testOwner.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOwner.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingOwner() throws Exception {
        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();
        owner.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, owner.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(owner))
            )
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOwner() throws Exception {
        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();
        owner.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(owner))
            )
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOwner() throws Exception {
        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();
        owner.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(owner))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOwnerWithPatch() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();

        // Update the owner using partial update
        Owner partialUpdatedOwner = new Owner();
        partialUpdatedOwner.setId(owner.getId());

        partialUpdatedOwner
            .secondName(UPDATED_SECOND_NAME)
            .firstLastName(UPDATED_FIRST_LAST_NAME)
            .secondLastName(UPDATED_SECOND_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .province(UPDATED_PROVINCE);

        restOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOwner.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOwner))
            )
            .andExpect(status().isOk());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
        Owner testOwner = ownerList.get(ownerList.size() - 1);
        assertThat(testOwner.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testOwner.getSecondName()).isEqualTo(UPDATED_SECOND_NAME);
        assertThat(testOwner.getFirstLastName()).isEqualTo(UPDATED_FIRST_LAST_NAME);
        assertThat(testOwner.getSecondLastName()).isEqualTo(UPDATED_SECOND_LAST_NAME);
        assertThat(testOwner.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testOwner.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testOwner.getIdentityNumber()).isEqualTo(DEFAULT_IDENTITY_NUMBER);
        assertThat(testOwner.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testOwner.getProvince()).isEqualTo(UPDATED_PROVINCE);
        assertThat(testOwner.getCanton()).isEqualTo(DEFAULT_CANTON);
        assertThat(testOwner.getDistrict()).isEqualTo(DEFAULT_DISTRICT);
        assertThat(testOwner.getOtp()).isEqualTo(DEFAULT_OTP);
        assertThat(testOwner.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testOwner.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateOwnerWithPatch() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();

        // Update the owner using partial update
        Owner partialUpdatedOwner = new Owner();
        partialUpdatedOwner.setId(owner.getId());

        partialUpdatedOwner
            .firstName(UPDATED_FIRST_NAME)
            .secondName(UPDATED_SECOND_NAME)
            .firstLastName(UPDATED_FIRST_LAST_NAME)
            .secondLastName(UPDATED_SECOND_LAST_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .photo(UPDATED_PHOTO)
            .identityNumber(UPDATED_IDENTITY_NUMBER)
            .address(UPDATED_ADDRESS)
            .province(UPDATED_PROVINCE)
            .canton(UPDATED_CANTON)
            .district(UPDATED_DISTRICT)
            .otp(UPDATED_OTP)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOwner.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOwner))
            )
            .andExpect(status().isOk());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
        Owner testOwner = ownerList.get(ownerList.size() - 1);
        assertThat(testOwner.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testOwner.getSecondName()).isEqualTo(UPDATED_SECOND_NAME);
        assertThat(testOwner.getFirstLastName()).isEqualTo(UPDATED_FIRST_LAST_NAME);
        assertThat(testOwner.getSecondLastName()).isEqualTo(UPDATED_SECOND_LAST_NAME);
        assertThat(testOwner.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testOwner.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testOwner.getIdentityNumber()).isEqualTo(UPDATED_IDENTITY_NUMBER);
        assertThat(testOwner.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOwner.getProvince()).isEqualTo(UPDATED_PROVINCE);
        assertThat(testOwner.getCanton()).isEqualTo(UPDATED_CANTON);
        assertThat(testOwner.getDistrict()).isEqualTo(UPDATED_DISTRICT);
        assertThat(testOwner.getOtp()).isEqualTo(UPDATED_OTP);
        assertThat(testOwner.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOwner.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingOwner() throws Exception {
        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();
        owner.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, owner.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(owner))
            )
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOwner() throws Exception {
        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();
        owner.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(owner))
            )
            .andExpect(status().isBadRequest());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOwner() throws Exception {
        int databaseSizeBeforeUpdate = ownerRepository.findAll().size();
        owner.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOwnerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(owner))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Owner in the database
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOwner() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        int databaseSizeBeforeDelete = ownerRepository.findAll().size();

        // Delete the owner
        restOwnerMockMvc
            .perform(delete(ENTITY_API_URL_ID, owner.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Owner> ownerList = ownerRepository.findAll();
        assertThat(ownerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

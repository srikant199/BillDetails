package com.evolveback.srikant.web.rest;

import com.evolveback.srikant.BillDetailsApp;

import com.evolveback.srikant.domain.BillDetails;
import com.evolveback.srikant.domain.LineItoms;
import com.evolveback.srikant.repository.BillDetailsRepository;
import com.evolveback.srikant.service.BillDetailsService;
import com.evolveback.srikant.repository.search.BillDetailsSearchRepository;
import com.evolveback.srikant.service.dto.BillDetailsDTO;
import com.evolveback.srikant.service.mapper.BillDetailsMapper;
import com.evolveback.srikant.web.rest.errors.ExceptionTranslator;
import com.evolveback.srikant.service.dto.BillDetailsCriteria;
import com.evolveback.srikant.service.BillDetailsQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.evolveback.srikant.web.rest.TestUtil.sameInstant;
import static com.evolveback.srikant.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BillDetailsResource REST controller.
 *
 * @see BillDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BillDetailsApp.class)
public class BillDetailsResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_BILL_NUMBER = 1L;
    private static final Long UPDATED_BILL_NUMBER = 2L;

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    @Autowired
    private BillDetailsRepository billDetailsRepository;

    @Autowired
    private BillDetailsMapper billDetailsMapper;

    @Autowired
    private BillDetailsService billDetailsService;

    @Autowired
    private BillDetailsSearchRepository billDetailsSearchRepository;

    @Autowired
    private BillDetailsQueryService billDetailsQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBillDetailsMockMvc;

    private BillDetails billDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BillDetailsResource billDetailsResource = new BillDetailsResource(billDetailsService, billDetailsQueryService);
        this.restBillDetailsMockMvc = MockMvcBuilders.standaloneSetup(billDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillDetails createEntity(EntityManager em) {
        BillDetails billDetails = new BillDetails()
            .date(DEFAULT_DATE)
            .name(DEFAULT_NAME)
            .billNumber(DEFAULT_BILL_NUMBER)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
        return billDetails;
    }

    @Before
    public void initTest() {
        billDetailsSearchRepository.deleteAll();
        billDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createBillDetails() throws Exception {
        int databaseSizeBeforeCreate = billDetailsRepository.findAll().size();

        // Create the BillDetails
        BillDetailsDTO billDetailsDTO = billDetailsMapper.toDto(billDetails);
        restBillDetailsMockMvc.perform(post("/api/bill-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the BillDetails in the database
        List<BillDetails> billDetailsList = billDetailsRepository.findAll();
        assertThat(billDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        BillDetails testBillDetails = billDetailsList.get(billDetailsList.size() - 1);
        assertThat(testBillDetails.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testBillDetails.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBillDetails.getBillNumber()).isEqualTo(DEFAULT_BILL_NUMBER);
        assertThat(testBillDetails.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);

        // Validate the BillDetails in Elasticsearch
        BillDetails billDetailsEs = billDetailsSearchRepository.findOne(testBillDetails.getId());
        assertThat(testBillDetails.getDate()).isEqualTo(testBillDetails.getDate());
        assertThat(billDetailsEs).isEqualToIgnoringGivenFields(testBillDetails, "date");
    }

    @Test
    @Transactional
    public void createBillDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = billDetailsRepository.findAll().size();

        // Create the BillDetails with an existing ID
        billDetails.setId(1L);
        BillDetailsDTO billDetailsDTO = billDetailsMapper.toDto(billDetails);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillDetailsMockMvc.perform(post("/api/bill-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billDetailsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BillDetails in the database
        List<BillDetails> billDetailsList = billDetailsRepository.findAll();
        assertThat(billDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBillDetails() throws Exception {
        // Initialize the database
        billDetailsRepository.saveAndFlush(billDetails);

        // Get all the billDetailsList
        restBillDetailsMockMvc.perform(get("/api/bill-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].billNumber").value(hasItem(DEFAULT_BILL_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getBillDetails() throws Exception {
        // Initialize the database
        billDetailsRepository.saveAndFlush(billDetails);

        // Get the billDetails
        restBillDetailsMockMvc.perform(get("/api/bill-details/{id}", billDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(billDetails.getId().intValue()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.billNumber").value(DEFAULT_BILL_NUMBER.intValue()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getAllBillDetailsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        billDetailsRepository.saveAndFlush(billDetails);

        // Get all the billDetailsList where date equals to DEFAULT_DATE
        defaultBillDetailsShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the billDetailsList where date equals to UPDATED_DATE
        defaultBillDetailsShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBillDetailsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        billDetailsRepository.saveAndFlush(billDetails);

        // Get all the billDetailsList where date in DEFAULT_DATE or UPDATED_DATE
        defaultBillDetailsShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the billDetailsList where date equals to UPDATED_DATE
        defaultBillDetailsShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBillDetailsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        billDetailsRepository.saveAndFlush(billDetails);

        // Get all the billDetailsList where date is not null
        defaultBillDetailsShouldBeFound("date.specified=true");

        // Get all the billDetailsList where date is null
        defaultBillDetailsShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllBillDetailsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billDetailsRepository.saveAndFlush(billDetails);

        // Get all the billDetailsList where date greater than or equals to DEFAULT_DATE
        defaultBillDetailsShouldBeFound("date.greaterOrEqualThan=" + DEFAULT_DATE);

        // Get all the billDetailsList where date greater than or equals to UPDATED_DATE
        defaultBillDetailsShouldNotBeFound("date.greaterOrEqualThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllBillDetailsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        billDetailsRepository.saveAndFlush(billDetails);

        // Get all the billDetailsList where date less than or equals to DEFAULT_DATE
        defaultBillDetailsShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the billDetailsList where date less than or equals to UPDATED_DATE
        defaultBillDetailsShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }


    @Test
    @Transactional
    public void getAllBillDetailsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        billDetailsRepository.saveAndFlush(billDetails);

        // Get all the billDetailsList where name equals to DEFAULT_NAME
        defaultBillDetailsShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the billDetailsList where name equals to UPDATED_NAME
        defaultBillDetailsShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBillDetailsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        billDetailsRepository.saveAndFlush(billDetails);

        // Get all the billDetailsList where name in DEFAULT_NAME or UPDATED_NAME
        defaultBillDetailsShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the billDetailsList where name equals to UPDATED_NAME
        defaultBillDetailsShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllBillDetailsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        billDetailsRepository.saveAndFlush(billDetails);

        // Get all the billDetailsList where name is not null
        defaultBillDetailsShouldBeFound("name.specified=true");

        // Get all the billDetailsList where name is null
        defaultBillDetailsShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllBillDetailsByBillNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        billDetailsRepository.saveAndFlush(billDetails);

        // Get all the billDetailsList where billNumber equals to DEFAULT_BILL_NUMBER
        defaultBillDetailsShouldBeFound("billNumber.equals=" + DEFAULT_BILL_NUMBER);

        // Get all the billDetailsList where billNumber equals to UPDATED_BILL_NUMBER
        defaultBillDetailsShouldNotBeFound("billNumber.equals=" + UPDATED_BILL_NUMBER);
    }

    @Test
    @Transactional
    public void getAllBillDetailsByBillNumberIsInShouldWork() throws Exception {
        // Initialize the database
        billDetailsRepository.saveAndFlush(billDetails);

        // Get all the billDetailsList where billNumber in DEFAULT_BILL_NUMBER or UPDATED_BILL_NUMBER
        defaultBillDetailsShouldBeFound("billNumber.in=" + DEFAULT_BILL_NUMBER + "," + UPDATED_BILL_NUMBER);

        // Get all the billDetailsList where billNumber equals to UPDATED_BILL_NUMBER
        defaultBillDetailsShouldNotBeFound("billNumber.in=" + UPDATED_BILL_NUMBER);
    }

    @Test
    @Transactional
    public void getAllBillDetailsByBillNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        billDetailsRepository.saveAndFlush(billDetails);

        // Get all the billDetailsList where billNumber is not null
        defaultBillDetailsShouldBeFound("billNumber.specified=true");

        // Get all the billDetailsList where billNumber is null
        defaultBillDetailsShouldNotBeFound("billNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllBillDetailsByBillNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        billDetailsRepository.saveAndFlush(billDetails);

        // Get all the billDetailsList where billNumber greater than or equals to DEFAULT_BILL_NUMBER
        defaultBillDetailsShouldBeFound("billNumber.greaterOrEqualThan=" + DEFAULT_BILL_NUMBER);

        // Get all the billDetailsList where billNumber greater than or equals to UPDATED_BILL_NUMBER
        defaultBillDetailsShouldNotBeFound("billNumber.greaterOrEqualThan=" + UPDATED_BILL_NUMBER);
    }

    @Test
    @Transactional
    public void getAllBillDetailsByBillNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        billDetailsRepository.saveAndFlush(billDetails);

        // Get all the billDetailsList where billNumber less than or equals to DEFAULT_BILL_NUMBER
        defaultBillDetailsShouldNotBeFound("billNumber.lessThan=" + DEFAULT_BILL_NUMBER);

        // Get all the billDetailsList where billNumber less than or equals to UPDATED_BILL_NUMBER
        defaultBillDetailsShouldBeFound("billNumber.lessThan=" + UPDATED_BILL_NUMBER);
    }


    @Test
    @Transactional
    public void getAllBillDetailsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        billDetailsRepository.saveAndFlush(billDetails);

        // Get all the billDetailsList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultBillDetailsShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the billDetailsList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultBillDetailsShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllBillDetailsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        billDetailsRepository.saveAndFlush(billDetails);

        // Get all the billDetailsList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultBillDetailsShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the billDetailsList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultBillDetailsShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllBillDetailsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        billDetailsRepository.saveAndFlush(billDetails);

        // Get all the billDetailsList where phoneNumber is not null
        defaultBillDetailsShouldBeFound("phoneNumber.specified=true");

        // Get all the billDetailsList where phoneNumber is null
        defaultBillDetailsShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllBillDetailsByLineItomsIsEqualToSomething() throws Exception {
        // Initialize the database
        LineItoms lineItoms = LineItomsResourceIntTest.createEntity(em);
        em.persist(lineItoms);
        em.flush();
        billDetails.addLineItoms(lineItoms);
        billDetailsRepository.saveAndFlush(billDetails);
        Long lineItomsId = lineItoms.getId();

        // Get all the billDetailsList where lineItoms equals to lineItomsId
        defaultBillDetailsShouldBeFound("lineItomsId.equals=" + lineItomsId);

        // Get all the billDetailsList where lineItoms equals to lineItomsId + 1
        defaultBillDetailsShouldNotBeFound("lineItomsId.equals=" + (lineItomsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultBillDetailsShouldBeFound(String filter) throws Exception {
        restBillDetailsMockMvc.perform(get("/api/bill-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].billNumber").value(hasItem(DEFAULT_BILL_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultBillDetailsShouldNotBeFound(String filter) throws Exception {
        restBillDetailsMockMvc.perform(get("/api/bill-details?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingBillDetails() throws Exception {
        // Get the billDetails
        restBillDetailsMockMvc.perform(get("/api/bill-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBillDetails() throws Exception {
        // Initialize the database
        billDetailsRepository.saveAndFlush(billDetails);
        billDetailsSearchRepository.save(billDetails);
        int databaseSizeBeforeUpdate = billDetailsRepository.findAll().size();

        // Update the billDetails
        BillDetails updatedBillDetails = billDetailsRepository.findOne(billDetails.getId());
        // Disconnect from session so that the updates on updatedBillDetails are not directly saved in db
        em.detach(updatedBillDetails);
        updatedBillDetails
            .date(UPDATED_DATE)
            .name(UPDATED_NAME)
            .billNumber(UPDATED_BILL_NUMBER)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        BillDetailsDTO billDetailsDTO = billDetailsMapper.toDto(updatedBillDetails);

        restBillDetailsMockMvc.perform(put("/api/bill-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billDetailsDTO)))
            .andExpect(status().isOk());

        // Validate the BillDetails in the database
        List<BillDetails> billDetailsList = billDetailsRepository.findAll();
        assertThat(billDetailsList).hasSize(databaseSizeBeforeUpdate);
        BillDetails testBillDetails = billDetailsList.get(billDetailsList.size() - 1);
        assertThat(testBillDetails.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testBillDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBillDetails.getBillNumber()).isEqualTo(UPDATED_BILL_NUMBER);
        assertThat(testBillDetails.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);

        // Validate the BillDetails in Elasticsearch
        BillDetails billDetailsEs = billDetailsSearchRepository.findOne(testBillDetails.getId());
        assertThat(testBillDetails.getDate()).isEqualTo(testBillDetails.getDate());
        assertThat(billDetailsEs).isEqualToIgnoringGivenFields(testBillDetails, "date");
    }

    @Test
    @Transactional
    public void updateNonExistingBillDetails() throws Exception {
        int databaseSizeBeforeUpdate = billDetailsRepository.findAll().size();

        // Create the BillDetails
        BillDetailsDTO billDetailsDTO = billDetailsMapper.toDto(billDetails);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBillDetailsMockMvc.perform(put("/api/bill-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billDetailsDTO)))
            .andExpect(status().isCreated());

        // Validate the BillDetails in the database
        List<BillDetails> billDetailsList = billDetailsRepository.findAll();
        assertThat(billDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBillDetails() throws Exception {
        // Initialize the database
        billDetailsRepository.saveAndFlush(billDetails);
        billDetailsSearchRepository.save(billDetails);
        int databaseSizeBeforeDelete = billDetailsRepository.findAll().size();

        // Get the billDetails
        restBillDetailsMockMvc.perform(delete("/api/bill-details/{id}", billDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean billDetailsExistsInEs = billDetailsSearchRepository.exists(billDetails.getId());
        assertThat(billDetailsExistsInEs).isFalse();

        // Validate the database is empty
        List<BillDetails> billDetailsList = billDetailsRepository.findAll();
        assertThat(billDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBillDetails() throws Exception {
        // Initialize the database
        billDetailsRepository.saveAndFlush(billDetails);
        billDetailsSearchRepository.save(billDetails);

        // Search the billDetails
        restBillDetailsMockMvc.perform(get("/api/_search/bill-details?query=id:" + billDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].billNumber").value(hasItem(DEFAULT_BILL_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillDetails.class);
        BillDetails billDetails1 = new BillDetails();
        billDetails1.setId(1L);
        BillDetails billDetails2 = new BillDetails();
        billDetails2.setId(billDetails1.getId());
        assertThat(billDetails1).isEqualTo(billDetails2);
        billDetails2.setId(2L);
        assertThat(billDetails1).isNotEqualTo(billDetails2);
        billDetails1.setId(null);
        assertThat(billDetails1).isNotEqualTo(billDetails2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillDetailsDTO.class);
        BillDetailsDTO billDetailsDTO1 = new BillDetailsDTO();
        billDetailsDTO1.setId(1L);
        BillDetailsDTO billDetailsDTO2 = new BillDetailsDTO();
        assertThat(billDetailsDTO1).isNotEqualTo(billDetailsDTO2);
        billDetailsDTO2.setId(billDetailsDTO1.getId());
        assertThat(billDetailsDTO1).isEqualTo(billDetailsDTO2);
        billDetailsDTO2.setId(2L);
        assertThat(billDetailsDTO1).isNotEqualTo(billDetailsDTO2);
        billDetailsDTO1.setId(null);
        assertThat(billDetailsDTO1).isNotEqualTo(billDetailsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(billDetailsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(billDetailsMapper.fromId(null)).isNull();
    }
}

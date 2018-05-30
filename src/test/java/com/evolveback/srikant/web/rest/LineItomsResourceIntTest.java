package com.evolveback.srikant.web.rest;

import com.evolveback.srikant.BillDetailsApp;

import com.evolveback.srikant.domain.LineItoms;
import com.evolveback.srikant.repository.LineItomsRepository;
import com.evolveback.srikant.service.LineItomsService;
import com.evolveback.srikant.repository.search.LineItomsSearchRepository;
import com.evolveback.srikant.service.dto.LineItomsDTO;
import com.evolveback.srikant.service.mapper.LineItomsMapper;
import com.evolveback.srikant.web.rest.errors.ExceptionTranslator;

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
import java.util.List;

import static com.evolveback.srikant.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LineItomsResource REST controller.
 *
 * @see LineItomsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BillDetailsApp.class)
public class LineItomsResourceIntTest {

    private static final Long DEFAULT_SERIAL_NUMBER = 1L;
    private static final Long UPDATED_SERIAL_NUMBER = 2L;

    private static final String DEFAULT_PARTICULAR = "AAAAAAAAAA";
    private static final String UPDATED_PARTICULAR = "BBBBBBBBBB";

    private static final Integer DEFAULT_AMOUNTS = 1;
    private static final Integer UPDATED_AMOUNTS = 2;

    @Autowired
    private LineItomsRepository lineItomsRepository;

    @Autowired
    private LineItomsMapper lineItomsMapper;

    @Autowired
    private LineItomsService lineItomsService;

    @Autowired
    private LineItomsSearchRepository lineItomsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLineItomsMockMvc;

    private LineItoms lineItoms;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LineItomsResource lineItomsResource = new LineItomsResource(lineItomsService);
        this.restLineItomsMockMvc = MockMvcBuilders.standaloneSetup(lineItomsResource)
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
    public static LineItoms createEntity(EntityManager em) {
        LineItoms lineItoms = new LineItoms()
            .serialNumber(DEFAULT_SERIAL_NUMBER)
            .particular(DEFAULT_PARTICULAR)
            .amounts(DEFAULT_AMOUNTS);
        return lineItoms;
    }

    @Before
    public void initTest() {
        lineItomsSearchRepository.deleteAll();
        lineItoms = createEntity(em);
    }

    @Test
    @Transactional
    public void createLineItoms() throws Exception {
        int databaseSizeBeforeCreate = lineItomsRepository.findAll().size();

        // Create the LineItoms
        LineItomsDTO lineItomsDTO = lineItomsMapper.toDto(lineItoms);
        restLineItomsMockMvc.perform(post("/api/line-itoms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineItomsDTO)))
            .andExpect(status().isCreated());

        // Validate the LineItoms in the database
        List<LineItoms> lineItomsList = lineItomsRepository.findAll();
        assertThat(lineItomsList).hasSize(databaseSizeBeforeCreate + 1);
        LineItoms testLineItoms = lineItomsList.get(lineItomsList.size() - 1);
        assertThat(testLineItoms.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testLineItoms.getParticular()).isEqualTo(DEFAULT_PARTICULAR);
        assertThat(testLineItoms.getAmounts()).isEqualTo(DEFAULT_AMOUNTS);

        // Validate the LineItoms in Elasticsearch
        LineItoms lineItomsEs = lineItomsSearchRepository.findOne(testLineItoms.getId());
        assertThat(lineItomsEs).isEqualToIgnoringGivenFields(testLineItoms);
    }

    @Test
    @Transactional
    public void createLineItomsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lineItomsRepository.findAll().size();

        // Create the LineItoms with an existing ID
        lineItoms.setId(1L);
        LineItomsDTO lineItomsDTO = lineItomsMapper.toDto(lineItoms);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLineItomsMockMvc.perform(post("/api/line-itoms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineItomsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LineItoms in the database
        List<LineItoms> lineItomsList = lineItomsRepository.findAll();
        assertThat(lineItomsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLineItoms() throws Exception {
        // Initialize the database
        lineItomsRepository.saveAndFlush(lineItoms);

        // Get all the lineItomsList
        restLineItomsMockMvc.perform(get("/api/line-itoms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lineItoms.getId().intValue())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].particular").value(hasItem(DEFAULT_PARTICULAR.toString())))
            .andExpect(jsonPath("$.[*].amounts").value(hasItem(DEFAULT_AMOUNTS)));
    }

    @Test
    @Transactional
    public void getLineItoms() throws Exception {
        // Initialize the database
        lineItomsRepository.saveAndFlush(lineItoms);

        // Get the lineItoms
        restLineItomsMockMvc.perform(get("/api/line-itoms/{id}", lineItoms.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lineItoms.getId().intValue()))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER.intValue()))
            .andExpect(jsonPath("$.particular").value(DEFAULT_PARTICULAR.toString()))
            .andExpect(jsonPath("$.amounts").value(DEFAULT_AMOUNTS));
    }

    @Test
    @Transactional
    public void getNonExistingLineItoms() throws Exception {
        // Get the lineItoms
        restLineItomsMockMvc.perform(get("/api/line-itoms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLineItoms() throws Exception {
        // Initialize the database
        lineItomsRepository.saveAndFlush(lineItoms);
        lineItomsSearchRepository.save(lineItoms);
        int databaseSizeBeforeUpdate = lineItomsRepository.findAll().size();

        // Update the lineItoms
        LineItoms updatedLineItoms = lineItomsRepository.findOne(lineItoms.getId());
        // Disconnect from session so that the updates on updatedLineItoms are not directly saved in db
        em.detach(updatedLineItoms);
        updatedLineItoms
            .serialNumber(UPDATED_SERIAL_NUMBER)
            .particular(UPDATED_PARTICULAR)
            .amounts(UPDATED_AMOUNTS);
        LineItomsDTO lineItomsDTO = lineItomsMapper.toDto(updatedLineItoms);

        restLineItomsMockMvc.perform(put("/api/line-itoms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineItomsDTO)))
            .andExpect(status().isOk());

        // Validate the LineItoms in the database
        List<LineItoms> lineItomsList = lineItomsRepository.findAll();
        assertThat(lineItomsList).hasSize(databaseSizeBeforeUpdate);
        LineItoms testLineItoms = lineItomsList.get(lineItomsList.size() - 1);
        assertThat(testLineItoms.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testLineItoms.getParticular()).isEqualTo(UPDATED_PARTICULAR);
        assertThat(testLineItoms.getAmounts()).isEqualTo(UPDATED_AMOUNTS);

        // Validate the LineItoms in Elasticsearch
        LineItoms lineItomsEs = lineItomsSearchRepository.findOne(testLineItoms.getId());
        assertThat(lineItomsEs).isEqualToIgnoringGivenFields(testLineItoms);
    }

    @Test
    @Transactional
    public void updateNonExistingLineItoms() throws Exception {
        int databaseSizeBeforeUpdate = lineItomsRepository.findAll().size();

        // Create the LineItoms
        LineItomsDTO lineItomsDTO = lineItomsMapper.toDto(lineItoms);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLineItomsMockMvc.perform(put("/api/line-itoms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineItomsDTO)))
            .andExpect(status().isCreated());

        // Validate the LineItoms in the database
        List<LineItoms> lineItomsList = lineItomsRepository.findAll();
        assertThat(lineItomsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLineItoms() throws Exception {
        // Initialize the database
        lineItomsRepository.saveAndFlush(lineItoms);
        lineItomsSearchRepository.save(lineItoms);
        int databaseSizeBeforeDelete = lineItomsRepository.findAll().size();

        // Get the lineItoms
        restLineItomsMockMvc.perform(delete("/api/line-itoms/{id}", lineItoms.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean lineItomsExistsInEs = lineItomsSearchRepository.exists(lineItoms.getId());
        assertThat(lineItomsExistsInEs).isFalse();

        // Validate the database is empty
        List<LineItoms> lineItomsList = lineItomsRepository.findAll();
        assertThat(lineItomsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLineItoms() throws Exception {
        // Initialize the database
        lineItomsRepository.saveAndFlush(lineItoms);
        lineItomsSearchRepository.save(lineItoms);

        // Search the lineItoms
        restLineItomsMockMvc.perform(get("/api/_search/line-itoms?query=id:" + lineItoms.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lineItoms.getId().intValue())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].particular").value(hasItem(DEFAULT_PARTICULAR.toString())))
            .andExpect(jsonPath("$.[*].amounts").value(hasItem(DEFAULT_AMOUNTS)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LineItoms.class);
        LineItoms lineItoms1 = new LineItoms();
        lineItoms1.setId(1L);
        LineItoms lineItoms2 = new LineItoms();
        lineItoms2.setId(lineItoms1.getId());
        assertThat(lineItoms1).isEqualTo(lineItoms2);
        lineItoms2.setId(2L);
        assertThat(lineItoms1).isNotEqualTo(lineItoms2);
        lineItoms1.setId(null);
        assertThat(lineItoms1).isNotEqualTo(lineItoms2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LineItomsDTO.class);
        LineItomsDTO lineItomsDTO1 = new LineItomsDTO();
        lineItomsDTO1.setId(1L);
        LineItomsDTO lineItomsDTO2 = new LineItomsDTO();
        assertThat(lineItomsDTO1).isNotEqualTo(lineItomsDTO2);
        lineItomsDTO2.setId(lineItomsDTO1.getId());
        assertThat(lineItomsDTO1).isEqualTo(lineItomsDTO2);
        lineItomsDTO2.setId(2L);
        assertThat(lineItomsDTO1).isNotEqualTo(lineItomsDTO2);
        lineItomsDTO1.setId(null);
        assertThat(lineItomsDTO1).isNotEqualTo(lineItomsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(lineItomsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(lineItomsMapper.fromId(null)).isNull();
    }
}

package com.evolveback.srikant.service.impl;

import com.evolveback.srikant.service.LineItomsService;
import com.evolveback.srikant.domain.LineItoms;
import com.evolveback.srikant.repository.LineItomsRepository;
import com.evolveback.srikant.repository.search.LineItomsSearchRepository;
import com.evolveback.srikant.service.dto.LineItomsDTO;
import com.evolveback.srikant.service.mapper.LineItomsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing LineItoms.
 */
@Service
@Transactional
public class LineItomsServiceImpl implements LineItomsService {

    private final Logger log = LoggerFactory.getLogger(LineItomsServiceImpl.class);

    private final LineItomsRepository lineItomsRepository;

    private final LineItomsMapper lineItomsMapper;

    private final LineItomsSearchRepository lineItomsSearchRepository;

    public LineItomsServiceImpl(LineItomsRepository lineItomsRepository, LineItomsMapper lineItomsMapper, LineItomsSearchRepository lineItomsSearchRepository) {
        this.lineItomsRepository = lineItomsRepository;
        this.lineItomsMapper = lineItomsMapper;
        this.lineItomsSearchRepository = lineItomsSearchRepository;
    }

    /**
     * Save a lineItoms.
     *
     * @param lineItomsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public LineItomsDTO save(LineItomsDTO lineItomsDTO) {
        log.debug("Request to save LineItoms : {}", lineItomsDTO);
        LineItoms lineItoms = lineItomsMapper.toEntity(lineItomsDTO);
        lineItoms = lineItomsRepository.save(lineItoms);
        LineItomsDTO result = lineItomsMapper.toDto(lineItoms);
        lineItomsSearchRepository.save(lineItoms);
        return result;
    }

    /**
     * Get all the lineItoms.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LineItomsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all LineItoms");
        return lineItomsRepository.findAll(pageable)
            .map(lineItomsMapper::toDto);
    }

    /**
     * Get one lineItoms by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public LineItomsDTO findOne(Long id) {
        log.debug("Request to get LineItoms : {}", id);
        LineItoms lineItoms = lineItomsRepository.findOne(id);
        return lineItomsMapper.toDto(lineItoms);
    }

    /**
     * Delete the lineItoms by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete LineItoms : {}", id);
        lineItomsRepository.delete(id);
        lineItomsSearchRepository.delete(id);
    }

    /**
     * Search for the lineItoms corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LineItomsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of LineItoms for query {}", query);
        Page<LineItoms> result = lineItomsSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(lineItomsMapper::toDto);
    }
}

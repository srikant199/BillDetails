package com.evolveback.srikant.service.impl;

import com.evolveback.srikant.service.BillDetailsService;
import com.evolveback.srikant.domain.BillDetails;
import com.evolveback.srikant.repository.BillDetailsRepository;
import com.evolveback.srikant.repository.search.BillDetailsSearchRepository;
import com.evolveback.srikant.service.dto.BillDetailsDTO;
import com.evolveback.srikant.service.mapper.BillDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing BillDetails.
 */
@Service
@Transactional
public class BillDetailsServiceImpl implements BillDetailsService {

    private final Logger log = LoggerFactory.getLogger(BillDetailsServiceImpl.class);

    private final BillDetailsRepository billDetailsRepository;

    private final BillDetailsMapper billDetailsMapper;

    private final BillDetailsSearchRepository billDetailsSearchRepository;

    public BillDetailsServiceImpl(BillDetailsRepository billDetailsRepository, BillDetailsMapper billDetailsMapper, BillDetailsSearchRepository billDetailsSearchRepository) {
        this.billDetailsRepository = billDetailsRepository;
        this.billDetailsMapper = billDetailsMapper;
        this.billDetailsSearchRepository = billDetailsSearchRepository;
    }

    /**
     * Save a billDetails.
     *
     * @param billDetailsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BillDetailsDTO save(BillDetailsDTO billDetailsDTO) {
        log.debug("Request to save BillDetails : {}", billDetailsDTO);
        BillDetails billDetails = billDetailsMapper.toEntity(billDetailsDTO);
        billDetails = billDetailsRepository.save(billDetails);
        BillDetailsDTO result = billDetailsMapper.toDto(billDetails);
        billDetailsSearchRepository.save(billDetails);
        return result;
    }

    /**
     * Get all the billDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BillDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BillDetails");
        return billDetailsRepository.findAll(pageable)
            .map(billDetailsMapper::toDto);
    }

    /**
     * Get one billDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public BillDetailsDTO findOne(Long id) {
        log.debug("Request to get BillDetails : {}", id);
        BillDetails billDetails = billDetailsRepository.findOne(id);
        return billDetailsMapper.toDto(billDetails);
    }

    /**
     * Delete the billDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete BillDetails : {}", id);
        billDetailsRepository.delete(id);
        billDetailsSearchRepository.delete(id);
    }

    /**
     * Search for the billDetails corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BillDetailsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BillDetails for query {}", query);
        Page<BillDetails> result = billDetailsSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(billDetailsMapper::toDto);
    }
}

package com.evolveback.srikant.service;


import java.time.ZonedDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.evolveback.srikant.domain.BillDetails;
import com.evolveback.srikant.domain.*; // for static metamodels
import com.evolveback.srikant.repository.BillDetailsRepository;
import com.evolveback.srikant.repository.search.BillDetailsSearchRepository;
import com.evolveback.srikant.service.dto.BillDetailsCriteria;

import com.evolveback.srikant.service.dto.BillDetailsDTO;
import com.evolveback.srikant.service.mapper.BillDetailsMapper;

/**
 * Service for executing complex queries for BillDetails entities in the database.
 * The main input is a {@link BillDetailsCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BillDetailsDTO} or a {@link Page} of {@link BillDetailsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BillDetailsQueryService extends QueryService<BillDetails> {

    private final Logger log = LoggerFactory.getLogger(BillDetailsQueryService.class);


    private final BillDetailsRepository billDetailsRepository;

    private final BillDetailsMapper billDetailsMapper;

    private final BillDetailsSearchRepository billDetailsSearchRepository;

    public BillDetailsQueryService(BillDetailsRepository billDetailsRepository, BillDetailsMapper billDetailsMapper, BillDetailsSearchRepository billDetailsSearchRepository) {
        this.billDetailsRepository = billDetailsRepository;
        this.billDetailsMapper = billDetailsMapper;
        this.billDetailsSearchRepository = billDetailsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link BillDetailsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BillDetailsDTO> findByCriteria(BillDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<BillDetails> specification = createSpecification(criteria);
        return billDetailsMapper.toDto(billDetailsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BillDetailsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BillDetailsDTO> findByCriteria(BillDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<BillDetails> specification = createSpecification(criteria);
        final Page<BillDetails> result = billDetailsRepository.findAll(specification, page);
        return result.map(billDetailsMapper::toDto);
    }

    /**
     * Function to convert BillDetailsCriteria to a {@link Specifications}
     */
    private Specifications<BillDetails> createSpecification(BillDetailsCriteria criteria) {
        Specifications<BillDetails> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), BillDetails_.id));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), BillDetails_.date));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), BillDetails_.name));
            }
            if (criteria.getBillNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBillNumber(), BillDetails_.billNumber));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), BillDetails_.phoneNumber));
            }
            if (criteria.getLineItomsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getLineItomsId(), BillDetails_.lineItoms, LineItoms_.id));
            }
        }
        return specification;
    }

}

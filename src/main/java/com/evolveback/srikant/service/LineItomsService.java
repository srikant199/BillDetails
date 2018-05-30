package com.evolveback.srikant.service;

import com.evolveback.srikant.service.dto.LineItomsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing LineItoms.
 */
public interface LineItomsService {

    /**
     * Save a lineItoms.
     *
     * @param lineItomsDTO the entity to save
     * @return the persisted entity
     */
    LineItomsDTO save(LineItomsDTO lineItomsDTO);

    /**
     * Get all the lineItoms.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<LineItomsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" lineItoms.
     *
     * @param id the id of the entity
     * @return the entity
     */
    LineItomsDTO findOne(Long id);

    /**
     * Delete the "id" lineItoms.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the lineItoms corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<LineItomsDTO> search(String query, Pageable pageable);
}

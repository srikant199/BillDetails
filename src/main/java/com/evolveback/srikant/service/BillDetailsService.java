package com.evolveback.srikant.service;

import com.evolveback.srikant.service.dto.BillDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing BillDetails.
 */
public interface BillDetailsService {

    /**
     * Save a billDetails.
     *
     * @param billDetailsDTO the entity to save
     * @return the persisted entity
     */
    BillDetailsDTO save(BillDetailsDTO billDetailsDTO);

    /**
     * Get all the billDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BillDetailsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" billDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    BillDetailsDTO findOne(Long id);

    /**
     * Delete the "id" billDetails.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the billDetails corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BillDetailsDTO> search(String query, Pageable pageable);
}

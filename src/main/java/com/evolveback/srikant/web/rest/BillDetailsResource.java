package com.evolveback.srikant.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.evolveback.srikant.service.BillDetailsService;
import com.evolveback.srikant.web.rest.errors.BadRequestAlertException;
import com.evolveback.srikant.web.rest.util.HeaderUtil;
import com.evolveback.srikant.web.rest.util.PaginationUtil;
import com.evolveback.srikant.service.dto.BillDetailsDTO;
import com.evolveback.srikant.service.dto.BillDetailsCriteria;
import com.evolveback.srikant.service.BillDetailsQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing BillDetails.
 */
@RestController
@RequestMapping("/api")
public class BillDetailsResource {

    private final Logger log = LoggerFactory.getLogger(BillDetailsResource.class);

    private static final String ENTITY_NAME = "billDetails";

    private final BillDetailsService billDetailsService;

    private final BillDetailsQueryService billDetailsQueryService;

    public BillDetailsResource(BillDetailsService billDetailsService, BillDetailsQueryService billDetailsQueryService) {
        this.billDetailsService = billDetailsService;
        this.billDetailsQueryService = billDetailsQueryService;
    }

    /**
     * POST  /bill-details : Create a new billDetails.
     *
     * @param billDetailsDTO the billDetailsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new billDetailsDTO, or with status 400 (Bad Request) if the billDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bill-details")
    @Timed
    public ResponseEntity<BillDetailsDTO> createBillDetails(@RequestBody BillDetailsDTO billDetailsDTO) throws URISyntaxException {
        log.debug("REST request to save BillDetails : {}", billDetailsDTO);
        if (billDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new billDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BillDetailsDTO result = billDetailsService.save(billDetailsDTO);
        return ResponseEntity.created(new URI("/api/bill-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bill-details : Updates an existing billDetails.
     *
     * @param billDetailsDTO the billDetailsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated billDetailsDTO,
     * or with status 400 (Bad Request) if the billDetailsDTO is not valid,
     * or with status 500 (Internal Server Error) if the billDetailsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bill-details")
    @Timed
    public ResponseEntity<BillDetailsDTO> updateBillDetails(@RequestBody BillDetailsDTO billDetailsDTO) throws URISyntaxException {
        log.debug("REST request to update BillDetails : {}", billDetailsDTO);
        if (billDetailsDTO.getId() == null) {
            return createBillDetails(billDetailsDTO);
        }
        BillDetailsDTO result = billDetailsService.save(billDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, billDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bill-details : get all the billDetails.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of billDetails in body
     */
    @GetMapping("/bill-details")
    @Timed
    public ResponseEntity<List<BillDetailsDTO>> getAllBillDetails(BillDetailsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BillDetails by criteria: {}", criteria);
        Page<BillDetailsDTO> page = billDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bill-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bill-details/:id : get the "id" billDetails.
     *
     * @param id the id of the billDetailsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the billDetailsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/bill-details/{id}")
    @Timed
    public ResponseEntity<BillDetailsDTO> getBillDetails(@PathVariable Long id) {
        log.debug("REST request to get BillDetails : {}", id);
        BillDetailsDTO billDetailsDTO = billDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(billDetailsDTO));
    }

    /**
     * DELETE  /bill-details/:id : delete the "id" billDetails.
     *
     * @param id the id of the billDetailsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bill-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteBillDetails(@PathVariable Long id) {
        log.debug("REST request to delete BillDetails : {}", id);
        billDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/bill-details?query=:query : search for the billDetails corresponding
     * to the query.
     *
     * @param query the query of the billDetails search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/bill-details")
    @Timed
    public ResponseEntity<List<BillDetailsDTO>> searchBillDetails(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of BillDetails for query {}", query);
        Page<BillDetailsDTO> page = billDetailsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/bill-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}

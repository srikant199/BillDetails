package com.evolveback.srikant.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.evolveback.srikant.service.LineItomsService;
import com.evolveback.srikant.web.rest.errors.BadRequestAlertException;
import com.evolveback.srikant.web.rest.util.HeaderUtil;
import com.evolveback.srikant.web.rest.util.PaginationUtil;
import com.evolveback.srikant.service.dto.LineItomsDTO;
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
 * REST controller for managing LineItoms.
 */
@RestController
@RequestMapping("/api")
public class LineItomsResource {

    private final Logger log = LoggerFactory.getLogger(LineItomsResource.class);

    private static final String ENTITY_NAME = "lineItoms";

    private final LineItomsService lineItomsService;

    public LineItomsResource(LineItomsService lineItomsService) {
        this.lineItomsService = lineItomsService;
    }

    /**
     * POST  /line-itoms : Create a new lineItoms.
     *
     * @param lineItomsDTO the lineItomsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lineItomsDTO, or with status 400 (Bad Request) if the lineItoms has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/line-itoms")
    @Timed
    public ResponseEntity<LineItomsDTO> createLineItoms(@RequestBody LineItomsDTO lineItomsDTO) throws URISyntaxException {
        log.debug("REST request to save LineItoms : {}", lineItomsDTO);
        if (lineItomsDTO.getId() != null) {
            throw new BadRequestAlertException("A new lineItoms cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LineItomsDTO result = lineItomsService.save(lineItomsDTO);
        return ResponseEntity.created(new URI("/api/line-itoms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /line-itoms : Updates an existing lineItoms.
     *
     * @param lineItomsDTO the lineItomsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lineItomsDTO,
     * or with status 400 (Bad Request) if the lineItomsDTO is not valid,
     * or with status 500 (Internal Server Error) if the lineItomsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/line-itoms")
    @Timed
    public ResponseEntity<LineItomsDTO> updateLineItoms(@RequestBody LineItomsDTO lineItomsDTO) throws URISyntaxException {
        log.debug("REST request to update LineItoms : {}", lineItomsDTO);
        if (lineItomsDTO.getId() == null) {
            return createLineItoms(lineItomsDTO);
        }
        LineItomsDTO result = lineItomsService.save(lineItomsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, lineItomsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /line-itoms : get all the lineItoms.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of lineItoms in body
     */
    @GetMapping("/line-itoms")
    @Timed
    public ResponseEntity<List<LineItomsDTO>> getAllLineItoms(Pageable pageable) {
        log.debug("REST request to get a page of LineItoms");
        Page<LineItomsDTO> page = lineItomsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/line-itoms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /line-itoms/:id : get the "id" lineItoms.
     *
     * @param id the id of the lineItomsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lineItomsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/line-itoms/{id}")
    @Timed
    public ResponseEntity<LineItomsDTO> getLineItoms(@PathVariable Long id) {
        log.debug("REST request to get LineItoms : {}", id);
        LineItomsDTO lineItomsDTO = lineItomsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(lineItomsDTO));
    }

    /**
     * DELETE  /line-itoms/:id : delete the "id" lineItoms.
     *
     * @param id the id of the lineItomsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/line-itoms/{id}")
    @Timed
    public ResponseEntity<Void> deleteLineItoms(@PathVariable Long id) {
        log.debug("REST request to delete LineItoms : {}", id);
        lineItomsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/line-itoms?query=:query : search for the lineItoms corresponding
     * to the query.
     *
     * @param query the query of the lineItoms search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/line-itoms")
    @Timed
    public ResponseEntity<List<LineItomsDTO>> searchLineItoms(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LineItoms for query {}", query);
        Page<LineItomsDTO> page = lineItomsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/line-itoms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}

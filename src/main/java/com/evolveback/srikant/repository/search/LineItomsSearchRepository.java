package com.evolveback.srikant.repository.search;

import com.evolveback.srikant.domain.LineItoms;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the LineItoms entity.
 */
public interface LineItomsSearchRepository extends ElasticsearchRepository<LineItoms, Long> {
}

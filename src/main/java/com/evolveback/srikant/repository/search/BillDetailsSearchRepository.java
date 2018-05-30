package com.evolveback.srikant.repository.search;

import com.evolveback.srikant.domain.BillDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BillDetails entity.
 */
public interface BillDetailsSearchRepository extends ElasticsearchRepository<BillDetails, Long> {
}

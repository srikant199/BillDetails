package com.evolveback.srikant.repository;

import com.evolveback.srikant.domain.BillDetails;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BillDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillDetailsRepository extends JpaRepository<BillDetails, Long>, JpaSpecificationExecutor<BillDetails> {

}

package com.evolveback.srikant.repository;

import com.evolveback.srikant.domain.LineItoms;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the LineItoms entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LineItomsRepository extends JpaRepository<LineItoms, Long> {

}

package com.evolveback.srikant.service.mapper;

import com.evolveback.srikant.domain.*;
import com.evolveback.srikant.service.dto.BillDetailsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BillDetails and its DTO BillDetailsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BillDetailsMapper extends EntityMapper<BillDetailsDTO, BillDetails> {


    @Mapping(target = "lineItoms", ignore = true)
    BillDetails toEntity(BillDetailsDTO billDetailsDTO);

    default BillDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        BillDetails billDetails = new BillDetails();
        billDetails.setId(id);
        return billDetails;
    }
}

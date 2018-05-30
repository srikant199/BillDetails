package com.evolveback.srikant.service.mapper;

import com.evolveback.srikant.domain.*;
import com.evolveback.srikant.service.dto.LineItomsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity LineItoms and its DTO LineItomsDTO.
 */
@Mapper(componentModel = "spring", uses = {BillDetailsMapper.class})
public interface LineItomsMapper extends EntityMapper<LineItomsDTO, LineItoms> {

    @Mapping(source = "billDetails.id", target = "billDetailsId")
    LineItomsDTO toDto(LineItoms lineItoms);

    @Mapping(source = "billDetailsId", target = "billDetails")
    LineItoms toEntity(LineItomsDTO lineItomsDTO);

    default LineItoms fromId(Long id) {
        if (id == null) {
            return null;
        }
        LineItoms lineItoms = new LineItoms();
        lineItoms.setId(id);
        return lineItoms;
    }
}

package com.cykor.hub.base.service;

import com.cykor.hub.base.domain.Congregation;
import com.cykor.hub.base.model.CongregationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CongregationMapper {

    CongregationDTO updateCongregationDTO(Congregation congregation,
            @MappingTarget CongregationDTO congregationDTO);

    @Mapping(target = "id", ignore = true)
    Congregation updateCongregation(CongregationDTO congregationDTO,
            @MappingTarget Congregation congregation);

}

package com.cykor.hub.base.service;

import com.cykor.hub.base.model.CongregationDTO;
import java.util.List;
import java.util.UUID;


public interface CongregationService {

    List<CongregationDTO> findAll();

    CongregationDTO get(UUID id);

    UUID create(CongregationDTO congregationDTO);

    void update(UUID id, CongregationDTO congregationDTO);

    void delete(UUID id);

}

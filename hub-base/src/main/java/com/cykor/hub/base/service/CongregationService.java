package com.cykor.hub.base.service;

import com.cykor.hub.base.domain.Congregation;
import com.cykor.hub.base.model.CongregationDTO;
import com.cykor.hub.base.repos.CongregationRepository;
import com.cykor.hub.base.repos.UserRepository;
import com.cykor.hub.base.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class CongregationService {

    private final CongregationRepository congregationRepository;
    private final CongregationMapper congregationMapper;
    private final UserRepository userRepository;

    public CongregationService(final CongregationRepository congregationRepository,
            final CongregationMapper congregationMapper, final UserRepository userRepository) {
        this.congregationRepository = congregationRepository;
        this.congregationMapper = congregationMapper;
        this.userRepository = userRepository;
    }

    public List<CongregationDTO> findAll() {
        final List<Congregation> congregations = congregationRepository.findAll(Sort.by("id"));
        return congregations.stream()
                .map(congregation -> congregationMapper.updateCongregationDTO(congregation, new CongregationDTO()))
                .toList();
    }

    public CongregationDTO get(final UUID id) {
        return congregationRepository.findById(id)
                .map(congregation -> congregationMapper.updateCongregationDTO(congregation, new CongregationDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final CongregationDTO congregationDTO) {
        final Congregation congregation = new Congregation();
        congregationMapper.updateCongregation(congregationDTO, congregation);
        return congregationRepository.save(congregation).getId();
    }

    public void update(final UUID id, final CongregationDTO congregationDTO) {
        final Congregation congregation = congregationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        congregationMapper.updateCongregation(congregationDTO, congregation);
        congregationRepository.save(congregation);
    }

    public void delete(final UUID id) {
        final Congregation congregation = congregationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        userRepository.findAllByCongregations(congregation)
                .forEach(user -> user.getCongregations().remove(congregation));
        congregationRepository.delete(congregation);
    }

}

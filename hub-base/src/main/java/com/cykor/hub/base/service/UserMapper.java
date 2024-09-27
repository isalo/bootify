package com.cykor.hub.base.service;

import com.cykor.hub.base.domain.Congregation;
import com.cykor.hub.base.domain.Role;
import com.cykor.hub.base.domain.User;
import com.cykor.hub.base.model.UserDTO;
import com.cykor.hub.base.repos.CongregationRepository;
import com.cykor.hub.base.repos.RoleRepository;
import com.cykor.hub.base.util.NotFoundException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "congregations", ignore = true)
    @Mapping(target = "password", ignore = true)
    UserDTO updateUserDTO(User user, @MappingTarget UserDTO userDTO);

    @AfterMapping
    default void afterUpdateUserDTO(User user, @MappingTarget UserDTO userDTO) {
        userDTO.setRoles(user.getRoles().stream()
                .map(role -> role.getId())
                .toList());
        userDTO.setCongregations(user.getCongregations().stream()
                .map(congregation -> congregation.getId())
                .toList());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "congregations", ignore = true)
    @Mapping(target = "password", ignore = true)
    User updateUser(UserDTO userDTO, @MappingTarget User user,
            @Context RoleRepository roleRepository,
            @Context CongregationRepository congregationRepository,
            @Context PasswordEncoder passwordEncoder);

    @AfterMapping
    default void afterUpdateUser(UserDTO userDTO, @MappingTarget User user,
            @Context RoleRepository roleRepository,
            @Context CongregationRepository congregationRepository,
            @Context PasswordEncoder passwordEncoder) {
        final List<Role> roles = roleRepository.findAllById(
                userDTO.getRoles() == null ? Collections.emptyList() : userDTO.getRoles());
        if (roles.size() != (userDTO.getRoles() == null ? 0 : userDTO.getRoles().size())) {
            throw new NotFoundException("one of roles not found");
        }
        user.setRoles(new HashSet<>(roles));
        final List<Congregation> congregations = congregationRepository.findAllById(
                userDTO.getCongregations() == null ? Collections.emptyList() : userDTO.getCongregations());
        if (congregations.size() != (userDTO.getCongregations() == null ? 0 : userDTO.getCongregations().size())) {
            throw new NotFoundException("one of congregations not found");
        }
        user.setCongregations(new HashSet<>(congregations));
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    }

}

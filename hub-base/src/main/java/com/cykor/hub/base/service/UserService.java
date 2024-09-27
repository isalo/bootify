package com.cykor.hub.base.service;

import com.cykor.hub.base.model.UserDTO;
import com.cykor.hub.base.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;


public interface UserService {

    List<UserDTO> findAll();

    UserDTO get(UUID id);

    UUID create(UserDTO userDTO);

    void update(UUID id, UserDTO userDTO);

    void delete(UUID id);

    boolean emailExists(String email);

    ReferencedWarning getReferencedWarning(UUID id);

}

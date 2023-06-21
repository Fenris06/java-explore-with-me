package ru.practicum.ewm.service.admin;

import ru.practicum.ewm.dto.userdto.UserDTO;

import java.util.List;

public interface AdminUserService {

    List<UserDTO> getUsers(List<Long> ids, Integer from, Integer size);

    UserDTO createUser(UserDTO userDTO);

    Long delete(Long userId);
}

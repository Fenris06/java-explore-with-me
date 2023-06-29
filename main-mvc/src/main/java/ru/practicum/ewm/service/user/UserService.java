package ru.practicum.ewm.service.user;

import ru.practicum.ewm.dto.user.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getUsers(List<Long> ids, Integer from, Integer size);

    UserDTO createUser(UserDTO userDTO);

    void delete(Long userId);
}

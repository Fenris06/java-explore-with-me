package ru.practicum.ewm.mapper.usermapper;

import ru.practicum.ewm.dto.userdto.UserDTO;
import ru.practicum.ewm.model.user.User;

public class UserMapper {

    public static UserDTO toDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setEmail(user.getEmail());
        dto.setId(user.getId());
        dto.setName(user.getName());
        return dto;
    }

    public static User fromDto(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        return user;
    }
}

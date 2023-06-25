package ru.practicum.ewm.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.user.UserDTO;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.user.UserMapper;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.service.admin.AdminUserService;
import ru.practicum.ewm.storage.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getUsers(List<Long> ids, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        if (ids.isEmpty()) {
            return repository.findAll(pageRequest)
                    .stream()
                    .map(UserMapper::toDto)
                    .collect(Collectors.toList());
        }
        return repository.findByIdIn(ids, pageRequest)
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        User user = UserMapper.fromDto(userDTO);
        return UserMapper.toDto(repository.save(user));
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        checkUser(userId);
        repository.deleteById(userId);
    }

    private void checkUser(Long userId) {
        repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
    }
}

package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.user.UserDTO;
import ru.practicum.ewm.service.admin.AdminUserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
public class AdminUsersController {
    private final AdminUserService service;

    @GetMapping
    public List<UserDTO> getUsers(@RequestParam(name = "ids", required = false, defaultValue = "") List<Long> ids,
                                  @RequestParam(name = "from", required = false, defaultValue = "0") @Min(0) Integer from,
                                  @RequestParam(name = "size", required = false, defaultValue = "10") @Min(0) Integer size) {
        return service.getUsers(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@RequestBody @Valid UserDTO userDTO) {
        return service.createUser(userDTO);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("userId") Long userId) {
        service.delete(userId);
    }
}

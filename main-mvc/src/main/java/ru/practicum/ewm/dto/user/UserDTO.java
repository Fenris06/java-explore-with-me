package ru.practicum.ewm.dto.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import javax.validation.constraints.*;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class UserDTO {
    @NotBlank(message = "Field: email. Error: must not be empty or have only white spase. Value: empty")
    @Email(message = "Field: email. Error: must be email pattern. Value: null")
    @Size(min = 6, max = 254, message = "Field: email. Error: must be min 6 char and max 254")
    private String email;
    @Null(message = "Field: id. Error: must be null Value: null")
    private Long id;
    @NotBlank(message = "Field: name. Error: must not be empty or have only white spase. Value: null")
    @Size(min = 2, max = 250, message = "Field: name. Error: must be min 6 char and max 254")
    private String name;
}

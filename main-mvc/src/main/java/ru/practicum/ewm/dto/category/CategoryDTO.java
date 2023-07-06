package ru.practicum.ewm.dto.category;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CategoryDTO {
    @Null(message = "Field: id. Error: must be null. Value: null")
    private Long id;
    @NotBlank(message = "Field: name. Error: must not be empty or have only white spase. Value: empty")
    @Size(min = 1, max = 50, message = "Field: name. Error: must be min 1 char and max 50 char")
    private String name;
}

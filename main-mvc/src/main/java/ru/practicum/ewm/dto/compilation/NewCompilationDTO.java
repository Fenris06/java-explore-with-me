package ru.practicum.ewm.dto.compilation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.vallidarion.Create;
import ru.practicum.ewm.vallidarion.Update;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class NewCompilationDTO {
    private Set<Long> events;
    private Boolean pinned;
    @NotBlank(message = "Field: description. Error: must not be empty or have only white spase. Value: empty", groups = Create.class)
    @Size(min = 1, max = 50, message = "Field: annotation. Error: must be min 1 char and max 50", groups = {Create.class, Update.class})
    private String title;

}

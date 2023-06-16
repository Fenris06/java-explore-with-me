package ru.practicum.ewm;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class StatDTO {
    Long id;
    @NotNull(message = "App can't be null")
    @NotEmpty(message = "App can't be empty")
    private String app;
    @NotNull(message = "Uri can't be null")
    @NotEmpty(message = "Uri can't be empty")
    private String uri;
    @NotNull(message = "Ip can't be null")
    @NotEmpty(message = "Ip can't be empty")
    private String ip;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}

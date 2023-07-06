package ru.practicum.ewm.dto.compilation;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.dto.event.EventShortDTO;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CompilationDTO {
   private List<EventShortDTO> events;
   private Long id;
   private Boolean pinned;
   private String title;
}

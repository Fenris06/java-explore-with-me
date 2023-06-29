package ru.practicum.ewm.mapper.compilation;

import ru.practicum.ewm.dto.compilation.CompilationDTO;
import ru.practicum.ewm.dto.compilation.NewCompilationDTO;
import ru.practicum.ewm.model.compilation.Compilation;

public class CompilationMapper {

    public static Compilation fromDTO(NewCompilationDTO newCompilationDTO) {
        Compilation compilation = new Compilation();
        compilation.setPinned(newCompilationDTO.getPinned());
        compilation.setTitle(newCompilationDTO.getTitle());
        return compilation;
    }

    public static CompilationDTO toDTO(Compilation compilation) {
        CompilationDTO compilationDTO = new CompilationDTO();
        compilationDTO.setId(compilation.getId());
        compilationDTO.setPinned(compilation.getPinned());
        compilationDTO.setTitle(compilation.getTitle());
        return compilationDTO;
    }
}

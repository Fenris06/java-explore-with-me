package ru.practicum.ewm.service.compilation;

import ru.practicum.ewm.dto.compilation.CompilationDTO;
import ru.practicum.ewm.dto.compilation.NewCompilationDTO;

import java.util.List;

public interface CompilationService {

    CompilationDTO createCompilation(NewCompilationDTO newCompilationDTO);

    void deleteCompilation(Long compId);

    CompilationDTO updateCompilation(Long compId, NewCompilationDTO newCompilationDTO);

    List<CompilationDTO> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDTO getCompilation(Long compId);
}

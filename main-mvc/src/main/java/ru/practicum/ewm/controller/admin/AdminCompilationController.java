package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.compilation.CompilationDTO;
import ru.practicum.ewm.dto.compilation.NewCompilationDTO;
import ru.practicum.ewm.service.compilation.CompilationService;
import ru.practicum.ewm.vallidarion.Create;
import ru.practicum.ewm.vallidarion.Update;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Validated
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Create.class)
    public CompilationDTO createCompilation(@RequestBody @Valid NewCompilationDTO newCompilationDTO) {
        return compilationService.createCompilation(newCompilationDTO);
    }

    @DeleteMapping("{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable("compId") Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("{compId}")
    @Validated(Update.class)
    public CompilationDTO updateCompilation(@PathVariable("compId") Long compId,
                                            @RequestBody @Valid NewCompilationDTO newCompilationDTO) {
        return compilationService.updateCompilation(compId, newCompilationDTO);
    }
}

package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.compilation.CompilationDTO;
import ru.practicum.ewm.service.compilation.CompilationService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Validated
public class PubCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDTO> getCompilations(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                                @RequestParam(name = "from", required = false, defaultValue = "0") @Min(0) Integer from,
                                                @RequestParam(name = "size", required = false, defaultValue = "10") @Min(1) Integer size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("{compId}")
    public CompilationDTO getCompilation(@PathVariable("compId") Long compId) {
        return compilationService.getCompilation(compId);
    }

}

package com.ratmir.spring.hogwardsartifactsonline.controller;

import com.ratmir.spring.hogwardsartifactsonline.dto.Result;
import com.ratmir.spring.hogwardsartifactsonline.dto.WizardDto;
import com.ratmir.spring.hogwardsartifactsonline.service.WizardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.endpoint.base-url}/wizards")
@RequiredArgsConstructor
public class WizardController {

    private final WizardService wizardService;

    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable Long wizardId) {
        var wizardDto = wizardService.findById(wizardId);
        return Result.builder()
                .flag(true)
                .code(HttpStatus.OK.value())
                .message("Find One Success")
                .data(wizardDto)
                .build();
    }

    @GetMapping
    public Result findAllWizards() {
        var wizardDtos = wizardService.findAll();
        return Result.builder()
                .flag(true)
                .code(HttpStatus.OK.value())
                .message("Find All Success")
                .data(wizardDtos)
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result addWizard(@Valid @RequestBody WizardDto wizardDto) {
        var savedWizardDto = wizardService.save(wizardDto);
        return Result.builder()
                .flag(true)
                .code(HttpStatus.CREATED.value())
                .message("Add Success")
                .data(savedWizardDto)
                .build();
    }

    @PutMapping("/{wizardId}")
    public Result updateWizard(
            @PathVariable Long wizardId,
            @Valid @RequestBody WizardDto wizardDto
    ) {
        var updatedWizardDto = wizardService.update(wizardId, wizardDto);
        return Result.builder()
                .flag(true)
                .code(HttpStatus.OK.value())
                .message("Update Success")
                .data(updatedWizardDto)
                .build();
    }

    @DeleteMapping("/{wizardId}")
    public Result deleteWizard(@PathVariable Long wizardId) {
        wizardService.delete(wizardId);
        return Result.builder()
                .flag(true)
                .code(HttpStatus.NO_CONTENT.value())
                .message("Delete Success")
                .data(null)
                .build();
    }
}
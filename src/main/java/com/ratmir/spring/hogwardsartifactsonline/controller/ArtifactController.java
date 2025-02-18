package com.ratmir.spring.hogwardsartifactsonline.controller;

import com.ratmir.spring.hogwardsartifactsonline.dto.ArtifactDto;
import com.ratmir.spring.hogwardsartifactsonline.service.ArtifactService;
import com.ratmir.spring.hogwardsartifactsonline.dto.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/artifacts")
public class ArtifactController {

    private final ArtifactService artifactService;

    @GetMapping("/{artifactId}")
    @ResponseStatus(HttpStatus.OK)
    public Result findArtifactById(@PathVariable Long artifactId) {
        var artifactDto = artifactService.findById(artifactId);
        return Result.builder()
                .flag(true)
                .code(HttpStatus.OK.value())
                .message("Find One Success")
                .data(artifactDto)
                .build();
    }

    @GetMapping()
    public Result findAllArtifacts() {
        List<ArtifactDto> artifactDtoList = artifactService.findAll();
        return Result.builder()
                .flag(true)
                .code(HttpStatus.OK.value())
                .message("Find All Success")
                .data(artifactDtoList)
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result addArtifact(@Valid @RequestBody ArtifactDto artifactDto) {
        var savedArtifactDto = artifactService.save(artifactDto);
        return Result.builder()
                .flag(true)
                .code(HttpStatus.CREATED.value())
                .message("Add Success")
                .data(savedArtifactDto)
                .build();
    }

    @PutMapping("/{artifactId}")
    public Result updateArtifact(
            @PathVariable Long artifactId,
            @Valid @RequestBody ArtifactDto artifactDto
    ) {
        var updatedArtifactDto = artifactService.update(artifactId, artifactDto);
        return Result.builder()
                .flag(true)
                .code(HttpStatus.OK.value())
                .message("Update Success")
                .data(updatedArtifactDto)
                .build();
    }

    @DeleteMapping("/{artifactId}")
    @ResponseStatus(HttpStatus.OK)
    public Result deleteArtifact(@PathVariable Long artifactId) {
        artifactService.delete(artifactId);
        return Result.builder()
                .flag(true)
                .code(HttpStatus.NO_CONTENT.value())
                .message("Delete Success")
                .build();
    }
}

package com.ratmir.spring.hogwardsartifactsonline.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record WizardDto(
        Long id,

        @NotEmpty(message = "Name is required")
        String name,

        Integer numberOfArtifacts
) {
}

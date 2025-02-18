package com.ratmir.spring.hogwardsartifactsonline.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record ArtifactDto(
        Long id,

        @NotEmpty(message = "Name is required")
        String name,

        @NotEmpty(message = "Description is required")
        String description,

        @NotEmpty(message = "ImageUrl is required")
        String imageUrl,

        WizardDto owner
) {
}

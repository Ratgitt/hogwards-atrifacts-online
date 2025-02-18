package com.ratmir.spring.hogwardsartifactsonline.dto;

import lombok.Builder;

@Builder
public record WizardDto(
        Long id,
        String name,
        Integer numberOfArtifacts
) {
}

package com.ratmir.spring.hogwardsartifactsonline.util.converter;

import com.ratmir.spring.hogwardsartifactsonline.dto.ArtifactDto;
import com.ratmir.spring.hogwardsartifactsonline.entity.Artifact;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ArtifactConverter {

    private final WizardConverter wizardConverter;

    public ArtifactDto toArtifactDto(Artifact source) {
        return new ArtifactDto(
                source.getId(),
                source.getName(),
                source.getDescription(),
                source.getImageUrl(),
                Optional.ofNullable(source.getOwner())
                        .map(wizardConverter::toWizardDto)
                        .orElse(null)
        );
    }

    public Artifact toArtifactEntity(ArtifactDto source) {
        return new Artifact(
                source.id(),
                source.name(),
                source.description(),
                source.imageUrl(),
                Optional.ofNullable(source.owner())
                        .map(wizardConverter::toWizardEntity)
                        .orElse(null)
        );
    }
}

package com.ratmir.spring.hogwardsartifactsonline.service;

import com.ratmir.spring.hogwardsartifactsonline.dto.ArtifactDto;
import com.ratmir.spring.hogwardsartifactsonline.entity.Artifact;
import com.ratmir.spring.hogwardsartifactsonline.util.converter.ArtifactConverter;
import com.ratmir.spring.hogwardsartifactsonline.util.exception.ObjectNotFoundException;
import com.ratmir.spring.hogwardsartifactsonline.repository.ArtifactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.*;

@RequiredArgsConstructor
@Service
public class ArtifactService {

    private final ArtifactRepository artifactRepository;
    private final ArtifactConverter artifactConverter;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public ArtifactDto findById(Long artifactId) {
        return artifactRepository.findById(artifactId)
                .map(artifactConverter::toArtifactDto)
                .orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));
    }

    @Transactional(readOnly = true)
    public List<ArtifactDto> findAll() {
        return artifactRepository.findAll()
                .stream()
                .map(artifactConverter::toArtifactDto)
                .collect(toList());
    }

    @Transactional
    public ArtifactDto save(ArtifactDto newArtifactDto) {
        Artifact artifact = artifactConverter.toArtifactEntity(newArtifactDto);
        Artifact savedArtifact = artifactRepository.save(artifact);
        return artifactConverter.toArtifactDto(savedArtifact);
    }

    @Transactional
    public ArtifactDto update(Long artifactId, ArtifactDto newArtifactDto) {
        Artifact artifact = artifactRepository.findById(artifactId)
                .orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));

        artifact.setName(newArtifactDto.name());
        artifact.setDescription(newArtifactDto.description());
        artifact.setImageUrl(newArtifactDto.imageUrl());

        return artifactConverter.toArtifactDto(artifact);
    }

    @Transactional
    public void delete(Long artifactId) {
        artifactRepository.findById(artifactId)
                .orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));
        artifactRepository.deleteById(artifactId);
    }
}

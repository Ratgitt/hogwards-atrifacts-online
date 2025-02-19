package com.ratmir.spring.hogwardsartifactsonline.service;

import com.ratmir.spring.hogwardsartifactsonline.dto.ArtifactDto;
import com.ratmir.spring.hogwardsartifactsonline.entity.Artifact;
import com.ratmir.spring.hogwardsartifactsonline.util.converter.ArtifactConverter;
import com.ratmir.spring.hogwardsartifactsonline.util.exception.ObjectNotFoundException;
import com.ratmir.spring.hogwardsartifactsonline.repository.ArtifactRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.*;

@RequiredArgsConstructor
@Service
@Transactional
public class ArtifactService {

    private final ArtifactRepository artifactRepository;
    private final ArtifactConverter artifactConverter;

    public ArtifactDto findById(Long artifactId) {
        return artifactRepository.findById(artifactId)
                .map(artifactConverter::toArtifactDto)
                .orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));
    }

    public List<ArtifactDto> findAll() {
        return artifactRepository.findAll()
                .stream()
                .map(artifactConverter::toArtifactDto)
                .collect(toList());
    }

    public ArtifactDto save(ArtifactDto newArtifactDto) {
        Artifact artifact = artifactConverter.toArtifactEntity(newArtifactDto);
        Artifact savedArtifact = artifactRepository.save(artifact);
        return artifactConverter.toArtifactDto(savedArtifact);
    }

    public ArtifactDto update(Long artifactId, ArtifactDto newArtifactDto) {
        Artifact artifact = artifactRepository.findById(artifactId)
                .orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));

        artifact.setName(newArtifactDto.name());
        artifact.setDescription(newArtifactDto.description());
        artifact.setImageUrl(newArtifactDto.imageUrl());

        return artifactConverter.toArtifactDto(artifact);
    }

    public void delete(Long artifactId) {
        artifactRepository.findById(artifactId)
                .orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));
        artifactRepository.deleteById(artifactId);
    }
}

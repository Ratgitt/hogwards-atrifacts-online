package com.ratmir.spring.hogwardsartifactsonline.artifact;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.ratmir.spring.hogwardsartifactsonline.dto.ArtifactDto;
import com.ratmir.spring.hogwardsartifactsonline.dto.WizardDto;
import com.ratmir.spring.hogwardsartifactsonline.entity.Artifact;
import com.ratmir.spring.hogwardsartifactsonline.entity.Wizard;
import com.ratmir.spring.hogwardsartifactsonline.repository.ArtifactRepository;
import com.ratmir.spring.hogwardsartifactsonline.service.ArtifactService;
import com.ratmir.spring.hogwardsartifactsonline.util.converter.ArtifactConverter;
import com.ratmir.spring.hogwardsartifactsonline.util.exception.ArtifactNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {

    @Mock
    private ArtifactRepository artifactRepository;
    @Mock
    private ArtifactConverter artifactConverter;

    @InjectMocks
    private ArtifactService artifactService;

    List<Artifact> artifacts;
    List<ArtifactDto> artifactDtos;

    @BeforeEach
    void setUp() {
        artifacts = List.of(
                new Artifact(
                        1L,
                        "Deluminator",
                        "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.",
                        "ImageUrl"
                ),
                new Artifact(
                        2L,
                        "Invisibility Cloak",
                        "An invisibility cloak is used to make the wearer invisible.",
                        "ImageUrl"
                )
        );
        artifactDtos = List.of(
                new ArtifactDto(
                        1L,
                        "Deluminator",
                        "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.",
                        "ImageUrl",
                        null
                ),
                new ArtifactDto(
                        2L,
                        "Invisibility Cloak",
                        "An invisibility cloak is used to make the wearer invisible.",
                        "ImageUrl",
                        null
                )
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Order(1)
    void testFindByIdSuccess() {
        // Given
        Wizard wizard = Wizard.builder()
                .id(1L)
                .name("Harry Potter")
                .build();

        Artifact artifact = Artifact.builder()
                .id(2L)
                .name("Invisibility Clock")
                .description("An invisibility clock is used to make the wearer invisible")
                .imageUrl("ImageUrl")
                .owner(wizard)
                .build();

        var wizardDto = WizardDto.builder()
                .id(1L)
                .name("Harry Potter")
                .build();

        var artifactDto = ArtifactDto.builder()
                .id(2L)
                .name("Invisibility Clock")
                .description("An invisibility clock is used to make the wearer invisible")
                .imageUrl("ImageUrl")
                .owner(wizardDto)
                .build();

        given(artifactRepository.findById(artifact.getId())).willReturn(Optional.of(artifact));
        given(artifactConverter.toArtifactDto(artifact)).willReturn(artifactDto);

        // When
        ArtifactDto returnedArtifactDto = artifactService.findById(artifact.getId());

        // Then
        assertThat(returnedArtifactDto.id()).isEqualTo(artifact.getId());
        assertThat(returnedArtifactDto.name()).isEqualTo(artifact.getName());
        assertThat(returnedArtifactDto.description()).isEqualTo(artifact.getDescription());
        assertThat(returnedArtifactDto.imageUrl()).isEqualTo(artifact.getImageUrl());
        verify(artifactRepository, times(1)).findById(artifact.getId());
        verify(artifactConverter, times(1)).toArtifactDto(artifact);
    }

    @Test
    @Order(2)
    void testFindByIdNotFound() {
        Long nonExistingId = -1L;
        given(artifactRepository.findById(nonExistingId)).willReturn(Optional.empty());

        var exception = assertThrows(ArtifactNotFoundException.class,
                () -> artifactService.findById(nonExistingId));

        assertThat(exception.getMessage()).isEqualTo(ArtifactNotFoundException.DEFAULT_MESSAGE + nonExistingId);
        verify(artifactRepository, times(1)).findById(nonExistingId);
        verify(artifactConverter, never()).toArtifactDto(any());
    }

    @Test
    @Order(3)
    void findAllSuccess() {
        given(artifactRepository.findAll()).willReturn(artifacts);

        List<ArtifactDto> returnedArtifactDtos = artifactService.findAll();

        assertThat(returnedArtifactDtos.size()).isEqualTo(artifacts.size());
        verify(artifactRepository, times(1)).findAll();
    }

    @Test
    @Order(4)
    void testSaveSuccess() {
        var inputDto = ArtifactDto.builder()
                .name("name")
                .description("description")
                .imageUrl("ImageUrl")
                .build();
        var convertedArtifact = Artifact.builder()
                .name("name")
                .description("description")
                .imageUrl("ImageUrl")
                .build();
        var savedArtifact = Artifact.builder()
                .id(1L)
                .name("name")
                .description("description")
                .imageUrl("ImageUrl")
                .build();
        var expectedDto = ArtifactDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .imageUrl("ImageUrl")
                .build();

        given(artifactConverter.toArtifactEntity(inputDto)).willReturn(convertedArtifact);
        given(artifactRepository.save(convertedArtifact)).willReturn(savedArtifact);
        given(artifactConverter.toArtifactDto(savedArtifact)).willReturn(expectedDto);

        var savedArtifactDto = artifactService.save(inputDto);

        assertThat(savedArtifactDto.id()).isEqualTo(savedArtifact.getId());
        assertThat(savedArtifactDto.name()).isEqualTo(savedArtifact.getName());
        assertThat(savedArtifactDto.description()).isEqualTo(savedArtifact.getDescription());
        assertThat(savedArtifactDto.imageUrl()).isEqualTo(savedArtifact.getImageUrl());

        verify(artifactRepository, times(1)).save(convertedArtifact);
        verify(artifactConverter, times(1)).toArtifactEntity(inputDto);
    }

    @Test
    @Order(5)
    void testUpdateSuccess() {
        Long artifactId = 1L;
        var newArtifactDto = ArtifactDto.builder()
                .name("newName")
                .description("newDescription")
                .imageUrl("newImageUrl")
                .build();
        var oldArtifactEntity = Artifact.builder()
                .id(1L)
                .name("oldName")
                .description("oldDescription")
                .imageUrl("oldImageUrl")
                .build();
        var updatedArtifactEntity = Artifact.builder()
                .id(1L)
                .name("newName")
                .description("newDescription")
                .imageUrl("newImageUrl")
                .build();
        var expectedArtifactDto = ArtifactDto.builder()
                .id(1L)
                .name("newName")
                .description("newDescription")
                .imageUrl("newImageUrl")
                .build();

        given(artifactRepository.findById(artifactId)).willReturn(Optional.of(oldArtifactEntity));
        given(artifactRepository.save(any(Artifact.class))).willReturn(updatedArtifactEntity);
        given(artifactConverter.toArtifactDto(updatedArtifactEntity)).willReturn(expectedArtifactDto);

        var updatedArtifactDto = artifactService.update(artifactId, newArtifactDto);
        assertThat(updatedArtifactDto).isEqualTo(expectedArtifactDto);

        verify(artifactRepository, times(1)).findById(artifactId);
        verify(artifactRepository, times(1)).save(any(Artifact.class));
        verify(artifactConverter, times(1)).toArtifactDto(updatedArtifactEntity);
    }

    @Test
    @Order(6)
    void testUpdateNotFound() {
        Long artifactId = 1L;
        var newArtifactDto = ArtifactDto.builder()
                .name("newName")
                .description("newDescription")
                .imageUrl("newImageUrl")
                .build();

        given(artifactRepository.findById(artifactId)).willReturn(Optional.empty());

        assertThrows(ArtifactNotFoundException.class,
                () -> artifactService.update(artifactId, newArtifactDto));

        verify(artifactRepository, times(1)).findById(artifactId);
    }

    @Test
    @Order(7)
    void testDeleteArtifactSuccess() {
        //Given
        Long artifactId = 1L;
        Artifact artifact = new Artifact(
                1L,
                "Deluminator",
                "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.",
                "ImageUrl"
        );

        given(artifactRepository.findById(artifactId)).willReturn(Optional.of(artifact));
        doNothing().when(artifactRepository).deleteById(artifactId);

        //When
        artifactService.delete(artifactId);

        //Then
        verify(artifactRepository, times(1)).findById(artifactId);
        verify(artifactRepository, times(1)).deleteById(artifactId);
    }

    @Test
    @Order(8)
    void testDeleteArtifactNotFound() {
        //Given
        Long artifactId = 1L;
        given(artifactRepository.findById(artifactId))
                .willThrow(new ArtifactNotFoundException(artifactId));

        //When
        assertThrows(ArtifactNotFoundException.class,
                () -> artifactService.delete(artifactId));

        //Then
        verify(artifactRepository, times(1)).findById(artifactId);
    }

}
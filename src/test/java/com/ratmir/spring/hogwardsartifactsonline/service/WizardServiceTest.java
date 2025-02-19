package com.ratmir.spring.hogwardsartifactsonline.service;

import com.ratmir.spring.hogwardsartifactsonline.dto.WizardDto;
import com.ratmir.spring.hogwardsartifactsonline.entity.Artifact;
import com.ratmir.spring.hogwardsartifactsonline.entity.Wizard;
import com.ratmir.spring.hogwardsartifactsonline.repository.ArtifactRepository;
import com.ratmir.spring.hogwardsartifactsonline.repository.WizardRepository;
import com.ratmir.spring.hogwardsartifactsonline.util.converter.WizardConverter;
import com.ratmir.spring.hogwardsartifactsonline.util.exception.ObjectNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.ratmir.spring.hogwardsartifactsonline.util.exception.ObjectNotFoundException.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;
    @Mock
    ArtifactRepository artifactRepository;

    @Mock
    WizardConverter wizardConverter;

    @InjectMocks
    WizardService wizardService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Order(1)
    void testFindByIdSuccess() {
        //Given
        Long wizardId = 1L;
        var wizardDto = WizardDto.builder()
                .id(1L)
                .name("Albus Dumbledore")
                .numberOfArtifacts(0)
                .build();
        var wizardEntity = Wizard.builder()
                .id(1L)
                .name("Albus Dumbledore")
                .artifacts(Collections.emptyList())
                .build();


        given(wizardRepository.findById(wizardId)).willReturn(Optional.of(wizardEntity));
        given(wizardConverter.toWizardDto(wizardEntity)).willReturn(wizardDto);

        //When
        var returnedWizardDto = wizardService.findById(wizardId);

        //Then
        assertThat(returnedWizardDto).isEqualTo(wizardDto);

        verify(wizardRepository, times(1)).findById(wizardId);
        verify(wizardConverter, times(1)).toWizardDto(wizardEntity);
    }

    @Test
    @Order(2)
    void testFindByIdNotFound() {
        //Given
        Long nonExistingId = -1L;
        given(wizardRepository.findById(nonExistingId)).willReturn(Optional.empty());

        //When
        var exception = assertThrows(ObjectNotFoundException.class,
                () -> wizardService.findById(nonExistingId));

        //Then
        assertThat(exception.getMessage()).isEqualTo(WIZARD_NOT_FOUND_MESSAGE + nonExistingId);
        verify(wizardRepository, times(1)).findById(nonExistingId);
        verify(wizardConverter, never()).toWizardDto(any());
    }

    @Test
    @Order(3)
    void testFindAll() {
        List<Wizard> wizardEntities = List.of(
                new Wizard(1L, "Albus Dumbledore", List.of(new Artifact(), new Artifact())),
                new Wizard(2L, "Harry Potter", List.of(new Artifact(), new Artifact())),
                new Wizard(3L, "Neville Longbottom", List.of(new Artifact()))
        );
        List<WizardDto> wizardDtos = List.of(
                new WizardDto(1L, "Albus Dumbledore", 2),
                new WizardDto(2L, "Harry Potter", 2),
                new WizardDto(3L, "Neville Longbottom", 1)
        );

        given(wizardRepository.findAll()).willReturn(wizardEntities);
        given(wizardConverter.toWizardDto(wizardEntities.getFirst())).willReturn(wizardDtos.getFirst());
        given(wizardConverter.toWizardDto(wizardEntities.get(1))).willReturn(wizardDtos.get(1));
        given(wizardConverter.toWizardDto(wizardEntities.get(2))).willReturn(wizardDtos.get(2));

        List<WizardDto> wizardList = wizardService.findAll();

        assertThat(wizardList).hasSize(3);
        assertThat(wizardList).isEqualTo(wizardDtos);

        verify(wizardRepository, times(1)).findAll();
        verify(wizardConverter, times(3)).toWizardDto(any());
    }

    @Test
    @Order(4)
    void testAddWizardSuccess() {
        WizardDto inputDto = new WizardDto(null, "Albus Dumbledore", null);
        Wizard convertedEntity = new Wizard(null, "Albus Dumbledore", new ArrayList<>());
        Wizard savedEntity = new Wizard(1L, "Albus Dumbledore", new ArrayList<>());
        WizardDto expectedDto = new WizardDto(1L, "Albus Dumbledore", 0);

        given(wizardConverter.toWizardEntity(inputDto)).willReturn(convertedEntity);
        given(wizardRepository.save(convertedEntity)).willReturn(savedEntity);
        given(wizardConverter.toWizardDto(savedEntity)).willReturn(expectedDto);

        var savedDto = wizardService.save(inputDto);

        assertThat(savedDto).isEqualTo(expectedDto);

        verify(wizardConverter, times(1)).toWizardEntity(inputDto);
        verify(wizardRepository, times(1)).save(convertedEntity);
        verify(wizardConverter, times(1)).toWizardDto(savedEntity);
    }

    @Test
    @Order(5)
    void testUpdateWizardSuccess() {
        Long wizardId = 1L;
        WizardDto inputDto = new WizardDto(null, "New Name", null);
        Wizard oldWizard = new Wizard(1L, "Albus Dumbledore", new ArrayList<>());
        WizardDto expectedWizardDto = new WizardDto(1L, "New Name", 0);

        given(wizardRepository.findById(wizardId)).willReturn(Optional.of(oldWizard));
        given(wizardConverter.toWizardDto(any())).willReturn(expectedWizardDto);

        var updatedWizardDto = wizardService.update(wizardId, inputDto);

        assertThat(updatedWizardDto).isEqualTo(expectedWizardDto);
        assertThat(oldWizard.getName()).isEqualTo("New Name");

        verify(wizardRepository, times(1)).findById(wizardId);
        verify(wizardConverter, times(1)).toWizardDto(any());
    }

    @Test
    @Order(6)
    void testUpdateWizardNotFound() {
        Long nonExistingId = 1L;
        WizardDto inputDto = new WizardDto(null, "New Name", null);

        given(wizardRepository.findById(nonExistingId)).willReturn(Optional.empty());

        var exception = assertThrows(ObjectNotFoundException.class,
                () -> wizardService.update(nonExistingId, inputDto));
        assertThat(exception.getMessage()).isEqualTo(WIZARD_NOT_FOUND_MESSAGE + nonExistingId);

        verify(wizardRepository, times(1)).findById(nonExistingId);
    }

    @Test
    @Order(7)
    void testDeleteWizardSuccess() {
        Long wizardId = 4L;
        Wizard wizard = new Wizard(4L, "Hermione Granger", new ArrayList<>());

        given(wizardRepository.findById(wizardId)).willReturn(Optional.of(wizard));
        doNothing().when(wizardRepository).deleteById(wizardId);

        wizardService.delete(wizardId);

        verify(wizardRepository, times(1)).findById(wizardId);
        verify(wizardRepository, times(1)).deleteById(wizardId);
    }

    @Test
    @Order(8)
    void testDeleteWizardNotFoundError() {
        Long wizardId = 1L;
        given(wizardRepository.findById(wizardId))
                .willThrow(new ObjectNotFoundException("wizard", wizardId));

        assertThrows(ObjectNotFoundException.class,
                () -> wizardService.delete(wizardId));

        verify(wizardRepository, times(1)).findById(wizardId);
    }

    @Test
    @Order(9)
    void testAssignArtifactSuccess() {
        var a = Artifact.builder()
                .id(2L)
                .name("Invisibility Cloak")
                .description("An invisibility cloak is used to make the wearer invisible.")
                .imageUrl("ImageUrl")
                .build();

        var w2 = new Wizard(2L, "Harry Potter");
        w2.addArtifact(a);

        var w3 = new Wizard(3L, "Neville Longbottom");

        given(artifactRepository.findById(2L)).willReturn(Optional.of(a));
        given(wizardRepository.findById(3L)).willReturn(Optional.of(w3));

        wizardService.assignArtifact(3L, 2L);

        assertThat(a.getOwner().getId()).isEqualTo(3L);
        assertThat(w3.getArtifacts()).contains(a);
        assertThat(w2.getArtifacts()).doesNotContain(a);
    }

    @Test
    @Order(10)
    void testAssignArtifactErrorWithNonExistingWizardId() {

        given(wizardRepository.findById(3L)).willReturn(Optional.empty());

        var exception = assertThrows(ObjectNotFoundException.class,
                () -> wizardService.assignArtifact(3L, 2L));

        assertThat(exception).hasMessage(WIZARD_NOT_FOUND_MESSAGE + 3L);
    }

    @Test
    @Order(11)
    void testAssignArtifactErrorWithNonExistingArtifactId() {

        var w3 = new Wizard(3L, "Neville Longbottom");

        given(wizardRepository.findById(3L)).willReturn(Optional.of(w3));
        given(artifactRepository.findById(2L)).willReturn(Optional.empty());

        var exception = assertThrows(ObjectNotFoundException.class,
                () -> wizardService.assignArtifact(3L, 2L));

        assertThat(exception).hasMessage(ARTIFACT_NOT_FOUND_MESSAGE + 2L);
    }
}
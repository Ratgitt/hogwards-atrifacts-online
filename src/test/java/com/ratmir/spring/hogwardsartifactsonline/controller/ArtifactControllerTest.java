package com.ratmir.spring.hogwardsartifactsonline.artifact;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratmir.spring.hogwardsartifactsonline.dto.ArtifactDto;
import com.ratmir.spring.hogwardsartifactsonline.service.ArtifactService;
import com.ratmir.spring.hogwardsartifactsonline.util.exception.ArtifactNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ArtifactControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ArtifactService artifactService;

    @Autowired
    ObjectMapper objectMapper;

    List<ArtifactDto> artifactDtos;

    @BeforeEach
    void setUp() {
        artifactDtos = new ArrayList<>();
        artifactDtos.addAll(List.of(
                ArtifactDto.builder()
                        .id(1L)
                        .name("Elder Wand")
                        .description("The most powerful wand ever made, one of the Deathly Hallows.")
                        .imageUrl("ImageUrl")
                        .build(),
                ArtifactDto.builder()
                        .id(2L)
                        .name("Resurrection Stone")
                        .description("A stone that can summon shades of the dead, one of the Deathly Hallows.")
                        .imageUrl("ImageUrl")
                        .build(),
                ArtifactDto.builder()
                        .id(3L)
                        .name("Philosopher's Stone")
                        .description("A legendary stone that grants immortality and turns any metal into gold.")
                        .imageUrl("ImageUrl")
                        .build(),
                ArtifactDto.builder()
                        .id(4L)
                        .name("Marauder’s Map")
                        .description("A magical map that shows the entire layout of Hogwarts and the locations of people inside.")
                        .imageUrl("ImageUrl")
                        .build(),
                ArtifactDto.builder()
                        .id(5L)
                        .name("Sword of Gryffindor")
                        .description("A goblin-made sword that appears to any worthy Gryffindor in times of need.")
                        .imageUrl("ImageUrl")
                        .build(),
                ArtifactDto.builder()
                        .id(6L)
                        .name("Horcrux Locket")
                        .description("A locket that contained a fragment of Voldemort’s soul, making him immortal.")
                        .imageUrl("ImageUrl")
                        .build()
        ));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Order(1)
    void testFindArtifactByIdSuccess() throws Exception {
        given(this.artifactService.findById(artifactDtos.getFirst().id()))
                .willReturn(artifactDtos.getFirst());

        mockMvc.perform(get("/api/v1/artifacts/" + artifactDtos.getFirst().id())
                .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(artifactDtos.getFirst().id()))
                .andExpect(jsonPath("$.data.name").value(artifactDtos.getFirst().name()));
    }

    @Test
    @Order(2)
    void testFindArtifactByIdNotFound() throws Exception {
        Long nonExistingId = -1L;
        given(artifactService.findById(nonExistingId))
                .willThrow(new ArtifactNotFoundException(nonExistingId));

        mockMvc.perform(get("/api/v1/artifacts/" + nonExistingId)
                        .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(ArtifactNotFoundException.DEFAULT_MESSAGE + nonExistingId))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Order(3)
    void testFindAllArtifactsSuccess() throws Exception {
        given(this.artifactService.findAll()).willReturn(this.artifactDtos);

        mockMvc.perform(get("/api/v1/artifacts")
                .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.artifactDtos.size())))
                .andExpect(jsonPath("$.data[0].id").value(artifactDtos.get(0).id()))
                .andExpect(jsonPath("$.data[0].name").value(artifactDtos.get(0).name()))
                .andExpect(jsonPath("$.data[1].id").value(artifactDtos.get(1).id()))
                .andExpect(jsonPath("$.data[1].name").value(artifactDtos.get(1).name()));
    }

    @Test
    @Order(4)
    void testAddArtifactSuccess() throws Exception {
        var inputDto = ArtifactDto.builder()
                .name("name")
                .description("description")
                .imageUrl("ImageUrl")
                .build();
        var savedDto = ArtifactDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .imageUrl("ImageUrl")
                .build();
        String json = objectMapper.writeValueAsString(inputDto);

        given(artifactService.save(any(ArtifactDto.class))).willReturn(savedDto);

        mockMvc.perform(post("/api/v1/artifacts")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").value(savedDto.id()))
                .andExpect(jsonPath("$.data.name").value(savedDto.name()))
                .andExpect(jsonPath("$.data.description").value(savedDto.description()))
                .andExpect(jsonPath("$.data.imageUrl").value(savedDto.imageUrl()))
                .andExpect(jsonPath("$.data.owner").isEmpty());
    }

    @Test
    @Order(5)
    void testAddArtifactInvalidArguments() throws Exception {
        var inputDto = ArtifactDto.builder()
                // no name
                // no description
                .imageUrl("ImageUrl")
                .build();
        String json = objectMapper.writeValueAsString(inputDto);

        mockMvc.perform(post("/api/v1/artifacts")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details"))
                .andExpect(jsonPath("$.data.name").value("Name is required"))
                .andExpect(jsonPath("$.data.description").value("Description is required"));
    }

    @Test
    @Order(6)
    void testUpdateArtifactSuccess() throws Exception {
        Long artifactId = 1L;
        var inputDto = ArtifactDto.builder()
                .name("Deluminator-update")
                .description("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.-update")
                .imageUrl("ImageUrl-update")
                .build();
        var updatedDto = ArtifactDto.builder()
                .id(artifactId)
                .name("Deluminator-update")
                .description("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.-update")
                .imageUrl("ImageUrl-update")
                .build();
        String json = objectMapper.writeValueAsString(inputDto);

        given(artifactService.update(artifactId, inputDto)).willReturn(updatedDto);

        mockMvc.perform(put("/api/v1/artifacts/" + artifactId)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(updatedDto.id()))
                .andExpect(jsonPath("$.data.name").value(updatedDto.name()))
                .andExpect(jsonPath("$.data.description").value(updatedDto.description()))
                .andExpect(jsonPath("$.data.imageUrl").value(updatedDto.imageUrl()))
                .andExpect(jsonPath("$.data.owner").isEmpty());
    }

    @Test
    @Order(6)
    void testUpdateArtifactErrorWithNoExistingId() throws Exception {
        Long nonExistingId = -1L;
        var inputDto = ArtifactDto.builder()
                .name("Deluminator-update")
                .description("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.-update")
                .imageUrl("ImageUrl-update")
                .build();
        String json = objectMapper.writeValueAsString(inputDto);

        given(artifactService.update(nonExistingId, inputDto))
                .willThrow(new ArtifactNotFoundException(nonExistingId));

        mockMvc.perform(put("/api/v1/artifacts/" + nonExistingId)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(ArtifactNotFoundException.DEFAULT_MESSAGE + nonExistingId))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Order(7)
    void testDeleteArtifactSuccess() throws Exception {
        //Given
        Long artifactId = 1L;
        doNothing().when(artifactService).delete(artifactId);

        //When Then
        mockMvc.perform(delete("/api/v1/artifacts/" + artifactId)
                        .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.NO_CONTENT.value()))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Order(8)
    void testDeleteArtifactErrorWithNoExistingId() throws Exception {
        //Given
        Long artifactId = 1L;
        doThrow(new ArtifactNotFoundException(artifactId))
                .when(artifactService).delete(artifactId);

        //When Then
        mockMvc.perform(delete("/api/v1/artifacts/" + artifactId)
                        .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(ArtifactNotFoundException.DEFAULT_MESSAGE + artifactId))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
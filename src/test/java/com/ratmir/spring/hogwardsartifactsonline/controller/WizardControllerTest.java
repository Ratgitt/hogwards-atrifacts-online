package com.ratmir.spring.hogwardsartifactsonline.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratmir.spring.hogwardsartifactsonline.dto.WizardDto;
import com.ratmir.spring.hogwardsartifactsonline.service.WizardService;
import com.ratmir.spring.hogwardsartifactsonline.util.exception.ObjectNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.ratmir.spring.hogwardsartifactsonline.util.exception.ObjectNotFoundException.*;
import static com.ratmir.spring.hogwardsartifactsonline.util.exception.handler.ExceptionHandlerAdvice.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WizardControllerTest {

    @Value("${api.endpoint.base-url}")
    private String BASE_URL;
    
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    WizardService wizardService;

    @Autowired
    ObjectMapper objectMapper;

    WizardDto wizardDto;

    @BeforeEach
    void setUp() {
        wizardDto = WizardDto.builder()
                .id(1L)
                .name("Albus Dumbledore")
                .numberOfArtifacts(0)
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Order(1)
    void testFindWizardByIdSuccess() throws Exception {
        given(wizardService.findById(any())).willReturn(wizardDto);

        mockMvc.perform(get(BASE_URL + "/wizards/1")
                        .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Albus Dumbledore"))
                .andExpect(jsonPath("$.data.numberOfArtifacts").value(0));
    }

    @Test
    @Order(2)
    void testFindWizardByIdNotFound() throws Exception {
        Long nonExistingId = 1L;
        given(wizardService.findById(any()))
                .willThrow(new ObjectNotFoundException("wizard", nonExistingId));

        mockMvc.perform(get(BASE_URL + "/wizards/" + nonExistingId)
                        .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(WIZARD_NOT_FOUND_MESSAGE + nonExistingId))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Order(3)
    void testFindAllWizards() throws Exception {
        List<WizardDto> wizardDtos = List.of(
                new WizardDto(1L, "Albus Dumbledore", 2),
                new WizardDto(2L, "Harry Potter", 2),
                new WizardDto(3L, "Neville Longbottom", 1)
        );

        given(wizardService.findAll())
                .willReturn(wizardDtos);

        mockMvc.perform(get(BASE_URL + "/wizards")
                        .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[0].name").value("Albus Dumbledore"))
                .andExpect(jsonPath("$.data[0].numberOfArtifacts").value(2))
                .andExpect(jsonPath("$.data[1].id").value(2L))
                .andExpect(jsonPath("$.data[1].name").value("Harry Potter"))
                .andExpect(jsonPath("$.data[1].numberOfArtifacts").value(2))
                .andExpect(jsonPath("$.data[2].id").value(3L))
                .andExpect(jsonPath("$.data[2].name").value("Neville Longbottom"))
                .andExpect(jsonPath("$.data[2].numberOfArtifacts").value(1));
    }

    @Test
    @Order(4)
    void testAddWizardSuccess() throws Exception {
        WizardDto inputDto = new WizardDto(null, "Albus Dumbledore", null);
        WizardDto expectedDto = new WizardDto(1L, "Albus Dumbledore", 0);

        var json = objectMapper.writeValueAsString(inputDto);

        given(wizardService.save(any())).willReturn(expectedDto);

        mockMvc.perform(post(BASE_URL + "/wizards")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("Albus Dumbledore"))
                .andExpect(jsonPath("$.data.numberOfArtifacts").value(0));
    }

    @Test
    @Order(5)
    void testAddWizardInvalidArgumentsError() throws Exception {
        WizardDto inputDto = new WizardDto(null, null, null);

        var json = objectMapper.writeValueAsString(inputDto);

        mockMvc.perform(post(BASE_URL + "/wizards")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(VALIDATION_ERROR_MESSAGE))
                .andExpect(jsonPath("$.data.name").value("Name is required"));
    }

    @Test
    @Order(6)
    void testUpdateWizardSuccess() throws Exception {
        Long wizardId = 1L;
        WizardDto inputDto = new WizardDto(null, "New Name", null);
        WizardDto expectedWizardDto = new WizardDto(1L, "New Name", 0);

        String json = objectMapper.writeValueAsString(inputDto);

        given(wizardService.update(wizardId, inputDto)).willReturn(expectedWizardDto);

        mockMvc.perform(put(BASE_URL + "/wizards/" + wizardId)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(expectedWizardDto.id()))
                .andExpect(jsonPath("$.data.name").value(expectedWizardDto.name()))
                .andExpect(jsonPath("$.data.numberOfArtifacts").value(expectedWizardDto.numberOfArtifacts()));
    }

    @Test
    @Order(6)
    void testUpdateWizardInvalidArgumentsError() throws Exception {
        long wizardId = 1L;
        WizardDto inputDto = new WizardDto(null, null, null);

        String json = objectMapper.writeValueAsString(inputDto);

        mockMvc.perform(put(BASE_URL + "/wizards/" + wizardId)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(VALIDATION_ERROR_MESSAGE))
                .andExpect(jsonPath("$.data.name").value("Name is required"));
    }

    @Test
    @Order(7)
    void testUpdateWizardErrorWithNoExistingId() throws Exception {
        Long nonExistingId = 1L;
        WizardDto inputDto = new WizardDto(null, "New Name", null);

        String json = objectMapper.writeValueAsString(inputDto);

        given(wizardService.update(nonExistingId, inputDto))
                .willThrow(new ObjectNotFoundException("wizard", nonExistingId));

        mockMvc.perform(put(BASE_URL + "/wizards/" + nonExistingId)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(WIZARD_NOT_FOUND_MESSAGE + nonExistingId))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Order(8)
    void testDeleteWizardSuccess() throws Exception {
        Long wizardId = 4L;
        doNothing().when(wizardService).delete(wizardId);

        mockMvc.perform(delete(BASE_URL + "/wizards/" + wizardId)
                        .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.NO_CONTENT.value()))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Order(9)
    void testDeleteWizardNotFoundError() throws Exception {
        Long nonExistingId = 4L;
        doThrow(new ObjectNotFoundException("wizard", nonExistingId))
                .when(wizardService).delete(nonExistingId);

        mockMvc.perform(delete(BASE_URL + "/wizards/" + nonExistingId)
                        .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(WIZARD_NOT_FOUND_MESSAGE + nonExistingId))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Order(10)
    void testAssignArtifactSuccess() throws Exception {
        doNothing().when(wizardService).assignArtifact(2L, 3L);

        mockMvc.perform(put(BASE_URL + "/wizards/2/artifacts/3")
                        .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Artifact Assignment Success"))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    @Test
    @Order(11)
    void testAssignArtifactErrorWithNonExistingWizardId() throws Exception {

        Long nonExistingWizardId = 5L;
        Long artifactId = 2L;

        doThrow(new ObjectNotFoundException("wizard", nonExistingWizardId))
                .when(wizardService).assignArtifact(nonExistingWizardId, artifactId);

        mockMvc.perform(put(BASE_URL + "/wizards/" + nonExistingWizardId +"/artifacts/" + artifactId)
                        .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(WIZARD_NOT_FOUND_MESSAGE + nonExistingWizardId))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Order(12)
    void testAssignArtifactErrorWithNonExistingArtifactId() throws Exception {
        Long wizardId = 5L;
        Long nonExistingArtifactId = 2L;

        doThrow(new ObjectNotFoundException("artifact", nonExistingArtifactId))
                .when(wizardService).assignArtifact(wizardId, nonExistingArtifactId);

        mockMvc.perform(put(BASE_URL + "/wizards/" + wizardId +"/artifacts/" + nonExistingArtifactId)
                        .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(ARTIFACT_NOT_FOUND_MESSAGE + nonExistingArtifactId))
                .andExpect(jsonPath("$.data").isEmpty());

    }
}
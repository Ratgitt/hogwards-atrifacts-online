package com.ratmir.spring.hogwardsartifactsonline.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratmir.spring.hogwardsartifactsonline.dto.CreateUserDto;
import com.ratmir.spring.hogwardsartifactsonline.dto.UserDto;
import com.ratmir.spring.hogwardsartifactsonline.service.UserService;
import com.ratmir.spring.hogwardsartifactsonline.util.exception.ObjectNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
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
public class UserControllerTest {

    @Value("${api.endpoint.base-url}")
    private String BASE_URL;

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    List<UserDto> userDtos;

    @BeforeEach
    void setUp() {
        userDtos = new ArrayList<>();
        userDtos.addAll(List.of(
                new UserDto(
                        1L,
                        "john",
                        true,
                        "admin user"
                ),
                new UserDto(
                        2L,
                        "eric",
                        true,
                        "user"
                ),
                new UserDto(
                        3L,
                        "tom",
                        false,
                        "user"
                )
        ));
    }

    @Test
    @Order(1)
    void testFindAllUsersSuccess() throws Exception {
        given(userService.findAll()).willReturn(userDtos);

        mockMvc.perform(get(BASE_URL + "/users")
                        .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data[0].id").value(userDtos.get(0).id()))
                .andExpect(jsonPath("$.data[0].username").value(userDtos.get(0).username()))
                .andExpect(jsonPath("$.data[0].enabled").value(userDtos.get(0).enabled()))
                .andExpect(jsonPath("$.data[0].roles").value(userDtos.get(0).roles()))
                .andExpect(jsonPath("$.data[1].id").value(userDtos.get(1).id()))
                .andExpect(jsonPath("$.data[1].username").value(userDtos.get(1).username()))
                .andExpect(jsonPath("$.data[1].enabled").value(userDtos.get(1).enabled()))
                .andExpect(jsonPath("$.data[1].roles").value(userDtos.get(1).roles()))
                .andExpect(jsonPath("$.data[2].id").value(userDtos.get(2).id()))
                .andExpect(jsonPath("$.data[2].username").value(userDtos.get(2).username()))
                .andExpect(jsonPath("$.data[2].enabled").value(userDtos.get(2).enabled()))
                .andExpect(jsonPath("$.data[2].roles").value(userDtos.get(2).roles()));
    }

    @Test
    @Order(2)
    void testFindUserByIdSuccess() throws Exception {
        Long userId = 2L;
        UserDto foundUserDto = userDtos.get(1);

        given(userService.findById(userId)).willReturn(foundUserDto);

        mockMvc.perform(get(BASE_URL + "/users/" + userId)
                        .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(foundUserDto.id()))
                .andExpect(jsonPath("$.data.username").value(foundUserDto.username()))
                .andExpect(jsonPath("$.data.enabled").value(foundUserDto.enabled()))
                .andExpect(jsonPath("$.data.roles").value(foundUserDto.roles()));
    }

    @Test
    @Order(3)
    void testFindUserByIdNotFoundError() throws Exception {
        Long nonExistingId = 10L;

        given(userService.findById(nonExistingId))
                .willThrow(new ObjectNotFoundException("user", nonExistingId));

        mockMvc.perform(get(BASE_URL + "/users/" + nonExistingId)
                        .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(USER_NOT_FOUND_MESSAGE + nonExistingId))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Order(4)
    void testCreateUserSuccess() throws Exception {
        var createUserDto = new CreateUserDto(
                "lily",
                "ABCDEfghi123456*()",
                true,
                "user"
        );
        var savedUserDto = new UserDto(
                4L,
                "lily",
                true,
                "user"
        );
        var json = objectMapper.writeValueAsString(createUserDto);

        given(userService.save(createUserDto)).willReturn(savedUserDto);

        mockMvc.perform(post(BASE_URL + "/users")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").value(savedUserDto.id()))
                .andExpect(jsonPath("$.data.username").value(savedUserDto.username()))
                .andExpect(jsonPath("$.data.enabled").value(savedUserDto.enabled()))
                .andExpect(jsonPath("$.data.roles").value(savedUserDto.roles()));
    }

    @Test
    @Order(5)
    void testCreateUserInvalidArgumentsError() throws Exception {
        // Пустая сущность
        var invalidCreateUserDto = CreateUserDto.builder().build();
        var json = objectMapper.writeValueAsString(invalidCreateUserDto);

        mockMvc.perform(post(BASE_URL + "/users")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(VALIDATION_ERROR_MESSAGE))
                .andExpect(jsonPath("$.data.username").value("Username is required"))
                .andExpect(jsonPath("$.data.password").value("Password is required"))
                .andExpect(jsonPath("$.data.roles").value("Roles are required"));
    }

    @Test
    @Order(6)
    void testUpdateUserSuccess() throws Exception {
        var userId = 1L;
        var updateUserDto = UserDto.builder()
                .username("john-update")
                .enabled(true)
                .roles("admin user")
                .build();
        var expectedUpdatedUserDto = UserDto.builder()
                .id(1L)
                .username("john-update")
                .enabled(true)
                .roles("admin user")
                .build();

        given(userService.update(userId, updateUserDto)).willReturn(expectedUpdatedUserDto);
        var json = objectMapper.writeValueAsString(updateUserDto);

        mockMvc.perform(put(BASE_URL + "/users/" + userId)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(expectedUpdatedUserDto.id()))
                .andExpect(jsonPath("$.data.username").value(expectedUpdatedUserDto.username()))
                .andExpect(jsonPath("$.data.enabled").value(expectedUpdatedUserDto.enabled()))
                .andExpect(jsonPath("$.data.roles").value(expectedUpdatedUserDto.roles()));
    }

    @Test
    @Order(7)
    void testUpdateUserInvalidArgumentsError() throws Exception {
        var userId = 1L;
        // Пустая сущность
        var invalidUpdateUserDto = UserDto.builder().build();
        var json = objectMapper.writeValueAsString(invalidUpdateUserDto);

        mockMvc.perform(put(BASE_URL + "/users/" + userId)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(VALIDATION_ERROR_MESSAGE))
                .andExpect(jsonPath("$.data.username").value("Username is required"))
                .andExpect(jsonPath("$.data.roles").value("Roles are required"));
    }

    @Test
    @Order(8)
    void testUpdateUserNotFoundError() throws Exception {
        Long nonExistingId = 10L;
        var updateUserDto = UserDto.builder()
                .username("john-update")
                .enabled(true)
                .roles("admin user")
                .build();
        var json = objectMapper.writeValueAsString(updateUserDto);

        given(userService.update(nonExistingId, updateUserDto))
                .willThrow(new ObjectNotFoundException("user", nonExistingId));

        mockMvc.perform(put(BASE_URL + "/users/" + nonExistingId)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(USER_NOT_FOUND_MESSAGE + nonExistingId))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Order(9)
    void testDeleteUserSuccess() throws Exception {
        Long userId = 3L;
        doNothing().when(userService).delete(userId);

        mockMvc.perform(delete(BASE_URL + "/users/" + userId)
                        .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(HttpStatus.NO_CONTENT.value()))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @Order(10)
    void testDeleteUserNotFoundError() throws Exception {
        Long nonExistingId = 10L;
        doThrow(new ObjectNotFoundException("user", nonExistingId))
                .when(userService).delete(nonExistingId);

        mockMvc.perform(delete(BASE_URL + "/users/" + nonExistingId)
                        .accept(APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(USER_NOT_FOUND_MESSAGE + nonExistingId))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}

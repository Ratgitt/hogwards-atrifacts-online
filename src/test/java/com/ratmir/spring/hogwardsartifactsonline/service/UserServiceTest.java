package com.ratmir.spring.hogwardsartifactsonline.service;

import com.ratmir.spring.hogwardsartifactsonline.dto.CreateUserDto;
import com.ratmir.spring.hogwardsartifactsonline.dto.UserDto;
import com.ratmir.spring.hogwardsartifactsonline.entity.HogwardsUser;
import com.ratmir.spring.hogwardsartifactsonline.repository.UserRepository;
import com.ratmir.spring.hogwardsartifactsonline.util.converter.UserConverter;
import com.ratmir.spring.hogwardsartifactsonline.util.exception.ObjectNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ratmir.spring.hogwardsartifactsonline.util.exception.ObjectNotFoundException.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserConverter userConverter;

    @InjectMocks
    UserService userService;

    List<HogwardsUser> hogwardsUsers;
    List<UserDto> userDtos;

    @BeforeEach
    void setUp() {
        hogwardsUsers = new ArrayList<>();
        userDtos = new ArrayList<>();

        hogwardsUsers.addAll(List.of(
                new HogwardsUser(
                        1L,
                        "john",
                        "124245",
                        true,
                        "admin user"
                ),
                new HogwardsUser(
                        2L,
                        "eric",
                        "238587",
                        true,
                        "user"
                ),
                new HogwardsUser(
                        3L,
                        "tom",
                        "9898776",
                        false,
                        "user"
                )
        ));
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

    @AfterEach
    void tearDown() {
    }

    @Test
    @Order(1)
    void testFindAllSuccess() {
        given(userRepository.findAll()).willReturn(hogwardsUsers);
        given(userConverter.toUserDto(hogwardsUsers.get(0))).willReturn(userDtos.get(0));
        given(userConverter.toUserDto(hogwardsUsers.get(1))).willReturn(userDtos.get(1));
        given(userConverter.toUserDto(hogwardsUsers.get(2))).willReturn(userDtos.get(2));

        List<UserDto> actualUserDtos = userService.findAll();

        assertThat(actualUserDtos).isEqualTo(userDtos);
        verify(userRepository, times(1)).findAll();
        verify(userConverter, times(1)).toUserDto(hogwardsUsers.get(0));
        verify(userConverter, times(1)).toUserDto(hogwardsUsers.get(1));
        verify(userConverter, times(1)).toUserDto(hogwardsUsers.get(2));
    }

    @Test
    @Order(2)
    void testFindByIdSuccess() {
        HogwardsUser hogwardsUser = hogwardsUsers.getFirst();
        UserDto userDto = userDtos.getFirst();

        given(userRepository.findById(hogwardsUser.getId())).willReturn(Optional.of(hogwardsUser));
        given(userConverter.toUserDto(hogwardsUser)).willReturn(userDto);

        UserDto actualUserDto = userService.findById(hogwardsUser.getId());

        assertThat(actualUserDto).isEqualTo(userDto);
        verify(userRepository, times(1)).findById(hogwardsUser.getId());
        verify(userConverter, times(1)).toUserDto(hogwardsUser);
    }

    @Test
    @Order(3)
    void testFindByIdNotFoundError() {
        Long nonExistingId = 10L;

        given(userRepository.findById(nonExistingId))
                .willThrow(new ObjectNotFoundException("user", nonExistingId));

        var exception = assertThrows(ObjectNotFoundException.class,
                () -> userService.findById(nonExistingId));

        assertThat(exception.getMessage()).isEqualTo(USER_NOT_FOUND_MESSAGE + nonExistingId);

        verify(userRepository, times(1)).findById(nonExistingId);
        verify(userConverter, never()).toUserDto(any());
    }

    @Test
    @Order(4)
    void testCreateNewUserSuccess() {
        var createUserDto = new CreateUserDto("lily", "ABCDEfghi123456*()", true, "user");
        var convertedHogwardsUser = new HogwardsUser("lily", "ABCDEfghi123456*()", true, "user");
        var createdHogwardsUser = new HogwardsUser(4L, "lily", "ABCDEfghi123456*()", true, "user");
        var expectedSavedUserDto = new UserDto(4L, "lily", true, "user");

        given(userConverter.toHogwardsUser(createUserDto)).willReturn(convertedHogwardsUser);
        given(userRepository.save(convertedHogwardsUser)).willReturn(createdHogwardsUser);
        given(userConverter.toUserDto(createdHogwardsUser)).willReturn(expectedSavedUserDto);

        var savedUserDto = userService.save(createUserDto);

        assertThat(savedUserDto).isEqualTo(expectedSavedUserDto);
        verify(userConverter, times(1)).toHogwardsUser(createUserDto);
        verify(userRepository, times(1)).save(convertedHogwardsUser);
        verify(userConverter, times(1)).toUserDto(createdHogwardsUser);
    }

    @Test
    @Order(5)
    void testUpdateUserSuccess() {
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
        var foundHogwardsUser = HogwardsUser.builder()
                .id(1L)
                .username("john")
                .enabled(true)
                .roles("user")
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.of(foundHogwardsUser));
        given(userConverter.toUserDto(any())).willReturn(expectedUpdatedUserDto);

        var actualUpdatedUserDto = userService.update(userId, updateUserDto);

        assertThat(actualUpdatedUserDto).isEqualTo(expectedUpdatedUserDto);
        verify(userRepository, times(1)).findById(userId);
        verify(userConverter, times(1)).toUserDto(any());
    }

    @Test
    @Order(6)
    void testUpdateUserNotFoundError() {
        Long nonExistingId = 10L;
        var updateUserDto = UserDto.builder()
                .username("john-update")
                .enabled(true)
                .roles("admin user")
                .build();

        given(userRepository.findById(nonExistingId))
                .willThrow(new ObjectNotFoundException("user", nonExistingId));

        var exception = assertThrows(ObjectNotFoundException.class,
                () -> userService.update(nonExistingId, updateUserDto));

        assertThat(exception.getMessage()).isEqualTo(USER_NOT_FOUND_MESSAGE + nonExistingId);

        verify(userRepository, times(1)).findById(nonExistingId);
        verify(userConverter, never()).toUserDto(any());
    }

    @Test
    @Order(7)
    void testDeleteUserSuccess() {
        Long userId = 3L;
        HogwardsUser hogwardsUser = hogwardsUsers.get(2);

        given(userRepository.findById(userId)).willReturn(Optional.of(hogwardsUser));
        doNothing().when(userRepository).deleteById(userId);

        userService.delete(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @Order(8)
    void testDeleteUserNotFoundError() {
        Long nonExistingId = 10L;
        given(userRepository.findById(nonExistingId))
                .willThrow(new ObjectNotFoundException("user", nonExistingId));

        var exception = assertThrows(ObjectNotFoundException.class,
                () -> userService.delete(nonExistingId));

        assertThat(exception.getMessage()).isEqualTo(USER_NOT_FOUND_MESSAGE + nonExistingId);

        verify(userRepository, times(1)).findById(nonExistingId);
    }
}

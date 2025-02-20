package com.ratmir.spring.hogwardsartifactsonline.service;

import com.ratmir.spring.hogwardsartifactsonline.dto.CreateUserDto;
import com.ratmir.spring.hogwardsartifactsonline.dto.UserDto;
import com.ratmir.spring.hogwardsartifactsonline.entity.HogwardsUser;
import com.ratmir.spring.hogwardsartifactsonline.repository.UserRepository;
import com.ratmir.spring.hogwardsartifactsonline.util.converter.UserConverter;
import com.ratmir.spring.hogwardsartifactsonline.util.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(userConverter::toUserDto)
                .collect(toList());
    }

    public UserDto findById(Long userId) {
        return userRepository.findById(userId)
                .map(userConverter::toUserDto)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    public UserDto save(CreateUserDto createUserDto) {
        // TODO: encode password before saving
        HogwardsUser hogwardsUser = userConverter.toHogwardsUser(createUserDto);
        HogwardsUser savedHogwardsUser = userRepository.save(hogwardsUser);
        return userConverter.toUserDto(savedHogwardsUser);
    }

    public UserDto update(Long userId, UserDto userDto) {
        // TODO: check if username is unique
        HogwardsUser foundHogwardsUser = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));

        foundHogwardsUser.setUsername(userDto.username());
        foundHogwardsUser.setEnabled(userDto.enabled());
        foundHogwardsUser.setRoles(userDto.roles());

        return userConverter.toUserDto(foundHogwardsUser);
    }


    public void delete(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
        userRepository.deleteById(userId);
    }
}

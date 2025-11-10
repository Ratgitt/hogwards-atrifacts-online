package com.ratmir.spring.hogwardsartifactsonline.service;

import com.ratmir.spring.hogwardsartifactsonline.dto.CreateUserDto;
import com.ratmir.spring.hogwardsartifactsonline.dto.UserDto;
import com.ratmir.spring.hogwardsartifactsonline.entity.HogwardsUser;
import com.ratmir.spring.hogwardsartifactsonline.repository.HogwardsUserRepository;
//import com.ratmir.spring.hogwardsartifactsonline.security.HogwardsUserDetails;
import com.ratmir.spring.hogwardsartifactsonline.util.converter.UserConverter;
import com.ratmir.spring.hogwardsartifactsonline.util.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class HogwartsUserService
//        implements UserDetailsService
{

    private final HogwardsUserRepository userRepository;
    private final UserConverter userConverter;

//    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserDto findById(Long userId) {
        return userRepository.findById(userId)
                .map(userConverter::toUserDto)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(userConverter::toUserDto)
                .collect(toList());
    }

    @Transactional
    public UserDto save(CreateUserDto createUserDto) {
        HogwardsUser hogwardsUser = userConverter.toHogwardsUser(createUserDto);
//        hogwardsUser.setPassword(passwordEncoder.encode(hogwardsUser.getPassword()));

        HogwardsUser savedHogwardsUser = userRepository.save(hogwardsUser);
        return userConverter.toUserDto(savedHogwardsUser);
    }

    @Transactional
    public UserDto update(Long userId, UserDto userDto) {
        // TODO: check if username is unique
        HogwardsUser foundHogwardsUser = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));

        foundHogwardsUser.setUsername(userDto.username());
        foundHogwardsUser.setEnabled(userDto.enabled());
        foundHogwardsUser.setRoles(userDto.roles());

        return userConverter.toUserDto(foundHogwardsUser);
    }

    @Transactional
    public void delete(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("user", userId));
        userRepository.deleteById(userId);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findByUsername(username)
//                .map(HogwardsUserDetails::new)
//                .orElseThrow(() -> new UsernameNotFoundException("Could not find user with username: " + username));
//    }
}

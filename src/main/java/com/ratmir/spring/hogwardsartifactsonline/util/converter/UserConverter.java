package com.ratmir.spring.hogwardsartifactsonline.util.converter;

import com.ratmir.spring.hogwardsartifactsonline.dto.CreateUserDto;
import com.ratmir.spring.hogwardsartifactsonline.dto.UserDto;
import com.ratmir.spring.hogwardsartifactsonline.entity.HogwardsUser;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserDto toUserDto(HogwardsUser source) {
        return UserDto.builder()
                .id(source.getId())
                .username(source.getUsername())
                .enabled(source.getEnabled())
                .roles(source.getRoles())
                .build();
    }

    public HogwardsUser toHogwardsUser(UserDto source) {
        return HogwardsUser.builder()
                .username(source.username())
                .enabled(source.enabled())
                .roles(source.roles())
                .build();
    }

    public HogwardsUser toHogwardsUser(CreateUserDto source) {
        return HogwardsUser.builder()
                .username(source.username())
                .password(source.password())
                .enabled(source.enabled())
                .roles(source.roles())
                .build();
    }
}

package com.ratmir.spring.hogwardsartifactsonline.a_spring_course.mapper;

import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.dto.UserCreateDto;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity.Role;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity.User;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.dto.UserReadDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import static org.mapstruct.MappingConstants.ComponentModel.*;

@Mapper(componentModel = SPRING,
        uses = CompanyMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    @Mapping(source = "company", target = "companyReadDto")
    UserReadDto entityToReadDto(User entity);

    default User createDtoToEntity(UserCreateDto dto) {
        return User.builder()
                .username(dto.getUsername())
                .birthDate(dto.getBirthDate())
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .role(Role.USER)
                .build();
    }
}
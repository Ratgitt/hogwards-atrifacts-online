package com.ratmir.spring.hogwardsartifactsonline.a_spring_course.mapper;

import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity.Company;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.dto.CompanyReadDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mapstruct.MappingConstants.ComponentModel.*;

@Mapper(componentModel = SPRING)
public interface CompanyMapper {

    CompanyReadDto entityToReadDto(Company company);

    // MapStruct будет использовать этот метод для преобразования Map -> Map
    default Map<String, String> mapLocales(Map<String, String> locales) {
        // копия, безопасная после закрытия сессии
        return locales == null ? Collections.emptyMap() : new HashMap<>(locales);
    }
}
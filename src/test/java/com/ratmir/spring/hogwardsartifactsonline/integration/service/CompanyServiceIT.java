package com.ratmir.spring.hogwardsartifactsonline.integration.service;

import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.dto.CompanyReadDto;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.service.CompanyService;
import com.ratmir.spring.hogwardsartifactsonline.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
public class CompanyServiceIT extends IntegrationTestBase {

    private static final Integer COMPANY_ID = 1;

    private final CompanyService companyService;

    @Test
    void findByIdSuccess() {

        var actualResult = companyService.findById(COMPANY_ID);

        var expectedResult = new CompanyReadDto(
                1,
                "Google",
                Map.of(
                        "en", "Google description",
                        "ru", "Google описание"
                ));

        assertEquals(expectedResult, actualResult);
    }
}

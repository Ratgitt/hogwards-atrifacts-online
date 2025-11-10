package com.ratmir.spring.hogwardsartifactsonline.integration;

import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.mapper.CompanyMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@TestConfiguration
public class TestApplicationRunner {

    @MockitoBean(name = "companyMapper")
    private CompanyMapper companyMapper;
}

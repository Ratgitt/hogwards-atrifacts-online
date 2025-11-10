package com.ratmir.spring.hogwardsartifactsonline.integration.service;

import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.mapper.CompanyMapper;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.service.UserService;
import com.ratmir.spring.hogwardsartifactsonline.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
public class UserServiceIT extends IntegrationTestBase {

    private final UserService userService;
    private final CompanyMapper companyMapper;

    @Test
    void test() {
        System.out.println();
    }
}

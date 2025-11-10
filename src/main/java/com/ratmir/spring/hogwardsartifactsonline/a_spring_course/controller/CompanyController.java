package com.ratmir.spring.hogwardsartifactsonline.a_spring_course.controller;

import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.service.CompanyService;
import com.ratmir.spring.hogwardsartifactsonline.dto.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.base-url}/companies")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/{companyId}")
    public Result findCompanyById(@PathVariable Integer companyId) {
        var companyDto = companyService.findById(companyId);
        return Result.builder()
                .flag(true)
                .code(HttpStatus.OK.value())
                .message("Find One Success")
                .data(companyDto)
                .build();
    }
}

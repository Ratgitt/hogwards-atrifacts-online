package com.ratmir.spring.hogwardsartifactsonline.a_spring_course.service;

import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.dto.CompanyReadDto;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.repository.CompanyRepository;
import com.ratmir.spring.hogwardsartifactsonline.util.exception.ObjectNotFoundException;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.mapper.CompanyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Transactional(readOnly = true)
    public CompanyReadDto findById(Integer id) {
        return companyRepository.findById(id)
                .map(companyMapper::entityToReadDto)
                .orElseThrow(() -> new ObjectNotFoundException("company", id));
    }
}

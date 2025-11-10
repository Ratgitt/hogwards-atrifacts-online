package com.ratmir.spring.hogwardsartifactsonline.service;

import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity.Company;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

//    private static final Integer COMPANY_ID = 1;
//
//    @Mock
//    private CrudRepository<Integer, Company> companyRepository;
//    @Mock
//    private UserService userService;
//    @Mock
//    private ApplicationEventPublisher eventPublisher;
//    @InjectMocks
//    private CompanyService companyService;

//    @Test
//    void findById() {
//        doReturn(Optional.of(new Company(COMPANY_ID)))
//                .when(companyRepository).findById(COMPANY_ID);
//
//        var actualResult = companyService.findById(COMPANY_ID);
//
//        assertTrue(actualResult.isPresent());
//
//        var expectedResult = new CompanyReadDto(COMPANY_ID);
//        actualResult.ifPresent(actual -> assertEquals(expectedResult, actual));
//
//        verify(eventPublisher).publishEvent(any(EntityEvent.class));
//        verifyNoMoreInteractions(eventPublisher, userService);
//    }
}


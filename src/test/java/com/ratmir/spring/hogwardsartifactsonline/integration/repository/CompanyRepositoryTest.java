package com.ratmir.spring.hogwardsartifactsonline.integration.repository;

import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity.Company;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.repository.CompanyRepository;
import com.ratmir.spring.hogwardsartifactsonline.integration.IntegrationTestBase;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class CompanyRepositoryTest extends IntegrationTestBase {

    private static final Integer GOOGLE_ID = 1;

    private final EntityManager entityManager;
    private final CompanyRepository companyRepository;
    private final TransactionTemplate transactionTemplate;

    @Test
    void checkFindByQueries() {
        companyRepository.findByName("google");
        companyRepository.findAllByNameContainingIgnoreCase("a");
    }

    @Test
    @Disabled
    void delete() {
        Optional<Company> maybeCompany = companyRepository.findById(GOOGLE_ID);
        assertTrue(maybeCompany.isPresent());
        maybeCompany.ifPresent(companyRepository::delete);
        entityManager.flush();
        assertTrue(companyRepository.findById(GOOGLE_ID).isEmpty());
    }

    @Test
    void findById() {
        transactionTemplate.executeWithoutResult(tx -> {
            Optional<Company> optionalCompany = companyRepository.findById(1);

            assertThat(optionalCompany.isPresent()).isTrue();

            Company company = optionalCompany.get();
            assertThat(company.getLocales()).hasSize(2);
        });
    }

    @Test
    void save() {
        var company = Company.builder()
                .name("Apple")
                .locales(Map.of(
                        "ru", "Apple описание",
                        "en", "Apple description"
                ))
                .build();
        companyRepository.save(company);
        assertNotNull(company.getId());
    }
}
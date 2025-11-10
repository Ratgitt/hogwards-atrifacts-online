package com.ratmir.spring.hogwardsartifactsonline.integration.repository;

import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.dto.UserFilter;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity.Role;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity.User;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity.querydsl.QPredicates;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.repository.UserRepository;
import com.ratmir.spring.hogwardsartifactsonline.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity.QUser.user;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class UserRepositoryTest extends IntegrationTestBase {

    private final UserRepository userRepository;

    @Test
    void checkBatch() {
        List<User> users = userRepository.findAll();
        userRepository.updateCompanyAndRole(users);
        System.out.println();
    }

    @Test
    void checkJdbcTemplate() {
        var users = userRepository.findAllByCompanyIdAndRole(1, Role.USER);
        assertThat(users).hasSize(1);
    }

    @Test
    void checkAuditing() {
        User user = userRepository.findById(1L).get();
        user.setBirthDate(user.getBirthDate().plusYears(1L));
        userRepository.flush();
        assertThat(user.getCreatedAt()).isNull();
        assertThat(user.getLastModifiedAt()).isNotNull();
        assertThat(user.getCreatedBy()).isNull();
        assertThat(user.getLastModifiedBy()).isNotNull();
    }

    @Test
    void checkCustomImplementation() {
        UserFilter filter = new UserFilter(
                null,
                "ov",
                LocalDate.now(),
                List.of(1L, 3L)

        );
//        List<User> users = userRepository.findAllByFilter(filter);
//        List<User> users = userRepository.findAllByFilterQueryDsl(filter);

        var predicate = QPredicates.builder()
                .add(filter.firstname(), user.firstname::containsIgnoreCase)
                .add(filter.lastname(), user.lastname::containsIgnoreCase)
                .add(filter.birthDate(), user.birthDate::before)
                .add(filter.chatIds(), ids -> {
                    if(ids == null || ids.isEmpty()) {
                        return null;
                    }
                    return user.userChats.any().chat.id.in(ids);
                })
                .build();
        List<User> users = userRepository.findAll(predicate);

        users.forEach(System.out::println);
        assertThat(users).hasSize(3);
    }

    @Test
    void checkProjections() {
        var users = userRepository.findAllByCompanyId(1);
        System.out.println(users.get(0).getClass().getName());
        int cnt = 1;
        for(var user: users) {
            System.out.println(cnt++);
            System.out.println(user.getFirstname());
            System.out.println(user.getLastname());
            System.out.println(user.getBirthDate());
        }
        assertThat(users).hasSize(2);
    }

    @Test
    void checkPageable() {
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by("id"));
        var slice = userRepository.findAllBy(pageRequest);
        slice.forEach(user -> System.out.println(user.getCompany().getName()));

        while(slice.hasNext()) {
            slice = userRepository.findAllBy(slice.nextPageable());
            slice.forEach(user -> System.out.println(user.getCompany().getName()));
        }
    }

    @Test
    void checkSort() {
        Sort sortByName = Sort.by("firstname").and(Sort.by("lastname"));

        Sort.TypedSort<User> sortBy = Sort.sort(User.class);
        Sort combinedSort = sortBy.by(User::getFirstname).ascending()
                .and(sortBy.by(User::getLastname).ascending());

        List<User> allUsers = userRepository.findTop3ByBirthDateBefore(LocalDate.now(), combinedSort);
        assertThat(allUsers).hasSize(3);
    }

    @Test
    void checkFirstTop() {
        Optional<User> topUser = userRepository.findFirstByOrderByIdDesc();
        assertTrue(topUser.isPresent());
        topUser.ifPresent(user -> assertEquals(5L, user.getId()));
    }

    @Test
    void checkUpdate() {
        User ivan = userRepository.getReferenceById(1L);
        assertSame(Role.ADMIN, ivan.getRole());
        ivan.setBirthDate(LocalDate.now());

        int resultCount = userRepository.updateRole(Role.USER, 1L, 5L);
        assertEquals(2, resultCount);

//        LazyInitializationException
//        ivan.getCompany().getName();

        User sameIvan = userRepository.getReferenceById(1L);
        assertSame(Role.USER, sameIvan.getRole());
    }

    @Test
    void checkQueries() {
        List<User> users = userRepository.findAllBy("a", "ov");
        assertThat(users).hasSize(3);
    }
}
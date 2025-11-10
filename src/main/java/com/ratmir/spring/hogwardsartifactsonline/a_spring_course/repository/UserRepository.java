package com.ratmir.spring.hogwardsartifactsonline.a_spring_course.repository;

import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.dto.PersonalInfo2;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity.Role;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity.User;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.ListQuerydslPredicateExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hibernate.jpa.AvailableHints.*;
import static org.springframework.data.jpa.repository.EntityGraph.*;

@Repository
public interface UserRepository extends
        JpaRepository<User, Long>,
        FilterUserRepository,
        RevisionRepository<User, Long, Integer>,
        ListQuerydslPredicateExecutor<User> {

    boolean existsByUsername(String username);

    @Query("select u from User u " +
            "where u.firstname like %:firstname% and u.lastname like %:lastname%")
    List<User> findAllBy(String firstname, String lastname);

    @Query(value = "SELECT u.* FROM spring_course.users u WHERE username = :username",
            nativeQuery = true)
    List<User> findAllByUsername(String username);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update User u set u.role = :role where u.id in (:ids)")
    int updateRole(Role role, Long... ids);

    Optional<User> findFirstByOrderByIdDesc();

    @QueryHints(
            @QueryHint(name = HINT_FETCH_SIZE, value = "50")
    )
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<User> findTop3ByBirthDateBefore(LocalDate birthDate, Sort sort);

//    @EntityGraph("User.company")
    @EntityGraph(attributePaths = {"company", "company.locales"},
                type = EntityGraphType.FETCH)
    @Query(value = "select u from User u",
    countQuery = "select count(distinct u.firstname) from User u")
    Page<User> findAllBy(Pageable pageable);

//    List<PersonalInfo> findAllByCompanyId(Integer companyId);

//    <T> List<T> findAllByCompanyId(Integer companyId, Class<T> clazz);

//    @Query(nativeQuery = true, value =
//            "SELECT " +
//            "u.firstname, " +
//            "u.lastname, " +
//            "u.birth_date " +
//            "FROM users u " +
//            "WHERE company_id = :companyId")
    @Query(nativeQuery = true, value =
                    "select u.firstname, u.lastname, u.birth_date as birthDate " +
                    "from spring_course.users u " +
                    "where u.company_id = :companyId")
    List<PersonalInfo2> findAllByCompanyId(Integer companyId);
}

















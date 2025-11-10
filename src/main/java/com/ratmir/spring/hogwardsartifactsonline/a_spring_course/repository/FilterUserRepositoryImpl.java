package com.ratmir.spring.hogwardsartifactsonline.a_spring_course.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.dto.PersonalInfo;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.dto.UserFilter;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity.*;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity.querydsl.QPredicates;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity.QChat.chat;
import static com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity.QUser.*;
import static com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity.QUserChat.userChat;

@RequiredArgsConstructor
public class FilterUserRepositoryImpl implements FilterUserRepository {

    private static final String FIND_BY_COMPANY_AND_ROLE = """
            SELECT firstname, lastname, birth_date
            FROM spring_course.users
            WHERE company_id = ?
                AND role = ?
            """;

    private static final String UPDATE_COMPANY_AND_ROLE = """
            UPDATE spring_course.users
            SET company_id = ?, role = ?
            WHERE id = ?
            """;

    private static final String UPDATE_COMPANY_AND_ROLE_NAMED = """
            UPDATE spring_course.users
            SET company_id = :companyId, role = :role
            WHERE id = :userId
            """;

    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public List<User> findAllByFilter(UserFilter filter) {
        var cb = entityManager.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);

        List<Predicate> predicates = new ArrayList<>();
        if(filter.firstname() != null) {
            predicates.add(cb.like(user.get("firstname"), filter.firstname()));
        }
        if(filter.lastname() != null) {
            predicates.add(cb.like(user.get("lastname"), filter.lastname()));
        }
        if(filter.birthDate() != null) {
            predicates.add(cb.lessThan(user.get("birthDate"), filter.birthDate()));
        }

        criteria.select(user)
                .where(predicates.toArray(Predicate[]::new));

        return entityManager
                .createQuery(criteria)
                .getResultList();
    }

    @Override
    public List<User> findAllByFilterQueryDsl(UserFilter filter) {

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

        return new JPAQuery<User>(entityManager)
                .select(user)
                .from(user)
                .where(predicate)
                .orderBy(user.birthDate.desc())
                .distinct()
                .fetch();
    }

    @Override
    public List<PersonalInfo> findAllByCompanyIdAndRole(Integer companyId, Role role) {
        return jdbcTemplate.query(
                FIND_BY_COMPANY_AND_ROLE,
                (rs, rowNum) -> new PersonalInfo(
                    rs.getString("firstname"),
                    rs.getString("lastname"),
                    rs.getDate("birth_date").toLocalDate()
                ),
                companyId, role.name()
        );
    }

    @Override
    public void updateCompanyAndRole(List<User> users) {
        var args = users.stream()
                .map(user -> new Object[]{
                        user.getCompany().getId(),
                        user.getRole().name(),
                        user.getId()
                })
                .toList();
        jdbcTemplate.batchUpdate(UPDATE_COMPANY_AND_ROLE, args);
    }

    @Override
    public void updateCompanyAndRoleNamed(List<User> users) {
        var args = users.stream()
                .map(user -> Map.of(
                        "companyId", user.getCompany().getId(),
                        "role", user.getRole().name(),
                        "userId", user.getId()
                ))
                .map(MapSqlParameterSource::new)
                .toArray(MapSqlParameterSource[]::new);

        namedJdbcTemplate.batchUpdate(UPDATE_COMPANY_AND_ROLE_NAMED, args);
    }
}

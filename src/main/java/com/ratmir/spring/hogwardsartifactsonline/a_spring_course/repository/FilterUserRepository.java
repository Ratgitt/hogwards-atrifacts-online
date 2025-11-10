package com.ratmir.spring.hogwardsartifactsonline.a_spring_course.repository;

import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.dto.PersonalInfo;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.dto.UserFilter;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity.Role;
import com.ratmir.spring.hogwardsartifactsonline.a_spring_course.entity.User;

import java.util.List;

public interface FilterUserRepository {

    List<User> findAllByFilter(UserFilter filter);

    List<User> findAllByFilterQueryDsl(UserFilter filter);

    List<PersonalInfo> findAllByCompanyIdAndRole(Integer companyId, Role role);

    void updateCompanyAndRole(List<User> users);

    void updateCompanyAndRoleNamed(List<User> users);
}

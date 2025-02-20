package com.ratmir.spring.hogwardsartifactsonline.repository;

import com.ratmir.spring.hogwardsartifactsonline.entity.HogwardsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<HogwardsUser, Long> {
}

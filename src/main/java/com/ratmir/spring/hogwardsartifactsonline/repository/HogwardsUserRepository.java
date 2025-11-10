package com.ratmir.spring.hogwardsartifactsonline.repository;

import com.ratmir.spring.hogwardsartifactsonline.entity.HogwardsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HogwardsUserRepository extends JpaRepository<HogwardsUser, Long> {

    Optional<HogwardsUser> findByUsername(String username);
}

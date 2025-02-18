package com.ratmir.spring.hogwardsartifactsonline.repository;

import com.ratmir.spring.hogwardsartifactsonline.entity.Artifact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtifactRepository extends JpaRepository<Artifact, Long> {
}

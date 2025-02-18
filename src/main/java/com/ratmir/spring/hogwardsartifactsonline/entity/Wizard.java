package com.ratmir.spring.hogwardsartifactsonline.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Wizard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Artifact> artifacts = new ArrayList<>();

    public Wizard(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addArtifacts(Artifact... artifacts) {

        this.artifacts.addAll(List.of(artifacts));

        Arrays.stream(artifacts)
                .forEach(artifact -> artifact.setOwner(this));
    }

    public Integer getNumberOfArtifacts() {
        return this.artifacts.size();
    }
}


package com.ratmir.spring.hogwardsartifactsonline.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Wizard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(
            mappedBy = "owner",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private List<Artifact> artifacts = new ArrayList<>();

    public Wizard(Long id, String name) {
        this.id = id;
        this.name = name;
        this.artifacts = new ArrayList<>();
    }

    public static WizardBuilder builder() {
        return new WizardBuilder();
    }

    public void addArtifact(Artifact artifact) {
        this.artifacts.add(artifact);
        artifact.setOwner(this);
    }

    public Integer getNumberOfArtifacts() {
        return this.artifacts.size();
    }

    public void removeAllArtifacts() {
        Hibernate.initialize(artifacts); // Гарантированная загрузка списка
        this.artifacts.forEach(artifact -> artifact.setOwner(null));
        this.artifacts.clear();
    }

    public void removeArtifact(Artifact artifact) {
        artifact.setOwner(null);
        this.artifacts.remove(artifact);
    }

    public static class WizardBuilder {
        private Long id;
        private String name;
        private List<Artifact> artifacts;

        WizardBuilder() {
        }

        public WizardBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public WizardBuilder name(String name) {
            this.name = name;
            return this;
        }

        public WizardBuilder artifacts(List<Artifact> artifacts) {
            this.artifacts = artifacts;
            return this;
        }

        public Wizard build() {
            return new Wizard(this.id, this.name, this.artifacts);
        }

        public String toString() {
            return "Wizard.WizardBuilder(id=" + this.id + ", name=" + this.name + ", artifacts=" + this.artifacts + ")";
        }
    }
}


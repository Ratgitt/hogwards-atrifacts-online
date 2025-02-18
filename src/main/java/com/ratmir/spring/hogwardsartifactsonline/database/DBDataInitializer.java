package com.ratmir.spring.hogwardsartifactsonline.database;

import com.ratmir.spring.hogwardsartifactsonline.entity.Artifact;
import com.ratmir.spring.hogwardsartifactsonline.repository.ArtifactRepository;
import com.ratmir.spring.hogwardsartifactsonline.entity.Wizard;
import com.ratmir.spring.hogwardsartifactsonline.repository.WizardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
//@Component
public class DBDataInitializer implements CommandLineRunner {

    private final ArtifactRepository artifactRepository;
    private final WizardRepository wizardRepository;

    @Override
    public void run(String... args) throws Exception {

//        Artifact a1 = new Artifact(
//                "1250808601744904191",
//                "Deluminator",
//                "A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.",
//                "ImageUrl"
//        );
//        Artifact a2 = new Artifact(
//                "1250808601744904192",
//                "Invisibility Cloak",
//                "An invisibility cloak is used to make the wearer invisible.",
//                "ImageUrl"
//        );
//        Artifact a3 = new Artifact(
//                "1250808601744904193",
//                "Elder Wand",
//                "The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.",
//                "ImageUrl"
//        );
//        Artifact a4 = new Artifact(
//                "1250808601744904194",
//                "The Marauder's Map",
//                "A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.",
//                "ImageUrl"
//        );
//        Artifact a5 = new Artifact(
//                "1250808601744904195",
//                "The Sword Of Gryffindor",
//                "A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.",
//                "ImageUrl"
//        );
//        Artifact a6 = new Artifact(
//                "1250808601744904196",
//                "Resurrection Stone",
//                "The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.",
//                "ImageUrl"
//        );
//
//        Wizard w1 = new Wizard(1, "Albus Dumbledore");
//        Wizard w2 = new Wizard(2, "Harry Potter");
//        Wizard w3 = new Wizard(3, "Neville Longbottom");
//
//        //        artifactRepository.saveAll(List.of(a1, a2, a3, a4, a5, a6));
//
//        w1.addArtifacts(a1, a3);
//        w2.addArtifacts(a2, a4);
//        w3.addArtifacts(a5);
//
//        wizardRepository.saveAll(List.of(w1, w2, w3));
//        artifactRepository.save(a6);
    }
}
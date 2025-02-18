package com.ratmir.spring.hogwardsartifactsonline.util.converter;

import com.ratmir.spring.hogwardsartifactsonline.dto.WizardDto;
import com.ratmir.spring.hogwardsartifactsonline.entity.Wizard;
import org.springframework.stereotype.Component;

@Component
public class WizardConverter {

    public WizardDto toWizardDto(Wizard wizard) {
        return new WizardDto(
                wizard.getId(),
                wizard.getName(),
                wizard.getNumberOfArtifacts()
        );
    }

    public Wizard toWizardEntity(WizardDto wizardDto) {
        return new Wizard(
                wizardDto.id(),
                wizardDto.name()
        );
    }
}

package com.ratmir.spring.hogwardsartifactsonline.service;

import com.ratmir.spring.hogwardsartifactsonline.dto.WizardDto;
import com.ratmir.spring.hogwardsartifactsonline.entity.Wizard;
import com.ratmir.spring.hogwardsartifactsonline.repository.WizardRepository;
import com.ratmir.spring.hogwardsartifactsonline.util.converter.WizardConverter;
import com.ratmir.spring.hogwardsartifactsonline.util.exception.WizardNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.*;

@RequiredArgsConstructor
@Service
@Transactional
public class WizardService {

    private final WizardRepository wizardRepository;
    private final WizardConverter wizardConverter;

    public WizardDto findById(Long wizardId) {
        return wizardRepository.findById(wizardId)
                .map(wizardConverter::toWizardDto)
                .orElseThrow(() -> new WizardNotFoundException(wizardId));
    }

    public List<WizardDto> findAll() {
        return wizardRepository.findAll().stream()
                .map(wizardConverter::toWizardDto)
                .collect(toList());
    }

    public WizardDto save(WizardDto wizardDto) {
        Wizard wizard = wizardConverter.toWizardEntity(wizardDto);
        Wizard savedWizard = wizardRepository.save(wizard);
        return wizardConverter.toWizardDto(savedWizard);
    }

    public WizardDto update(Long wizardId, WizardDto wizardDto) {
        var wizard = wizardRepository.findById(wizardId)
                .orElseThrow(() -> new WizardNotFoundException(wizardId));

        wizard.setName(wizardDto.name());

        return wizardConverter.toWizardDto(wizard);
    }

    public void delete(Long wizardId) {
        wizardRepository.findById(wizardId)
                .orElseThrow(() -> new WizardNotFoundException(wizardId));
        wizardRepository.deleteById(wizardId);
    }
}

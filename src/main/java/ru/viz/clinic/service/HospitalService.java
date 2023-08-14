package ru.viz.clinic.service;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.viz.clinic.data.entity.Hospital;
import ru.viz.clinic.data.repository.HospitalRepository;
import ru.viz.clinic.help.Helper;
import ru.viz.clinic.help.Translator;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class HospitalService {
    private final HospitalRepository hospitalRepository;

    @Autowired
    public HospitalService(final HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    @Transactional
    public void save(@NotNull final Hospital hospital) {
        Objects.requireNonNull(hospital);
        try {
            hospitalRepository.save(hospital);
            Helper.showSuccessNotification(Translator.MSG_HOSPITAL_SUCCESS_SAVED);
        } catch (final Exception e) {
            log.error("error ", e);
            Helper.showErrorNotification("ошибка при сохранеинии госпиталя");
        }
    }

    public List<Hospital> getAll() {
        return hospitalRepository.findAll();
    }

    public Hospital getHospitalById(final Long id) {
        return hospitalRepository.findById(id).orElse(null);
    }

    public void deleteHospital(final Long id) {
        hospitalRepository.deleteById(id);
    }
}
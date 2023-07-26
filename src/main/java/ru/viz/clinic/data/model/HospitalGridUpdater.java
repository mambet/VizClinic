package ru.viz.clinic.data.model;

import ru.viz.clinic.data.entity.Department;
import ru.viz.clinic.data.entity.Hospital;

import java.util.Optional;

public interface HospitalGridUpdater {
    void updateHospital(Optional<Hospital> optionalDepartment);
}

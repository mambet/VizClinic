package ru.viz.clinic.data.model;

import ru.viz.clinic.data.entity.Department;

import java.util.Optional;

public interface DepartmentGridUpdater extends HospitalGridUpdater {
    void updateDepartment(Optional<Department> optionalDepartment);
}

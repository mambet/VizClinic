package ru.viz.clinic.data.model;

import ru.viz.clinic.data.entity.Department;

public interface DepartmentGridFilterUpdater extends HospitalGridFilterUpdater {
    void setDepartmentFilterParameter(Department department);
}

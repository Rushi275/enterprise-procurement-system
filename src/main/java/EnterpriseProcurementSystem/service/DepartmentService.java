package EnterpriseProcurementSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import EnterpriseProcurementSystem.entity.Department;
import EnterpriseProcurementSystem.repository.DepartmentRepository;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department updateDepartment(Long id, Department department) {
        Department existingDepartment = departmentRepository.findById(id).orElse(null);

        if (existingDepartment != null) {
            existingDepartment.setDepartmentName(department.getDepartmentName());
            existingDepartment.setManager(department.getManager());
            return departmentRepository.save(existingDepartment);
        }

        return null;
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}
package com.accuresoftech.abc.servicesimpl;

import com.accuresoftech.abc.entity.auth.Department;
import com.accuresoftech.abc.exception.ResourceNotFoundException;
import com.accuresoftech.abc.repository.DepartmentRepository;
import com.accuresoftech.abc.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public Department create(Department d) {
        return departmentRepository.save(d);
    }

    @Override
    public Department update(Long id, Department d) {
        Department ex = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        ex.setName(d.getName());
        ex.setManager(d.getManager());
        return departmentRepository.save(ex);
    }

    @Override
    public Department getById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
    }

    @Override
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        departmentRepository.deleteById(id);
    }
}

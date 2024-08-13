package com.bside.students.services;

import com.bside.Errors.ResourceNotFoundException;
import com.bside.students.dtos.StudentDto;
import com.bside.students.models.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student create(StudentDto studentDto) throws Exception;
    List<Student> getAll();
    Optional<Student> update(Integer id, StudentDto studentDto) throws ResourceNotFoundException, Exception;
    void delete(Integer id) throws ResourceNotFoundException, Exception;;
}


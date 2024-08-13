package com.bside.students.services.impl;

import com.bside.students.models.Student;
import com.bside.students.services.StudentService;

import java.util.List;

public class StudentServiceImpl implements StudentService {
    @Override
    public Student create(Student student) {
        return null;
    }

    @Override
    public List<Student> getAllStudent() {
        return List.of();
    }

    @Override
    public Student update(Integer id) {
        return null;
    }

    @Override
    public Student updateWithJavaFilter(Integer id) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void deleteWithJavaFilter(Integer id) {

    }
}

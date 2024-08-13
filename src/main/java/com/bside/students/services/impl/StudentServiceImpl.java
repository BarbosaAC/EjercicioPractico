package com.bside.students.services.impl;

import com.bside.Errors.ResourceNotFoundException;
import com.bside.students.dtos.StudentDto;
import com.bside.students.models.Student;
import com.bside.students.repositories.StudentRepository;
import com.bside.students.services.StudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger log = LogManager.getLogger(StudentServiceImpl.class);
    @Autowired
    private StudentRepository studentRepository;


    @Override
    public Student create(StudentDto studentDto) throws Exception{
        log.info("[START] Init service to create student");
        try {
            log.info("Convert entity student");
            Student student = this.dtoToModel(studentDto);
            log.info("Try save entity student");
            Student newStudent = studentRepository.save(student);
            log.info("Success save entity student");
            return newStudent;
        } catch (Exception e) {
            log.error("Error saving student");
            log.error(e);
            throw e;
        }

    }

    @Override
    public List<Student> getAll() {
        log.info("[START] Init service to get all students");
        try {
            log.info("Try find all students");
            List<Student> students = studentRepository.findAll();
            List<StudentDto> studentsDto = students.stream()
                    .map((this::modelToDto))
                    .toList();
            log.info("Success get all students");
            return students;
        } catch (Exception e) {
            log.error("Error obtaining students");
            log.error(e);
            throw e;
        }
    }

    @Override
    public Optional<Student> update(Integer id, StudentDto studentDto) throws ResourceNotFoundException, Exception{
        log.info("[START] Init service to update student");
        try {
            log.info("Try update entity student");
            Optional<Student> studentUpdate = Optional.ofNullable(studentRepository.findById(id)
                    .map(student -> {
                        student = this.dtoToModel(studentDto);
                        return studentRepository.save(student);
                    }).orElseThrow(() -> new ResourceNotFoundException("The student with id: " + id + " not found")));
            log.info("Success update entity student with id {}" , id);
            return studentUpdate;
        } catch (Exception e) {
            log.error("Error saving student with id: {}", id);
            log.error(e);
            throw e;
        }
    }

    @Override
    public void delete(Integer id) throws ResourceNotFoundException, Exception{
        log.info("[START] Init service to delete student");

        try {
            log.info("Try deleted entity student");
            if(studentRepository.existsById(id)){
                studentRepository.deleteById(id);
            } else {
                throw new ResourceNotFoundException("The student with id: " + id + " not found");
            }

            log.info("Success delete entity student with id: {}", id);

        } catch (Exception e) {
            log.error("Error saving student with id: {}", id);
            log.error(e);
            throw e;
        }
    }

    private StudentDto modelToDto(Student student){
        return new StudentDto(student.getId(), student.getFullName(),student.getAge(),
                student.getEmail(), student.getAddress(), student.getGrade());
    }

    private Student dtoToModel(StudentDto studentDto){
        return new Student(studentDto.getId(), studentDto.getFullName(), studentDto.getAge(),
                studentDto.getEmail(), studentDto.getAddress(), studentDto.getGrade());
    }
}

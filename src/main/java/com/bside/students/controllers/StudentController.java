package com.bside.students.controllers;

import com.bside.errors.GenericException;
import com.bside.errors.ResourceNotFoundException;
import com.bside.students.dtos.StudentDto;
import com.bside.students.models.Student;
import com.bside.students.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("v1/students")
public class StudentController {
    private static final Logger log = LogManager.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    @PostMapping("")
    public ResponseEntity<Student> create(@Valid @RequestBody StudentDto studentDto){
        log.info("Receive request create in controller");

        try {
            Student newStudent = studentService.create(studentDto);
            log.info("[END] create student");
            return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new GenericException("There was an error while trying to create the student. Please check the logs.");
        }
    }

    @GetMapping("")
    public ResponseEntity<List<StudentDto>> getAll(){
        log.info("Receive request get all students in controller");

        try {
            List<StudentDto> students = studentService.getAll();
            log.info("[END] get all students");
            return new ResponseEntity<>(students, HttpStatus.OK);
        } catch (Exception e) {
            throw new GenericException("There was an error while trying get all students. Please check the logs.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Optional<Student>> update(@PathVariable Integer id,
                                                           @Valid @RequestBody StudentDto studentDto) {
        log.info("Receive request update in controller");

        try {
            Optional<Student> student = studentService.update(id, studentDto);
            log.info("[END] update student");
            return new ResponseEntity<>(student, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new GenericException("There was an error while trying to update the student. Please check the logs.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        log.info("Receive request delete in controller");

        try {
            studentService.delete(id);
            log.info("[END] delete student");
            return new ResponseEntity<>("The student was deleted successfully", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new GenericException("There was an error while trying to delete the student. Please check the logs.");
        }
    }
}

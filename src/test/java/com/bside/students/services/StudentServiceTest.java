package com.bside.students.services;

import com.bside.errors.ResourceNotFoundException;
import com.bside.students.dtos.StudentDto;
import com.bside.students.models.Student;
import com.bside.students.repositories.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @MockBean
    private StudentRepository studentRepository;

    @Test
    void testCreate() throws Exception {
        StudentDto studentDto = new StudentDto("David Martínez", 30,
                "david.martinez@test.com", "Av. siempre viva #124", 6.7);
        Student student = new Student(1, "David Martínez", 30,
                "david.martinez@test.com", "Av. siempre viva #124", 6.7);

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student studentSaved = studentService.create(studentDto);

        assertNotNull(studentSaved);
        assertEquals(studentSaved.getFullName(), studentDto.getFullName());
        assertEquals(studentSaved.getId(), student.getId());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testCreateInternalErrorException() throws Exception {
        StudentDto studentDto = new StudentDto("David Martínez", 30,
                "david.martinez@test.com", "Av. siempre viva #124", 6.7);

        when(studentRepository.save(any(Student.class)))
                .thenThrow(new RuntimeException("Authenticate DB failed"));

        assertThrows(Exception.class, () -> studentService.create(studentDto));

    }

    @Test
    void testGetAll() throws Exception {
        Student student = new Student(1, "David Martínez", 30,
                "david.martinez@test.com", "Av. siempre viva #124", 6.7);

        Student studentTwo = new Student(2, "Isabella Díaz", 25,
                "isabella.díaz@test.com", "Av. siempre viva #124", 9.4);

        List<Student> students = new ArrayList<Student>();
        students.add(student);
        students.add(studentTwo);

        when(studentRepository.findAll()).thenReturn(students);

        List<StudentDto> studentsDto = studentService.getAll();

        assertNotNull(studentsDto);
        assertEquals(studentsDto.size(), students.size());
        assertEquals(studentsDto.getFirst().getId(), students.getFirst().getId());
        assertEquals(studentsDto.get(1).getFullName(), students.get(1).getFullName());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetAllInternalErrorException() throws Exception {

        when(studentRepository.findAll())
                .thenThrow(new RuntimeException("Authenticate DB failed"));

        assertThrows(Exception.class, () -> studentService.getAll());
    }

    @Test
    void testUpdate() throws Exception {

        Integer id = 1;
        StudentDto studentDto = new StudentDto("David Martínez", 30,
                "david.martinez@test.com", "Av. siempre viva #124", 6.7);
        Student existingStudent = new Student(1, ",Miriam Arredondo", 30,
                "david.martinez@test.com", "Av. siempre viva #124", 6.7);
        Student updatedStudent = new Student(1, "David Martínez", 30,
                "david.martinez@test.com", "Av. siempre viva #124", 6.7);


        when(studentRepository.findById(any(Integer.class))).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        Optional<Student> studentOptionalSaved = studentService.update(id, studentDto);

        assertNotNull(studentOptionalSaved);
        studentOptionalSaved.ifPresent(studentSaved -> {
            assertEquals(studentSaved.getFullName(), studentDto.getFullName());
            assertEquals(studentSaved.getId(), id);
        });

        verify(studentRepository, times(1)).findById(any(Integer.class));
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testUpdateNotFoundException() throws Exception {

        Integer id = 1;
        StudentDto studentDto = new StudentDto("David Martínez", 30,
                "david.martinez@test.com", "Av. siempre viva #124", 6.7);

        when(studentRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> studentService.update(id, studentDto));

    }

    @Test
    void testUpdateInternalErrorException() throws Exception {

        Integer id = 1;
        StudentDto studentDto = new StudentDto("David Martínez", 30,
                "david.martinez@test.com", "Av. siempre viva #124", 6.7);

        when(studentRepository.findById(any(Integer.class)))
                .thenThrow(new RuntimeException("Authenticate DB failed"));

        assertThrows(Exception.class, () -> studentService.update(id, studentDto));

    }

    @Test
    void testDelete() throws Exception {

        Integer id = 1;

        when(studentRepository.existsById(any(Integer.class))).thenReturn(true);
        doNothing().when(studentRepository).deleteById(any(Integer.class));

        studentService.delete(id);

        verify(studentRepository, times(1)).existsById(any(Integer.class));
        verify(studentRepository, times(1)).deleteById(any(Integer.class));
    }

    @Test
    void testDeleteNotFoundException() throws Exception {

        Integer id = 1;

        when(studentRepository.existsById(any(Integer.class))).thenReturn(false);
        doNothing().when(studentRepository).deleteById(any(Integer.class));

        assertThrows(ResourceNotFoundException.class, () -> studentService.delete(id));

    }

    @Test
    void testDeleteInternalErrorException() throws Exception {

        Integer id = 1;

        when(studentRepository.existsById(any(Integer.class)))
                .thenThrow(new RuntimeException("Authenticate DB failed"));

        assertThrows(Exception.class, () -> studentService.delete(id));
    }
}

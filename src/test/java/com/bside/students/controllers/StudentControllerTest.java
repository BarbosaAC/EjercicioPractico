package com.bside.students.controllers;

import com.bside.errors.ResourceNotFoundException;
import com.bside.students.dtos.StudentDto;
import com.bside.students.models.Student;
import com.bside.students.services.StudentService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @BeforeEach
    void setUp(){

    }

    @Test
    void testCreate() throws Exception {
        StudentDto studentDto = new StudentDto("David Martínez", 30,
                "david.martinez@test.com", "Av. siempre viva #124", 6.7);
        Student student = new Student(1, "David Martínez", 30,
                "david.martinez@test.com", "Av. siempre viva #124", 6.7);

        when(studentService.create(any(StudentDto.class))).thenReturn(student);

        Gson gson = new GsonBuilder().create();
        var studentJson = gson.toJson(studentDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/students")
                        .content(studentJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id").exists())
                        .andReturn();

        assertNotNull(result);
        assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());
        verify(studentService, times(1)).create(any(StudentDto.class));
    }

    @Test
    void testCreateInternalErrorException() throws Exception {
        StudentDto studentDto = new StudentDto("David Martínez", 30,
                "david.martinez@test.com", "Av. siempre viva #124", 6.7);

        when(studentService.create(any(StudentDto.class)))
                .thenThrow(new Exception("Authenticate DB failed"));

        Gson gson = new GsonBuilder().create();
        var studentJson = gson.toJson(studentDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/students")
                        .content(studentJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().is5xxServerError())
                        .andReturn();

        assertNotNull(result);
        assertEquals(result.getResponse().getStatus(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        verify(studentService, times(1)).create(any(StudentDto.class));
    }

    @Test
    void testCreateValidateException() throws Exception {
        StudentDto studentDto = new StudentDto("", -2,
                "david.martinez", "Av. siempre viva #124", 6.7);

        Gson gson = new GsonBuilder().create();
        var studentJson = gson.toJson(studentDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/students")
                        .content(studentJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().is4xxClientError())
                        .andReturn();

        assertNotNull(result);
        assertEquals(result.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void testGetAll() throws Exception {

        StudentDto student = new StudentDto(1, "David Martínez", 30,
                "david.martinez@test.com", "Av. siempre viva #124", 6.7);

        List<StudentDto> students = new ArrayList<StudentDto>();
        students.add(student);

        when(studentService.getAll()).thenReturn(students);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/students")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        assertNotNull(result);
        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        verify(studentService, times(1)).getAll();

    }

    @Test
    void testGetAllInternalErrorException() throws Exception {

        when(studentService.getAll())
                .thenThrow(new RuntimeException("Authenticate DB failed"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andReturn();

        assertNotNull(result);
        assertEquals(result.getResponse().getStatus(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        verify(studentService, times(1)).getAll();
    }

    @Test
    void testUpdate() throws Exception {
        StudentDto studentDto = new StudentDto(1,"David Martínez", 30,
                "david.martinez@test.com", "Av. siempre viva #124", 6.7);
        Optional<Student> student = Optional.of(new Student(1, "David Martínez", 30,
                "david.martinez@test.com", "Av. siempre viva #124", 6.7));

        when(studentService.update(any(Integer.class), any(StudentDto.class)))
                .thenReturn(student);

        Gson gson = new GsonBuilder().create();
        var studentJson = gson.toJson(studentDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/v1/students/" + studentDto.getId())
                        .content(studentJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        assertNotNull(result);
        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        verify(studentService, times(1)).update(any(Integer.class), any(StudentDto.class));
    }

    @Test
    void testUpdateInternalErrorException() throws Exception {
        StudentDto studentDto = new StudentDto(1,"David Martínez", 30,
                "david.martinez@test.com", "Av. siempre viva #124", 6.7);

        when(studentService.update(any(Integer.class), any(StudentDto.class)))
                .thenThrow(new Exception("Authenticate DB failed"));

        Gson gson = new GsonBuilder().create();
        var studentJson = gson.toJson(studentDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/v1/students/" + studentDto.getId())
                        .content(studentJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andReturn();

        assertNotNull(result);
        assertEquals(result.getResponse().getStatus(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        verify(studentService, times(1)).update(any(Integer.class), any(StudentDto.class));
    }

    @Test
    void testUpdateValidateException() throws Exception {
        StudentDto studentDto = new StudentDto(1,"", -2,
                "david.martinez", "Av. siempre viva #124", 6.7);

        Gson gson = new GsonBuilder().create();
        var studentJson = gson.toJson(studentDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/v1/students/" + studentDto.getId())
                        .content(studentJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        assertNotNull(result);
        assertEquals(result.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void testUpdateNotFoundException() throws Exception {
        StudentDto studentDto = new StudentDto(1,"David Martínez", 30,
                "david.martinez@test.com", "Av. siempre viva #124", 6.7);

        when(studentService.update(any(Integer.class), any(StudentDto.class)))
                .thenThrow(new ResourceNotFoundException("The student not found"));

        Gson gson = new GsonBuilder().create();
        var studentJson = gson.toJson(studentDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/v1/students/" + studentDto.getId())
                        .content(studentJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        assertNotNull(result);
        assertEquals(result.getResponse().getStatus(), HttpStatus.NOT_FOUND.value());
        verify(studentService, times(1)).update(any(Integer.class), any(StudentDto.class));
    }

    @Test
    void testDelete() throws Exception {
        Integer id = 1;

        doNothing().when(studentService).delete(any(Integer.class));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/v1/students/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertNotNull(result);
        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        verify(studentService, times(1)).delete(any(Integer.class));
    }

    @Test
    void testDeleteInternalErrorException() throws Exception {
        Integer id = 1;

        doThrow(new Exception("Database error")).when(studentService).delete(any(Integer.class));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/v1/students/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andReturn();

        assertNotNull(result);
        assertEquals(result.getResponse().getStatus(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        verify(studentService, times(1)).delete(any(Integer.class));
    }

    @Test
    void testDeleteNotFoundException() throws Exception {
        Integer id = 1;

        doThrow(new ResourceNotFoundException("The student not found")).when(studentService).delete(any(Integer.class));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/v1/students/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        assertNotNull(result);
        assertEquals(result.getResponse().getStatus(), HttpStatus.NOT_FOUND.value());
        verify(studentService, times(1)).delete(any(Integer.class));
    }

}

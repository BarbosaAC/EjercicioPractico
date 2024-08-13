package com.bside.students.dtos;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class StudentDto {
    private Integer id;

    @NotNull(message = "The name is mandatory")
    @NotBlank(message = "The name is mandatory")
    private String fullName;

    @Positive(message = "The age cannot be zero o negative")
    private int age;

    @NotNull(message = "The email cannot be null.")
    @NotBlank(message = "The email cannot be null.")
    @Email(message = "The email will be valid")
    private String email;

    private String address;

    private double grade;

    public StudentDto(Integer id, String fullName, int age, String email, String address, double grade) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.address = address;
        this.grade = grade;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

}

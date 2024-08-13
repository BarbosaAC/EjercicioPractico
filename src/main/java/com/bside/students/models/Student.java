package com.bside.students.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "invoices")
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String fullName;

    @Column
    private int age;

    @Column
    private String email;

    @Column
    private String address;

    @Column
    private double grade;
}

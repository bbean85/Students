package com.example.students.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDto {
    private Long id;
    private String name;
    private String grade;
    private String email;
    private LocalDate dateOfBirth;
    private Set<CourseDto> courses;
}


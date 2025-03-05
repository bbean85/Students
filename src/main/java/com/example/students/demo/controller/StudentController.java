package com.example.students.demo.controller;

import com.example.students.demo.dto.CourseDto;
import com.example.students.demo.dto.StudentResponseDto;
import com.example.students.demo.model.Student;
import com.example.students.demo.repository.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @GetMapping
    public List<StudentResponseDto> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    @PostMapping
    public StudentResponseDto createStudent(@RequestBody Student student) {
        Student savedStudent = studentRepository.save(student);
        return mapToDto(savedStudent);
    }


    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDto> getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(student -> ResponseEntity.ok(mapToDto(student)))
                .orElse(ResponseEntity.notFound().build());
    }



    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDto> updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setName(updatedStudent.getName());
                    student.setEmail(updatedStudent.getEmail());
                    student.setGrade(updatedStudent.getGrade());
                    student.setDateOfBirth(updatedStudent.getDateOfBirth());
                    Student savedStudent = studentRepository.save(student);
                    return ResponseEntity.ok(mapToDto(savedStudent));
                })
                .orElse(ResponseEntity.notFound().build());
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }



    private StudentResponseDto mapToDto(Student student) {
        StudentResponseDto dto = new StudentResponseDto();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setGrade(student.getGrade());
        dto.setEmail(student.getEmail());
        dto.setDateOfBirth(student.getDateOfBirth());


        Set<CourseDto> courseDtos = student.getCoursesForJson().stream()
                .map(course -> new CourseDto(
                        course.getId(),
                        course.getName(),
                        course.getDescription(),
                        course.getCreditHours()
                ))
                .collect(Collectors.toSet());
        dto.setCourses((Set<CourseDto>) courseDtos);

        return dto;
    }
}

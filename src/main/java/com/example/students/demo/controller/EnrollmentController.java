package com.example.students.demo.controller;

import com.example.students.demo.dto.EnrollmentDto;
import com.example.students.demo.model.Enrollment;
import com.example.students.demo.model.Student;
import com.example.students.demo.model.Course;
import com.example.students.demo.repository.EnrollmentRepository;
import com.example.students.demo.repository.StudentRepository;
import com.example.students.demo.repository.CourseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;  // <-- Add this import

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentController(EnrollmentRepository enrollmentRepository,
                                StudentRepository studentRepository,
                                CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable Long id) {
        return enrollmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Replace your existing enrollStudent method with the following:
    @PostMapping
    @Transactional
    public ResponseEntity<Enrollment> enrollStudent(@RequestBody EnrollmentDto dto) {
        Optional<Student> studentOpt = studentRepository.findById(dto.getStudent_id());
        Optional<Course> courseOpt = courseRepository.findById(dto.getCourse_id());

        if (studentOpt.isEmpty() || courseOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Student student = studentOpt.get();
        Course course = courseOpt.get();

        // Update the ManyToMany relationship before creating the enrollment
        student.getCourses().add(course);
        course.getStudents().add(student);
        // Save student to persist join table changes
        studentRepository.save(student);

        // Now create and save the enrollment record
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setGrade(dto.getGrade() != null ? dto.getGrade() : "Not Assigned");

        if (dto.getEnrollment_date() != null && !dto.getEnrollment_date().isEmpty()) {
            enrollment.setEnrollmentDate(LocalDate.parse(dto.getEnrollment_date()));
        }

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return ResponseEntity.ok(savedEnrollment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Enrollment> updateEnrollment(@PathVariable Long id,
                                                       @RequestParam String grade) {
        return enrollmentRepository.findById(id)
                .map(enrollment -> {
                    enrollment.setGrade(grade);
                    Enrollment updated = enrollmentRepository.save(enrollment);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        if (!enrollmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        enrollmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

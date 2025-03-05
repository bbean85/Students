package com.example.students.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"enrollments", "courses"})
@EqualsAndHashCode
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String grade;

    @Column(unique = true)
    private String email;

    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Enrollment> enrollments = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @JsonIgnore
    private Set<Course> courses = new HashSet<>();

    @JsonProperty("courses")
    public synchronized Set<Course> getCoursesForJson() {
        return new HashSet<>(this.courses);
    }

    @JsonProperty("enrollments")
    public synchronized Set<Enrollment> getEnrollmentsForJson() {
        return new HashSet<>(this.enrollments);
    }
}

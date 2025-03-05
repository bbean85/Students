package com.example.students.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 255)
    private String description;

    @Column
    private int creditHours;

    @ManyToMany(mappedBy = "courses")
    @JsonIgnore
    @ToString.Exclude
    private Set<Student> students = new HashSet<>();

    public Course(String name) {
        this.name = name;
    }

    @JsonProperty("students")
    public Set<Student> getStudentsForJson() {
        return new HashSet<>(students);
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

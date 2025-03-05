package com.example.students.demo.dto;

public class EnrollmentDto {

    private String enrollment_date;
    private String grade;
    private Long course_id;
    private Long student_id;

    public EnrollmentDto() {
    }

    public String getEnrollment_date() {
        return enrollment_date;
    }

    public void setEnrollment_date(String enrollment_date) {
        this.enrollment_date = enrollment_date;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Long getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Long course_id) {
        this.course_id = course_id;
    }

    public Long getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Long student_id) {
        this.student_id = student_id;
    }
}

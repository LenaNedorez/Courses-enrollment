package ru.nedorezova.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentDto {

    private Long studentId;
    private Long courseId;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
package ru.nedorezova.service;


@Component
public class EnrollmentDtoMapper {

    public EnrollmentDto toDto(Enrollment enrollment) {
        EnrollmentDto enrollmentDto = new EnrollmentDto();
        enrollmentDto.setStudentId(enrollment.getStudentId());
        enrollmentDto.setCourseId(enrollment.getCourseId());
        return enrollmentDto;
    }

    public Enrollment toEntity(EnrollmentDto enrollmentDto) {
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(enrollmentDto.getStudentId());
        enrollment.setCourseId(enrollmentDto.getCourseId());
        return enrollment;
    }
}

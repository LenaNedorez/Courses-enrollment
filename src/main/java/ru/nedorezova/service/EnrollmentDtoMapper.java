package ru.nedorezova.service;

import org.springframework.stereotype.Service;
import ru.nedorezova.dto.EnrollmentDto;
import ru.nedorezova.entity.Enrollment;

@Service
public class EnrollmentDtoMapper {

    public EnrollmentDto converttoDto(Enrollment enrollment) {
        EnrollmentDto enrollmentDto = new EnrollmentDto();
        enrollmentDto.setStudentId(enrollment.getStudentId());
        enrollmentDto.setCourseId(enrollment.getCourseId());
        return enrollmentDto;
    }

}

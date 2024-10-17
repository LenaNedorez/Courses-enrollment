package ru.nedorezova.mappers;

import javax.annotation.processing.Generated;
import ru.nedorezova.dto.EnrollmentDto;
import ru.nedorezova.entity.Enrollment;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-17T17:54:30+0300",
    comments = "version: 1.6.2, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
public class EnrollmentMapperImpl implements EnrollmentMapper {

    @Override
    public EnrollmentDto toDto(Enrollment enrollment) {
        if ( enrollment == null ) {
            return null;
        }

        EnrollmentDto enrollmentDto = new EnrollmentDto();

        enrollmentDto.setStudentId( enrollment.getStudentId() );
        enrollmentDto.setCourseId( enrollment.getCourseId() );

        return enrollmentDto;
    }
}

package ru.nedorezova.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.nedorezova.dto.EnrollmentDto;
import ru.nedorezova.entity.Enrollment;

@Mapper
public interface EnrollmentMapper {

    EnrollmentMapper INSTANCE = Mappers.getMapper( EnrollmentMapper.class );
    EnrollmentDto toDto(Enrollment enrollment);

}

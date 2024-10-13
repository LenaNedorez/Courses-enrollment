package ru.nedorezova.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.nedorezova.dto.StudentDto;
import ru.nedorezova.entity.Student;

@Mapper
public interface StudentMapper {

    StudentMapper INSTANCE = Mappers.getMapper( StudentMapper.class );
    StudentDto toDto(Student student);

}

package ru.nedorezova.mappers;

import javax.annotation.processing.Generated;
import ru.nedorezova.dto.StudentDto;
import ru.nedorezova.entity.Student;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-15T16:53:01+0300",
    comments = "version: 1.6.2, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
public class StudentMapperImpl implements StudentMapper {

    @Override
    public StudentDto toDto(Student student) {
        if ( student == null ) {
            return null;
        }

        StudentDto studentDto = new StudentDto();

        studentDto.setId( student.getId() );
        studentDto.setName( student.getName() );
        studentDto.setSurname( student.getSurname() );

        return studentDto;
    }
}

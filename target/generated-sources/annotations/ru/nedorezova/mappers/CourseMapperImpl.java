package ru.nedorezova.mappers;

import javax.annotation.processing.Generated;
import ru.nedorezova.dto.CourseDto;
import ru.nedorezova.entity.Course;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-17T17:54:30+0300",
    comments = "version: 1.6.2, compiler: javac, environment: Java 17.0.8 (Oracle Corporation)"
)
public class CourseMapperImpl implements CourseMapper {

    @Override
    public CourseDto toDto(Course course) {
        if ( course == null ) {
            return null;
        }

        CourseDto courseDto = new CourseDto();

        courseDto.setId( course.getId() );
        courseDto.setName( course.getName() );
        courseDto.setCapacity( course.getCapacity() );
        courseDto.setCurrentEnrollment( course.getCurrentEnrollment() );
        courseDto.setStartDate( course.getStartDate() );
        courseDto.setEndDate( course.getEndDate() );

        return courseDto;
    }
}

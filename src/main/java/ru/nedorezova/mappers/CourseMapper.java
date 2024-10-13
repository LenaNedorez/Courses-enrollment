package ru.nedorezova.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.nedorezova.dto.CourseDto;
import ru.nedorezova.entity.Course;

@Mapper
public interface CourseMapper {

    CourseMapper  INSTANCE = Mappers.getMapper( CourseMapper .class );

    CourseDto toDto(Course course);

}

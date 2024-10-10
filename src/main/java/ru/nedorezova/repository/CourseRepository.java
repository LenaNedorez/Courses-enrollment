package ru.nedorezova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nedorezova.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

}

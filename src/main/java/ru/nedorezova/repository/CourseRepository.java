package ru.nedorezova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nedorezova.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

}

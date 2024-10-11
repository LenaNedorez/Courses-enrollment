package ru.nedorezova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nedorezova.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}

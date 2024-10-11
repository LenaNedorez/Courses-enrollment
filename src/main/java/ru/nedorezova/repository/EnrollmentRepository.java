package ru.nedorezova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nedorezova.entity.Enrollment;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long id);
}

package ru.nedorezova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nedorezova.entity.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
}

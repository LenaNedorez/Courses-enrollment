package ru.nedorezova.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int capacity;
    private int currentEnrollment;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Course(Long id, String name, int capacity, int currentEnrollment, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.currentEnrollment = currentEnrollment;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @ManyToMany(mappedBy = "courses")
    private List<Student> students = new ArrayList<>();
}

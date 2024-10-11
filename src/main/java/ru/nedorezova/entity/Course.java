package ru.nedorezova.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

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
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int capacity;
    private int currentEnrollment;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToMany(mappedBy = "courses")
    private List<Student> students = new ArrayList<>();

}

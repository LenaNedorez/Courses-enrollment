package ru.nedorezova.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {

    private Long id;
    private String name;
    private int capacity;
    private int currentEnrollment;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}

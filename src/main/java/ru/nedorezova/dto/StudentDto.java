package ru.nedorezova.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudentDto {

    private Long id;
    private String name;
    private String surname;
    private List<Long> courseIds;

}

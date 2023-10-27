package ru.job4j.auth.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonDto {

    @Positive(message = "Id must be more than 0")
    private int id;

    @NotBlank(message = "Login must not be empty")
    private String login;

}
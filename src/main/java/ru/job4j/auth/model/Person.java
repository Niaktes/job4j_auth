package ru.job4j.auth.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.Data;
import ru.job4j.auth.validation.Operation;

@Entity
@Data
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Positive(message = "Id must be more than 0", groups = Operation.OnUpdate.class)
    private int id;

    @NotBlank(message = "Login must not be empty",
            groups = {Operation.OnCreate.class, Operation.OnUpdate.class})
    private String login;

    @Size(min = 6, message = "Password must be more than 6 characters",
            groups = {Operation.OnCreate.class, Operation.OnUpdate.class})
    @NotBlank(message = "Password must not be empty",
            groups = {Operation.OnCreate.class, Operation.OnUpdate.class})
    private String password;

}
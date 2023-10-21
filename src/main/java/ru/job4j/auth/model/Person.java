package ru.job4j.auth.model;

import javax.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String login;

    private String password;

}
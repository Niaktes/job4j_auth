package ru.job4j.auth.service;

import java.util.List;
import java.util.Optional;
import ru.job4j.auth.model.Person;

public interface PersonService {

    Optional<Person> findById(int id);

    List<Person> findAll();

    Person save(Person person);

    void deletePerson(int id);

}
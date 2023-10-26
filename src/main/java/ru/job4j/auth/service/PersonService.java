package ru.job4j.auth.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import ru.job4j.auth.model.Person;

public interface PersonService {

    Optional<Person> findById(int id);

    List<Person> findAll();

    boolean save(Person person);

    boolean deletePerson(int id);

    Optional<Person> patch(Person person);

}
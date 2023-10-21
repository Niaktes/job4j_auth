package ru.job4j.auth.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.auth.model.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {

    @Override
    List<Person> findAll();

}
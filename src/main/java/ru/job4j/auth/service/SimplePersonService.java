package ru.job4j.auth.service;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.repository.PersonRepository;

@Service
@AllArgsConstructor
@Slf4j
public class SimplePersonService implements PersonService {

    private final PersonRepository personRepository;

    @Override
    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public boolean save(Person person) {
        boolean result = false;
        try {
            personRepository.save(person);
            result = true;
        } catch (DataAccessException | IllegalArgumentException e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    @Override
    public boolean deletePerson(int id) {
        boolean isExists = personRepository.existsById(id);
        if (isExists) {
            Person person = new Person();
            person.setId(id);
            personRepository.delete(person);
        }
        return isExists;
    }

}
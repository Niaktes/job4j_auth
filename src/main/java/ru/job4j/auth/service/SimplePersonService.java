package ru.job4j.auth.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
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

    @Override
    public Optional<Person> patch(Person person) {
        Optional<Person> existedOptional = findById(person.getId());
        if (existedOptional.isEmpty()) {
            return Optional.empty();
        }
        Person existed = existedOptional.get();
        var methods = existed.getClass().getDeclaredMethods();
        var namePerMethod = new HashMap<String, Method>();
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                namePerMethod.put(name, method);
            }
        }
        for (String name : namePerMethod.keySet()) {
            if (name.startsWith("get")) {
                Method getMethod = namePerMethod.get(name);
                Method setMethod = namePerMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    return Optional.empty();
                }
                try {
                    var newValue = getMethod.invoke(person);
                    if (newValue != null) {
                        setMethod.invoke(existed, newValue);
                    }
                } catch (InvocationTargetException | IllegalAccessException e) {
                    log.error(e.getMessage(), e);
                    return Optional.empty();
                }
            }
        }
        personRepository.save(existed);
        return Optional.of(existed);
    }

}
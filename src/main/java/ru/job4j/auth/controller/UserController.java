package ru.job4j.auth.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.service.PersonService;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final PersonService personService;
    private final PasswordEncoder encoder;

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        personService.save(person);
    }

    @GetMapping("/all")
    public List<Person> findAll() {
        return personService.findAll();
    }

}
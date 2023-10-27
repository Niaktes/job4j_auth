package ru.job4j.auth.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.auth.dto.PersonDto;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.service.PersonService;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;
    private final PasswordEncoder encoder;
    private final ObjectMapper objectMapper;

    @GetMapping("/")
    public ResponseEntity<List<Person>> findAll() {
        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = personService.findById(id);
        return new ResponseEntity<>(
                person.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Nothing found for this ID.")),
                HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        if (person.getLogin() == null || person.getPassword() == null) {
            throw new NullPointerException("Login and password mustn't be empty");
        }
        if (person.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be 6 symbols length at least.");
        }
        person.setPassword(encoder.encode(person.getPassword()));
        boolean result = personService.save(person);
        if (!result) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Person with this login already exists.");
        }
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        if (person.getLogin() == null || person.getPassword() == null) {
            throw new NullPointerException("Login or password fields are empty");
        }
        if (person.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be 6 symbols length at least.");
        }
        if (personService.findById(person.getId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Person for update not found.");
        }
        personService.save(person);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (!personService.deletePerson(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Nothing was deleted using the specified ID.");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/")
    public ResponseEntity<Person> patch(@RequestBody PersonDto personDto) {
        Person patchablePerson = personService.findById(personDto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong person id."));
        if (personDto.getLogin() == null || personDto.getLogin().isEmpty()) {
            throw new NullPointerException("Login mustn't be empty.");
        }
        patchablePerson.setLogin(personDto.getLogin());
        personService.save(patchablePerson);
        return new ResponseEntity<>(patchablePerson, HttpStatus.OK);
    }

    @ExceptionHandler (value = {IllegalArgumentException.class})
    private void handleException(Exception e, HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        Map<String, String> errorDetails = Map.of(
                "message", "Mistake in parameters.",
                "details", e.getMessage()
        );
        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
        log.error(e.getMessage());
    }

}
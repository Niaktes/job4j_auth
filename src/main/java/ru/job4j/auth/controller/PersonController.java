package ru.job4j.auth.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.auth.dto.PersonDto;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.service.PersonService;
import ru.job4j.auth.validation.Operation;

@RestController
@AllArgsConstructor
@Slf4j
@Validated
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
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Person> create(@Valid @RequestBody Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        boolean result = personService.save(person);
        if (!result) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Person with this login already exists.");
        }
        return new ResponseEntity<>(person, HttpStatus.CREATED);
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Person person) {
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
    public ResponseEntity<Person> patch(@Valid @RequestBody PersonDto personDto) {
        Person patchablePerson = personService.findById(personDto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong person id."));
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
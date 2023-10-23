package ru.job4j.auth.service;

import java.util.Optional;
import static java.util.Collections.emptyList;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.repository.PersonRepository;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> userOptional = personRepository.findByLogin(username);
        if (userOptional.isEmpty()) {
           throw new UsernameNotFoundException(username);
        }
        return new User(userOptional.get().getLogin(), userOptional.get().getPassword(), emptyList());
    }

}
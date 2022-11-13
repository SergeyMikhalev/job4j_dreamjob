package ru.job4j.dreamjob.service;


import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.store.UsersDBStore;

import java.util.Optional;

@Service
public class UsersService {
    private final UsersDBStore store;

    public UsersService(UsersDBStore store) {
        this.store = store;
    }

    public Optional<User> add(User user) {
        return store.add(user);
    }

    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        return store.findUserByEmailAndPassword(email, password);
    }
}


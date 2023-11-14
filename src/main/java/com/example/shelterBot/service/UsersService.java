package com.example.shelterBot.service;

import com.example.shelterBot.model.people.Users;
import com.example.shelterBot.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class UsersService {

    private final UsersRepository repository;

    @Autowired
    public UsersService(UsersRepository repository) {
        this.repository = repository;
    }

    public Collection<Users> allUsers() {
        return (Collection<Users>) repository.findAll();
    }

    public boolean ifUserExists(User telegramUser) {
        Users persistentUser = repository.findUsersByTelegramUserId(telegramUser.getId());
        if (persistentUser == null) {
            Users transientUser = new Users();
            transientUser.setTelegramUserId(telegramUser.getId());
            transientUser.setFirstName(telegramUser.getFirstName());
            transientUser.setLastName(telegramUser.getLastName());
            transientUser.setFirstLoginDate(LocalDateTime.now());
            repository.save(transientUser);
            return false;
        }
        return true;
    }

    public Users addUser(Users users) {
        return repository.save(users);
    }

    public Users editUser(Users users) {
        return repository.findById(users.getId())
                .map(i -> {
                    i.setFirstName(users.getFirstName());
                    i.setLastName(users.getLastName());
                    i.setAge(users.getAge());
                    return repository.save(i);
                }).orElse(null);
    }

    public void deleteUser(long id) {
        repository.deleteById(id);
    }

    public Users findUser(long id) {
        return repository.findById(id).orElse(null);
    }
}

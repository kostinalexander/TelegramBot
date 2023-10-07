package com.example.shelterBot.repository;

import com.example.shelterBot.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<Users,Long> {
    Users findUsersByTelegramUserId(Long id);

}

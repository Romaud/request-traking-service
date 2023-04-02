package com.requesttraking.service;

import com.requesttraking.entity.User;
import com.requesttraking.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public List<User> searchUsers(String filter) {
        return userRepository.findByNameFilter(filter);
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Пользователь с id: " + id + " не найден"));
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new RuntimeException("Пользователь с username: " + username + " не найден"));
    }
}

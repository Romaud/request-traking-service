package com.requesttraking.service;

import com.requesttraking.entity.Role;
import com.requesttraking.entity.User;
import com.requesttraking.entity.common.RoleType;
import com.requesttraking.exception.ResourceNotFoundException;
import com.requesttraking.repository.RoleRepository;
import com.requesttraking.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public List<User> searchUsers(String filter) {
        List<User> users = userRepository.findByNameFilter(filter);
        if (users.size() == 0) {
            throw new ResourceNotFoundException("Нет пользователей с заданным фильтром: " + filter);
        }
        return users;
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Пользователь с id: " + id + " не найден"));
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new ResourceNotFoundException("Пользователь с username: " + username + " не найден"));
    }

    public User update(Long userId) {
        Role role = roleRepository.findByRole(RoleType.ROLE_OPERATOR);
        User user = getById(userId);
        Set<Role> roles = user.getRoles();
        roles.add(role);
        user.setRoles(roles);
        return userRepository.save(user);
    }
}

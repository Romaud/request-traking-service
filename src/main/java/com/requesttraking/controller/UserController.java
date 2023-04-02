package com.requesttraking.controller;

import com.requesttraking.entity.User;
import com.requesttraking.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String filter) {
        return ResponseEntity.ok(userService.searchUsers(filter));
    }
}

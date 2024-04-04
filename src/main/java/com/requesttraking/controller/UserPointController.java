package com.requesttraking.controller;

import com.requesttraking.service.UserPointService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/userPoint")
@AllArgsConstructor
public class UserPointController {
    private UserPointService userPointService;

    @PostMapping
    public ResponseEntity<?> saveOrUpdate(@RequestParam Long userId,
                                          @RequestParam Long point) {
        return ResponseEntity.ok(userPointService.saveOrUpdate(userId, point));
    }
}

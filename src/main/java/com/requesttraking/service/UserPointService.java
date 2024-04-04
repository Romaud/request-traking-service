package com.requesttraking.service;

import com.requesttraking.entity.UserPoint;
import com.requesttraking.repository.UserPointRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserPointService {
    private UserPointRepository userPointRepository;
    private UserService userService;

    public UserPoint saveOrUpdate(Long userId, Long point) {
        UserPoint currentPoint = userPointRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("Пользователя c таким ID не существует"));

        Long balance = currentPoint.getBalance();
        balance += point;
        currentPoint.setBalance(balance);

        return userPointRepository.save(currentPoint);
    }
}

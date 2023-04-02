package com.requesttraking.controller;

import com.requesttraking.entity.Request;
import com.requesttraking.entity.User;
import com.requesttraking.entity.common.Status;
import com.requesttraking.service.RequestService;
import com.requesttraking.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class RequestController {
    private RequestService requestService;
    private UserService userService;

    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @GetMapping("/requests")
    public ResponseEntity<List<Request>> getAllRequests(@RequestParam(defaultValue = "0") Integer page,
                                                        @RequestParam(defaultValue = "5") Integer size,
                                                        @RequestParam(defaultValue = "createdAt") String sort,
                                                        @RequestParam(defaultValue = "asc") String direction) {
        Page<Request> requests = requestService.getAllRequests(page, size, sort, direction);
        return ResponseEntity.ok(requests.getContent());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user/requests")
    public ResponseEntity<List<Request>> getUserRequests(@RequestParam(defaultValue = "0") Integer page,
                                                         @RequestParam(defaultValue = "5") Integer size,
                                                         @RequestParam(defaultValue = "createdAt") String sort,
                                                         @RequestParam(defaultValue = "asc") String direction) {
        Page<Request> requests = requestService.getUserRequests(getAuthenticatedName(), page, size, sort, direction);
        return ResponseEntity.ok(requests.getContent());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/request")
    public ResponseEntity<Request> createRequest(@RequestBody Request request) {
        User user = userService.getByUsername(getAuthenticatedName());
        request.setStatus(Status.DRAFT);
        request.setUserId(user.getId());
        Request savedRequest = requestService.save(request);
        return ResponseEntity.ok(savedRequest);
    }

    @GetMapping("/request/{id}")
    public ResponseEntity<Request> getById(@PathVariable Long id) {
        Request request = requestService.getById(id);
        return ResponseEntity.ok(request);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/request/{id}/send")
    public ResponseEntity<Request> sendRequest(@PathVariable Long id) {
        Request request = requestService.send(id);
        return ResponseEntity.ok(request);
    }

    @PutMapping("/request/{id}/accept")
    public ResponseEntity<Request> acceptRequest(@PathVariable Long id) {
        Request request = requestService.accept(id);
        return ResponseEntity.ok(request);
    }

    @PutMapping("/request/{id}/reject")
    public ResponseEntity<Request> rejectRequest(@PathVariable Long id) {
        Request request = requestService.reject(id);
        return ResponseEntity.ok(request);
    }

    @PutMapping("/request/{id}")
    public ResponseEntity<Request> updateRequest(@PathVariable Long id) {
        Request request = requestService.update(id);
        return ResponseEntity.ok(request);
    }

    private String getAuthenticatedName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}

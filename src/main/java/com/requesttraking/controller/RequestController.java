package com.requesttraking.controller;

import com.requesttraking.dto.CreateRqDto;
import com.requesttraking.dto.UpdateRqDto;
import com.requesttraking.entity.Request;
import com.requesttraking.service.JasperReportsService;
import com.requesttraking.service.RequestService;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/requests")
public class RequestController {
    private RequestService requestService;
    private JasperReportsService jasperReportsService;

    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @GetMapping
    public ResponseEntity<List<Request>> getSubmittedRe127quests(@RequestParam(defaultValue = "0") Integer page,
                                                              @RequestParam(defaultValue = "5") Integer size,
                                                              @RequestParam(defaultValue = "asc") String direction) {
        Page<Request> requests = requestService.getSubmittedRequests(page, size, direction);
        return ResponseEntity.ok(requests.getContent());
    }

    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @GetMapping("/filter")
    public ResponseEntity<List<Request>> getSubmittedUserRequests(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "") Long userId) {
        Page<Request> requests = requestService.getSubmittedUserRequests(page, size, direction, userId);
        return ResponseEntity.ok(requests.getContent());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    public ResponseEntity<List<Request>> getUserRequests(@RequestParam(defaultValue = "0") Integer page,
                                                         @RequestParam(defaultValue = "5") Integer size,
                                                         @RequestParam(defaultValue = "asc") String direction) {
        Page<Request> requests = requestService.getUserRequests(page, size, direction);
        return ResponseEntity.ok(requests.getContent());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user/{id}")
    public ResponseEntity<Request> getUserRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(requestService.geUserRequestById(id));
    }

    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @GetMapping("/{id}")
    public ResponseEntity<Request> getByIdForOperator(@PathVariable Long id) {
        return ResponseEntity.ok(requestService.geByIdForOperator(id));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<Request> createRequest(@Validated @RequestBody CreateRqDto dto) {
        Request savedRequest = requestService.save(dto);
        return ResponseEntity.ok(savedRequest);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/edit")
    public ResponseEntity<Request> editRequest(@RequestBody UpdateRqDto dto) {
        Request saveRequest = requestService.edit(dto);
        return ResponseEntity.ok(saveRequest);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/send/{id}")
    public ResponseEntity<Request> sendRequest(@PathVariable Long id) {
        Request request = requestService.send(id);
        return ResponseEntity.ok(request);
    }

    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PutMapping("/accept/{id}")
    public ResponseEntity<Request> acceptRequest(@PathVariable Long id) {
        Request request = requestService.accept(id);
        return ResponseEntity.ok(request);
    }

    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PutMapping("/reject/{id}")
    public ResponseEntity<Request> rejectRequest(@PathVariable Long id) {
        Request request = requestService.reject(id);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/export")
    public ResponseEntity<?> export() throws JRException {
        return ResponseEntity.ok(jasperReportsService.exportReport());
    }
}

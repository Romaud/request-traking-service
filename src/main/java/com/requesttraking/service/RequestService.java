package com.requesttraking.service;

import com.requesttraking.dto.CreateRqDto;
import com.requesttraking.dto.UpdateRqDto;
import com.requesttraking.entity.Request;
import com.requesttraking.entity.User;
import com.requesttraking.entity.common.Status;
import com.requesttraking.exception.ForbiddenResourceException;
import com.requesttraking.exception.ResourceNotFoundException;
import com.requesttraking.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;

    @Value("${request-tracking-config.get-page-config.sort-by}")
    private String sortBy;

    public RequestService(RequestRepository requestRepository, UserService userService) {
        this.requestRepository = requestRepository;
        this.userService = userService;
    }

    public Page<Request> getSubmittedRequests(Integer pageNumber, Integer pageSize, String direction) {
        Pageable pageable = checkDirection(pageNumber, pageSize, sortBy, direction);
        return requestRepository.findByStatus(Status.SENT, pageable);
    }

    public Page<Request> getSubmittedUserRequests(Integer pageNumber, Integer pageSize, String direction, Long userId) {
        Pageable pageable = checkDirection(pageNumber, pageSize, sortBy, direction);
        return requestRepository.findByStatusAndUserId(Status.SENT, userId, pageable);
    }

    public Page<Request> getUserRequests(Integer pageNumber, Integer pageSize, String direction) {
        User user = userService.getByUsername(getAuthenticatedName());
        Pageable pageable = checkDirection(pageNumber, pageSize, sortBy, direction);
        return requestRepository.findByUserId(user.getId(), pageable);
    }

    public Request geUserRequestById(Long id) {
        User user = userService.getByUsername(getAuthenticatedName());
        Request rqFromDb = getById(id);
        if (!Objects.equals(rqFromDb.getUserId(), user.getId())) {
            throw new ForbiddenResourceException("Заявка с id: " + id + " не принадлежит пользователю");
        }
        return rqFromDb;
    }

    public Request geByIdForOperator(Long id) {
        Request rqFromDb = getById(id);
        if (rqFromDb.getStatus() == Status.DRAFT) {
            throw new ForbiddenResourceException("Нельзя просматиртвать заявки в статусе 'Черновик'");
        }
        rqFromDb.setText(convertString(rqFromDb.getText()));
        return rqFromDb;
    }

    public Request getById(Long id) {
        return requestRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Заявка с id: " + id + " не найдена"));
    }

    public Request save(CreateRqDto dto) {
        User user = userService.getByUsername(getAuthenticatedName());
        dto.setUserid(user.getId());
        return requestRepository.save(dto.toEntity());
    }

    public Request edit(UpdateRqDto dto) {
        Request rqFromDb = getById(dto.getId());
        Request rqForSave = dto.toEntity();
        if (rqFromDb.getStatus() != Status.DRAFT) {
            throw new IllegalArgumentException("Редактировать можно только заявки в статусе 'Черновик'");
        }
        rqForSave.setText(rqForSave.getText());
        rqForSave.setCreatedAt(rqFromDb.getCreatedAt());
        return requestRepository.save(rqForSave);
    }

    public Request send(Long id) {
        Request request = getById(id);
        if (request.getStatus() != Status.DRAFT) {
            throw new IllegalArgumentException("Редактировать можно только заявки в статусе 'Черновик'");
        }
        request.setStatus(Status.SENT);
        requestRepository.save(request);
        return request;
    }

    public Request accept(Long id) {
        Request request = getById(id);
        if (request.getStatus() != Status.SENT) {
            throw new IllegalArgumentException("Принимать можно только заявки в статусе 'Отправлено'");
        }
        request.setStatus(Status.ACCEPTED);
        User user = userService.getByUsername(getAuthenticatedName());
        request.setAssigneeId(user.getId());
        return requestRepository.save(request);
    }

    public Request reject(Long id) {
        Request request = getById(id);
        request.setStatus(Status.REJECTED);
        return requestRepository.save(request);
    }

    private Pageable checkDirection(Integer pageNumber, Integer pageSize,
                                    String sort, String direction) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sort).ascending());
        if (direction.equalsIgnoreCase("desc")) {
            return PageRequest.of(pageNumber, pageSize, Sort.by(sort).descending());
        }
        return pageable;
    }

    private String getAuthenticatedName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private static String convertString(String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            result.append(s.charAt(i));
            result.append("-");
        }
        return result.substring(0, result.length() - 1);
    }
}

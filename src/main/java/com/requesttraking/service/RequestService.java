package com.requesttraking.service;

import com.requesttraking.entity.Request;
import com.requesttraking.entity.User;
import com.requesttraking.entity.common.Status;
import com.requesttraking.repository.RequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RequestService {
    private RequestRepository requestRepository;
    private UserService userService;

    public Page<Request> getAllRequests(Integer pageNumber, Integer pageSize, String sort, String direction) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sort).ascending());
        if (direction.equalsIgnoreCase("desc")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sort).descending());
        }
        return requestRepository.findAll(pageable);
    }

    public Page<Request> getUserRequests(String username, Integer pageNumber,
                                         Integer pageSize, String sort, String direction) {
        User user = userService.getByUsername(username);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sort).ascending());
        if (direction.equalsIgnoreCase("desc")) {
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sort).descending());
        }

        return requestRepository.findByUserId(user.getId(), pageable);
    }

    public Request getById(Long id) {
        return requestRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Заявка с id: " + id + " не найдена"));
    }

    public Request save(Request request) {
        System.out.println(request);
        userService.getById(request.getUserId());
        return requestRepository.save(request);
    }

    public Request accept(Long id) {
        Request request = getById(id);
        request.setStatus(Status.ACCEPTED);
        requestRepository.save(request);
        return request;
    }

    public Request send(Long id) {
        Request request = getById(id);
        request.setStatus(Status.SENT);
        requestRepository.save(request);
        return request;
    }

    public Request reject(Long id) {
        Request request = getById(id);
        request.setStatus(Status.REJECTED);
        requestRepository.save(request);
        return request;
    }

    public Request update(Long id) {
        Request request = getById(id);
        if (request.getStatus() != Status.DRAFT) {
            //new RuntimeException("Заявку с id: " + id + " нельзя редактиировать");
            return request;
        }
        requestRepository.save(request);
        return request;
    }
}

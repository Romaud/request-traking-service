package com.requesttraking.repository;

import com.requesttraking.entity.Request;
import com.requesttraking.entity.common.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    Page<Request> findByUserId(Long username, Pageable pageable);

    Page<Request> findByStatus(Status status, Pageable pageable);

    Page<Request> findByStatusAndUserId(Status status, Long userId, Pageable pageable);
}
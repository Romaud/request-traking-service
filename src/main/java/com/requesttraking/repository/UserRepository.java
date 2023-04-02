package com.requesttraking.repository;

import com.requesttraking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from users u where upper(u.username) like %?1%")
    List<User> findByNameFilter(String filter);

    Optional<User> findByUsername(String username);
}
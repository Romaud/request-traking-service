package com.requesttraking.repository;

import com.requesttraking.entity.Role;
import com.requesttraking.entity.common.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(RoleType roleType);
}
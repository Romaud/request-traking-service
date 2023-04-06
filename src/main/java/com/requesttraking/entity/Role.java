package com.requesttraking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.requesttraking.entity.common.RoleType;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}

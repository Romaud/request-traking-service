package com.requesttraking.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @ManyToMany
    @ToString.Exclude
    private Set<Role> roles;
}

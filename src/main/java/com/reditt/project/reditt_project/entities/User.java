package com.reditt.project.reditt_project.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder // generates builder methods for our class
@Entity
@Data // generates getters and setters
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotNull(message = "User name cannot be empty or null")
    private String userName;

    @NotNull(message = "Password cannot be empty or null")
    private String password;

    @Email
    @NotEmpty(message = "Email id is required")
    private String email;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private Instant createdDate;

    private boolean enabled; //USER WILL BE ENABLED AFTER COMPLETING THE EMAIL VERIFICATION PROCESS

}

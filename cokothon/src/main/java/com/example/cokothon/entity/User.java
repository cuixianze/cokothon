package com.example.cokothon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String name;
    
    @Column
    private String email;
    
    @Column(name = "is_admin", nullable = false, columnDefinition = "boolean default false")
    private Boolean isAdmin = false;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    public User(String username, String password, String name, String email, Boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.isAdmin = isAdmin != null ? isAdmin : false;
    }
}

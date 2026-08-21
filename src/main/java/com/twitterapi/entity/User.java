package com.twitterapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ad bos olamaz")
    @Size(max = 255)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Soyad bos olamaz")
    @Size(max = 255)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Kullanici adi bos olamaz")
    @Size(min = 3, max = 30, message = "Kullanici adi 3-30 karakter arasinda olmalidir")
    @Column(nullable = false, length = 30)
    private String username;

    @NotBlank(message = "Email bos olamaz")
    @Email(message = "Gecerli bir email adresi giriniz")
    @Size(max = 255)
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Sifre bos olamaz")
    @Size(max = 255)
    @Column(nullable = false)
    private String password; // BCrypt hash olarak saklanir

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

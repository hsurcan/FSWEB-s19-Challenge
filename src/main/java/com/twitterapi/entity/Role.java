package com.twitterapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Yetki adi bos olamaz")
    @Size(max = 50, message = "Yetki adi en fazla 50 karakter olabilir")
    @Column(nullable = false, unique = true, length = 50)
    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}

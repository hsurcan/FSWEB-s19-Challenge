package com.twitterapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Ad bos olamaz")
        String firstName,

        @NotBlank(message = "Soyad bos olamaz")
        String lastName,

        @NotBlank(message = "Kullanici adi bos olamaz")
        @Size(min = 3, max = 30, message = "Kullanici adi 3-30 karakter arasinda olmalidir")
        String username,

        @NotBlank(message = "Email bos olamaz")
        @Email(message = "Gecerli bir email adresi giriniz")
        String email,

        @NotBlank(message = "Sifre bos olamaz")
        @Size(min = 6, message = "Sifre en az 6 karakter olmalidir")
        String password
) {}

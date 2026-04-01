package com.valgames.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterDto {

    @NotBlank(message = "Field must not be blank")
    private String name;

    @NotBlank(message = "Field must not be blank")
    @Email(message = "Email must have proper format")
    private String email;

    @NotBlank(message = "Field must not be blank")
    @Size(min = 4,message = "Password must be at least 4 characters")
    private String password;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

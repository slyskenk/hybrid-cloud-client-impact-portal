package com.ibmjob.hybridportal.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class UserAccount extends PortfolioRecord {

    @NotBlank
    private String username;

    @NotBlank
    private String displayName;

    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    public UserAccount() {
    }

    public UserAccount(String username, String displayName, String email, Role role) {
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

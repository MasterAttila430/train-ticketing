package com.siemens.train.model;

// Represents a system user, either an admin or a customer
public class AppUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String username;
    private String passwordHash;
    private String email;
    private Role role;

    public AppUser() {
        super();
    }

    public AppUser(Long id, String username, String passwordHash,
                   String email, Role role) {
        super(id);
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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

    @Override
    public String toString() {
        return "AppUser{"
                + "id=" + id
                + ", username='" + username + '\''
                + ", email='" + email + '\''
                + ", role=" + role
                + '}';
    }
}
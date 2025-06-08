package com.labpro;

import java.util.Objects;

public class User {
    private String username;
    private boolean isAdmin;

    public User(String username, String password, boolean isAdmin) {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("Username dan password tidak bisa null atau kosong.");
        }
        this.username = username;
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username tidak bisa null.");
        }
        this.username = username;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equalsIgnoreCase(user.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
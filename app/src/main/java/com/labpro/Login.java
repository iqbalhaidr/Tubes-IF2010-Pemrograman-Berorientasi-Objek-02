package com.labpro;

public class Login{
    private User activeUser;

    public boolean authenticate(String username, String password, List<User> users) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.validatePassword(password)) {
                this.activeUser = user;
                return true;
            }
        }
        return false;
    }

    public void logout() {
        this.activeUser = null;
        System.out.println("User logged out.");
    }

    public User getActiveUser() {
        return activeUser;
    }

    public boolean isLoggedIn() {
        return activeUser != null;
    }
}
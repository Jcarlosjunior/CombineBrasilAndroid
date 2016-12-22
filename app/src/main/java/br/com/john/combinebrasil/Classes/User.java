package br.com.john.combinebrasil.Classes;

/**
 * Created by GTAC on 19/10/2016.
 */

public class User {
    String id;
    String name;
    String email;
    boolean isAdmin;
    boolean canWrite;
    String Token;

    public User(){}

    public User(String id, String name, String email, boolean isAdmin, boolean canWrite, String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.Token = token;
        this.isAdmin = isAdmin;
        this.canWrite = canWrite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getCanWrite() {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        this.Token = token;
    }
}

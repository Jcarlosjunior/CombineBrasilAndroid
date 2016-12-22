package br.com.john.combinebrasil.Classes;

/**
 * Created by GTAC on 21/12/2016.
 */

public class Login {
    private String Email;
    private boolean IsAdmin;
    private boolean CanWrite;
    private String Token;

    public Login(){

    }
    public Login(String email, boolean isAdmin, boolean canWrite, String token){
        Email = email;
        IsAdmin = isAdmin;
        CanWrite = canWrite;
        Token = token;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public boolean getIsAdmin() {
        return IsAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        IsAdmin = isAdmin;
    }

    public boolean getCanWrite() {
        return CanWrite;
    }

    public void setCanWrite(boolean canWrite) {
        CanWrite = canWrite;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}

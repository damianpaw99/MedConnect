package edu.ib.security;

public class Logger {
    private String login;
    private String password;

    public Logger() {
    }

    public Logger(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isCorrect(){
        if(login.equals("")){
            password="";
            return false;
        }
        if(password.equals("") || password.length()<5 || password.length()>10){
            password="";
            return false;
        }
        return true;
    }
    public boolean isPasswordCorrect(){
        if(password.equals("") || password.length()<5 || password.length()>10){
            password="";
            return false;
        }
        return true;
    }
}

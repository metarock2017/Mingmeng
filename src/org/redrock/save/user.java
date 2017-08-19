package org.redrock.save;

public class user {
    private String Name;
    private String Password;
    public String question;
    public String answer;

    public void setName(String name) {
        this.Name = name;
    }
    public String getName(){
        return this.Name;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getPassword() {
        return this.Password;
    }
}

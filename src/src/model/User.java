package model;

public abstract class User {
    protected String userID;
    protected String name;
    protected String email;
    protected String role;
    protected String password;

    public User(String userID, String name, String email, String role, String password) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    public String getUserID()   { return userID; }
    public String getName()     { return name; }
    public String getEmail()    { return email; }
    public String getRole()     { return role; }
    public String getPassword() { return password; }
}

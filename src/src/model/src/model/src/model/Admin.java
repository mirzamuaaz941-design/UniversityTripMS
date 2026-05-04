package model;

public class Admin extends User {
    private String adminID;
    private int accessLevel;

    public Admin(String userID, String name, String email, String password,
                 String adminID, int accessLevel) {
        super(userID, name, email, "Admin", password);
        this.adminID = adminID;
        this.accessLevel = accessLevel;
    }

    public String getAdminID()  { return adminID; }
    public int getAccessLevel() { return accessLevel; }
}

package model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private String studentID;
    private String department;
    private List<String> enrolledTrips;

    public Student(String userID, String name, String email, String password,
                   String studentID, String department) {
        super(userID, name, email, "Student", password);
        this.studentID = studentID;
        this.department = department;
        this.enrolledTrips = new ArrayList<>();
    }

    public void enroll(String tripID) {
        if (!enrolledTrips.contains(tripID)) {
            enrolledTrips.add(tripID);
        }
    }

    public String getStudentID()           { return studentID; }
    public String getDepartment()          { return department; }
    public List<String> getEnrolledTrips() { return enrolledTrips; }
}

package service;

import model.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static DataStore instance;

    private List<User> users       = new ArrayList<>();
    private List<Trip> trips       = new ArrayList<>();
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<TripRequest> requests = new ArrayList<>();

    private DataStore() {
        loadSampleData();
    }

    public static DataStore getInstance() {
        if (instance == null) instance = new DataStore();
        return instance;
    }

    private void loadSampleData() {
        users.add(new Student("U001","Ali Khan","ali@fast.edu","123","S001","CS"));
        users.add(new Student("U002","Sara Ahmed","sara@fast.edu","123","S002","EE"));
        users.add(new Teacher("U003","Dr. Umar","umar@fast.edu","123","T001","CS"));
        users.add(new Admin("U004","Admin","admin@fast.edu","admin","A001",1));

        vehicles.add(new Vehicle("V001","ABC-123",40));
        vehicles.add(new Vehicle("V002","XYZ-456",30));
        vehicles.add(new Vehicle("V003","DEF-789",50));

        Trip t1 = new Trip("TR001","Lahore Study Tour","Lahore",
                "2026-06-01","2026-06-03",40,"U003");
        t1.setStatus(Trip.Status.ENROLLMENT_OPEN);
        trips.add(t1);

        Trip t2 = new Trip("TR002","Islamabad Conference","Islamabad",
                "2026-07-10","2026-07-12",30,"U003");
        t2.setStatus(Trip.Status.APPROVED);
        trips.add(t2);

        Trip t3 = new Trip("TR003","Murree Winter Camp","Murree",
                "2026-08-05","2026-08-07",50,"U003");
        t3.setStatus(Trip.Status.PENDING);
        trips.add(t3);
    }

    public User login(String email, String password) {
        for (User u : users)
            if (u.getEmail().equals(email) && u.getPassword().equals(password))
                return u;
        return null;
    }

    public List<Trip> getAllTrips()             { return trips; }
    public List<Vehicle> getAllVehicles()       { return vehicles; }
    public List<User> getAllUsers()             { return users; }
    public List<TripRequest> getAllRequests()   { return requests; }

    public void addTrip(Trip t)                { trips.add(t); }
    public void addRequest(TripRequest r)      { requests.add(r); }

    public Trip getTripByID(String id) {
        for (Trip t : trips) if (t.getTripID().equals(id)) return t;
        return null;
    }

    public Vehicle getAvailableVehicle() {
        for (Vehicle v : vehicles) if (v.isAvailable()) return v;
        return null;
    }
}

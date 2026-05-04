package model;

public class Vehicle {
    private String vehicleID;
    private String plateNo;
    private int capacity;
    private boolean isAvailable;

    public Vehicle(String vehicleID, String plateNo, int capacity) {
        this.vehicleID = vehicleID;
        this.plateNo = plateNo;
        this.capacity = capacity;
        this.isAvailable = true;
    }

    public void assign()  { this.isAvailable = false; }
    public void release() { this.isAvailable = true; }

    public String getVehicleID() { return vehicleID; }
    public String getPlateNo()   { return plateNo; }
    public int getCapacity()     { return capacity; }
    public boolean isAvailable() { return isAvailable; }
}

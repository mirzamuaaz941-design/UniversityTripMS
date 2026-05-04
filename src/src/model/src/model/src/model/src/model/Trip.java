package model;

public class Trip {
    public enum Status {
        DRAFT, PENDING, APPROVED, ENROLLMENT_OPEN,
        DEPARTED, COMPLETED, CANCELLED, REJECTED
    }

    private String tripID;
    private String title;
    private String destination;
    private String departureDate;
    private String returnDate;
    private int capacity;
    private Status status;
    private String vehicleID;
    private String requestedBy;

    public Trip(String tripID, String title, String destination,
                String departureDate, String returnDate,
                int capacity, String requestedBy) {
        this.tripID = tripID;
        this.title = title;
        this.destination = destination;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.capacity = capacity;
        this.status = Status.DRAFT;
        this.requestedBy = requestedBy;
    }

    public void approve()        { this.status = Status.APPROVED; }
    public void reject()         { this.status = Status.REJECTED; }
    public void openEnrollment() { this.status = Status.ENROLLMENT_OPEN; }
    public void cancel()         { this.status = Status.CANCELLED; }
    public void depart()         { this.status = Status.DEPARTED; }
    public void complete()       { this.status = Status.COMPLETED; }

    public String getTripID()        { return tripID; }
    public String getTitle()         { return title; }
    public String getDestination()   { return destination; }
    public String getDepartureDate() { return departureDate; }
    public String getReturnDate()    { return returnDate; }
    public int getCapacity()         { return capacity; }
    public Status getStatus()        { return status; }
    public String getVehicleID()     { return vehicleID; }
    public String getRequestedBy()   { return requestedBy; }
    public void setVehicleID(String v)  { this.vehicleID = v; }
    public void setStatus(Status s)     { this.status = s; }
}

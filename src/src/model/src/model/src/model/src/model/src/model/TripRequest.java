package model;

public class TripRequest {
    private String requestID;
    private String destination;
    private String travelDate;
    private int participants;
    private String status;
    private String submittedBy;

    public TripRequest(String requestID, String destination, String travelDate,
                       int participants, String submittedBy) {
        this.requestID = requestID;
        this.destination = destination;
        this.travelDate = travelDate;
        this.participants = participants;
        this.submittedBy = submittedBy;
        this.status = "PENDING";
    }

    public void submit()            { this.status = "SUBMITTED"; }

    public String getRequestID()    { return requestID; }
    public String getDestination()  { return destination; }
    public String getTravelDate()   { return travelDate; }
    public int getParticipants()    { return participants; }
    public String getStatus()       { return status; }
    public String getSubmittedBy()  { return submittedBy; }
    public void setStatus(String s) { this.status = s; }
}

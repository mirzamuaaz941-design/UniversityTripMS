package gui;

import model.*;
import service.DataStore;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminDashboard extends JFrame {
    private Admin admin;
    private DefaultTableModel tripTableModel;
    private DefaultTableModel vehicleTableModel;

    public AdminDashboard(Admin admin) {
        this.admin = admin;
        setTitle("Admin Dashboard — " + admin.getName());
        setSize(850, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel top = new JPanel(new BorderLayout());
        JLabel welcome = new JLabel("Admin Panel  |  " + admin.getName());
        welcome.setFont(new Font("SansSerif", Font.BOLD, 14));
        JButton logoutBtn = new JButton("Logout");
        top.add(welcome, BorderLayout.WEST);
        top.add(logoutBtn, BorderLayout.EAST);
        main.add(top, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        // Tab 1: Manage Trips
        JPanel tripsPanel = new JPanel(new BorderLayout(5, 5));
        String[] tripCols = {"Trip ID","Title","Destination","Departure","Capacity","Status"};
        tripTableModel = new DefaultTableModel(tripCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tripTable = new JTable(tripTableModel);
        tripTable.setRowHeight(24);
        tripTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        loadTrips();

        JPanel tripBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton approveBtn       = new JButton("Approve");
        JButton rejectBtn        = new JButton("Reject");
        JButton openEnrollBtn    = new JButton("Open Enrollment");
        JButton cancelBtn        = new JButton("Cancel Trip");
        JButton assignVehicleBtn = new JButton("Assign Vehicle");
        tripBtns.add(approveBtn);
        tripBtns.add(rejectBtn);
        tripBtns.add(openEnrollBtn);
        tripBtns.add(cancelBtn);
        tripBtns.add(assignVehicleBtn);

        tripsPanel.add(new JScrollPane(tripTable), BorderLayout.CENTER);
        tripsPanel.add(tripBtns, BorderLayout.SOUTH);
        tabs.addTab("Manage Trips", tripsPanel);

        // Tab 2: Vehicles
        JPanel vehiclePanel = new JPanel(new BorderLayout(5, 5));
        String[] vCols = {"Vehicle ID","Plate No","Capacity","Available"};
        vehicleTableModel = new DefaultTableModel(vCols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable vehicleTable = new JTable(vehicleTableModel);
        vehicleTable.setRowHeight(24);
        loadVehicles();
        vehiclePanel.add(new JScrollPane(vehicleTable), BorderLayout.CENTER);
        tabs.addTab("Vehicles", vehiclePanel);

        // Tab 3: Add Trip
        JPanel addTripPanel = new JPanel(new GridBagLayout());
        addTripPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField aTitleField = new JTextField(15);
        JTextField aDestField  = new JTextField(15);
        JTextField aDepField   = new JTextField(15);
        JTextField aRetField   = new JTextField(15);
        JTextField aCapField   = new JTextField(15);

        String[] labels = {"Title:","Destination:",
                "Departure (YYYY-MM-DD):","Return (YYYY-MM-DD):","Capacity:"};
        JTextField[] fields = {aTitleField, aDestField, aDepField, aRetField, aCapField};
        for (int i = 0; i < labels.length; i++) {
            g.gridx = 0; g.gridy = i; g.gridwidth = 1;
            addTripPanel.add(new JLabel(labels[i]), g);
            g.gridx = 1;
            addTripPanel.add(fields[i], g);
        }
        g.gridx = 0; g.gridy = labels.length; g.gridwidth = 2;
        JButton addTripBtn = new JButton("Add Trip");
        addTripPanel.add(addTripBtn, g);
        tabs.addTab("Add New Trip", addTripPanel);

        main.add(tabs, BorderLayout.CENTER);
        add(main);

        // Actions
        approveBtn.addActionListener(e ->
                changeTripStatus(tripTable, "APPROVE"));
        rejectBtn.addActionListener(e ->
                changeTripStatus(tripTable, "REJECT"));
        openEnrollBtn.addActionListener(e ->
                changeTripStatus(tripTable, "OPEN_ENROLLMENT"));
        cancelBtn.addActionListener(e ->
                changeTripStatus(tripTable, "CANCEL"));

        assignVehicleBtn.addActionListener(e -> {
            int row = tripTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this,
                        "Select a trip first.", "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String tripID = (String) tripTableModel.getValueAt(row, 0);
            Trip trip = DataStore.getInstance().getTripByID(tripID);
            Vehicle v = DataStore.getInstance().getAvailableVehicle();
            if (v == null) {
                JOptionPane.showMessageDialog(this,
                        "No vehicles available.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            trip.setVehicleID(v.getVehicleID());
            v.assign();
            loadTrips();
            loadVehicles();
            JOptionPane.showMessageDialog(this,
                    "Vehicle " + v.getPlateNo() + " assigned to trip.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        addTripBtn.addActionListener(e -> {
            String title = aTitleField.getText().trim();
            String dest  = aDestField.getText().trim();
            String dep   = aDepField.getText().trim();
            String ret   = aRetField.getText().trim();
            String cap   = aCapField.getText().trim();

            if (title.isEmpty() || dest.isEmpty() || dep.isEmpty()
                    || ret.isEmpty() || cap.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill all fields.", "Missing Fields",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            int capacity;
            try { capacity = Integer.parseInt(cap); }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Capacity must be a number.", "Invalid",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            String tripID = "TR" + System.currentTimeMillis();
            Trip t = new Trip(tripID, title, dest, dep, ret, capacity, admin.getUserID());
            t.setStatus(Trip.Status.APPROVED);
            DataStore.getInstance().addTrip(t);
            loadTrips();
            JOptionPane.showMessageDialog(this,
                    "Trip added: " + tripID, "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            for (JTextField f : fields) f.setText("");
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginScreen().setVisible(true);
        });
    }

    private void changeTripStatus(JTable table, String action) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select a trip first.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String tripID = (String) tripTableModel.getValueAt(row, 0);
        Trip trip = DataStore.getInstance().getTripByID(tripID);
        if (trip == null) return;
        switch (action) {
            case "APPROVE":         trip.approve(); break;
            case "REJECT":          trip.reject(); break;
            case "OPEN_ENROLLMENT": trip.openEnrollment(); break;
            case "CANCEL":          trip.cancel(); break;
        }
        loadTrips();
        JOptionPane.showMessageDialog(this,
                "Status updated to: " + trip.getStatus(),
                "Updated", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadTrips() {
        tripTableModel.setRowCount(0);
        for (Trip t : DataStore.getInstance().getAllTrips()) {
            tripTableModel.addRow(new Object[]{
                t.getTripID(), t.getTitle(), t.getDestination(),
                t.getDepartureDate(), t.getCapacity(), t.getStatus().name()
            });
        }
    }

    private void loadVehicles() {
        vehicleTableModel.setRowCount(0);
        for (Vehicle v : DataStore.getInstance().getAllVehicles()) {
            vehicleTableModel.addRow(new Object[]{
                v.getVehicleID(), v.getPlateNo(),
                v.getCapacity(), v.isAvailable() ? "Yes" : "No"
            });
        }
    }
}

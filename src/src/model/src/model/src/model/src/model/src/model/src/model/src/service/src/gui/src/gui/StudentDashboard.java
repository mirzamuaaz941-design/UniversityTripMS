package gui;

import model.*;
import service.DataStore;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentDashboard extends JFrame {
    private Student student;
    private DefaultTableModel tableModel;

    public StudentDashboard(Student student) {
        this.student = student;
        setTitle("Student Dashboard — " + student.getName());
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel top = new JPanel(new BorderLayout());
        JLabel welcome = new JLabel("Welcome, " + student.getName()
                + "  |  " + student.getDepartment());
        welcome.setFont(new Font("SansSerif", Font.BOLD, 14));
        JButton logoutBtn = new JButton("Logout");
        top.add(welcome, BorderLayout.WEST);
        top.add(logoutBtn, BorderLayout.EAST);
        main.add(top, BorderLayout.NORTH);

        String[] cols = {"Trip ID","Title","Destination","Departure","Return","Capacity","Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        loadTrips();
        main.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton enrollBtn  = new JButton("Enroll in Selected Trip");
        JButton refreshBtn = new JButton("Refresh");
        JButton myTripsBtn = new JButton("My Enrolled Trips");
        bottom.add(enrollBtn);
        bottom.add(refreshBtn);
        bottom.add(myTripsBtn);
        main.add(bottom, BorderLayout.SOUTH);

        add(main);

        enrollBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this,
                        "Please select a trip first.", "No Selection",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String tripID = (String) tableModel.getValueAt(row, 0);
            String status = (String) tableModel.getValueAt(row, 6);
            if (!status.equals("ENROLLMENT_OPEN")) {
                JOptionPane.showMessageDialog(this,
                        "This trip is not open for enrollment.\nStatus: " + status,
                        "Enrollment Closed", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (student.getEnrolledTrips().contains(tripID)) {
                JOptionPane.showMessageDialog(this,
                        "You are already enrolled in this trip.",
                        "Already Enrolled", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            student.enroll(tripID);
            JOptionPane.showMessageDialog(this,
                    "Successfully enrolled in: " + tableModel.getValueAt(row, 1),
                    "Enrolled!", JOptionPane.INFORMATION_MESSAGE);
        });

        refreshBtn.addActionListener(e -> loadTrips());

        myTripsBtn.addActionListener(e -> {
            List<String> enrolled = student.getEnrolledTrips();
            if (enrolled.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "You have not enrolled in any trips yet.",
                        "My Trips", JOptionPane.INFORMATION_MESSAGE);
            } else {
                StringBuilder sb = new StringBuilder("Your enrolled trips:\n\n");
                for (String id : enrolled) {
                    Trip t = DataStore.getInstance().getTripByID(id);
                    if (t != null)
                        sb.append("• ").append(t.getTitle())
                          .append(" → ").append(t.getDestination()).append("\n");
                }
                JOptionPane.showMessageDialog(this, sb.toString(),
                        "My Trips", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginScreen().setVisible(true);
        });
    }

    private void loadTrips() {
        tableModel.setRowCount(0);
        for (Trip t : DataStore.getInstance().getAllTrips()) {
            tableModel.addRow(new Object[]{
                t.getTripID(), t.getTitle(), t.getDestination(),
                t.getDepartureDate(), t.getReturnDate(),
                t.getCapacity(), t.getStatus().name()
            });
        }
    }
}

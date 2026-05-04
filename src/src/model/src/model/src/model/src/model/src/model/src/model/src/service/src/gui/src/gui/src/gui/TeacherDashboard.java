package gui;

import model.*;
import service.DataStore;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TeacherDashboard extends JFrame {
    private Teacher teacher;
    private DefaultTableModel tableModel;

    public TeacherDashboard(Teacher teacher) {
        this.teacher = teacher;
        setTitle("Teacher Dashboard — " + teacher.getName());
        setSize(780, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel top = new JPanel(new BorderLayout());
        JLabel welcome = new JLabel("Teacher: " + teacher.getName()
                + "  |  " + teacher.getDepartment());
        welcome.setFont(new Font("SansSerif", Font.BOLD, 14));
        JButton logoutBtn = new JButton("Logout");
        top.add(welcome, BorderLayout.WEST);
        top.add(logoutBtn, BorderLayout.EAST);
        main.add(top, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();

        // Tab 1: View Trips
        JPanel tripsPanel = new JPanel(new BorderLayout(5, 5));
        String[] cols = {"Trip ID","Title","Destination","Departure","Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(24);
        loadTrips();
        tripsPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        tabs.addTab("All Trips", tripsPanel);

        // Tab 2: Submit Request
        JPanel reqPanel = new JPanel(new GridBagLayout());
        reqPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        JTextField titleField = new JTextField(15);
        JTextField destField  = new JTextField(15);
        JTextField dateField  = new JTextField(15);
        JTextField partField  = new JTextField(15);

        String[] labels = {"Trip Title:","Destination:","Travel Date (YYYY-MM-DD):","No. of Participants:"};
        JTextField[] fields = {titleField, destField, dateField, partField};
        for (int i = 0; i < labels.length; i++) {
            g.gridx = 0; g.gridy = i; g.gridwidth = 1;
            reqPanel.add(new JLabel(labels[i]), g);
            g.gridx = 1;
            reqPanel.add(fields[i], g);
        }
        g.gridx = 0; g.gridy = labels.length; g.gridwidth = 2;
        JButton submitBtn = new JButton("Submit Trip Request");
        reqPanel.add(submitBtn, g);
        tabs.addTab("Submit Request", reqPanel);

        main.add(tabs, BorderLayout.CENTER);
        JButton refreshBtn = new JButton("Refresh Trips");
        main.add(refreshBtn, BorderLayout.SOUTH);
        add(main);

        submitBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String dest  = destField.getText().trim();
            String date  = dateField.getText().trim();
            String part  = partField.getText().trim();

            if (title.isEmpty() || dest.isEmpty() || date.isEmpty() || part.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please fill all fields.", "Missing Fields",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            int participants;
            try { participants = Integer.parseInt(part); }
            catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Participants must be a number.", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            String tripID = "TR" + System.currentTimeMillis();
            Trip t = new Trip(tripID, title, dest, date, date, participants, teacher.getUserID());
            t.setStatus(Trip.Status.PENDING);
            DataStore.getInstance().addTrip(t);

            JOptionPane.showMessageDialog(this,
                    "Trip request submitted!\nTrip ID: " + tripID,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            for (JTextField f : fields) f.setText("");
            loadTrips();
        });

        refreshBtn.addActionListener(e -> loadTrips());
        logoutBtn.addActionListener(e -> { dispose(); new LoginScreen().setVisible(true); });
    }

    private void loadTrips() {
        tableModel.setRowCount(0);
        for (Trip t : DataStore.getInstance().getAllTrips()) {
            tableModel.addRow(new Object[]{
                t.getTripID(), t.getTitle(), t.getDestination(),
                t.getDepartureDate(), t.getStatus().name()
            });
        }
    }
}

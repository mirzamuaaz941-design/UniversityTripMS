package gui;

import service.DataStore;
import model.*;
import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {

    public LoginScreen() {
        setTitle("University Trip Management System — Login");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());

        JLabel header = new JLabel("University Trip Management System", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 15));
        header.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        main.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.gridy = 0;
        form.add(new JLabel("Email:"), g);
        g.gridx = 1;
        JTextField emailField = new JTextField(15);
        form.add(emailField, g);

        g.gridx = 0; g.gridy = 1;
        form.add(new JLabel("Password:"), g);
        g.gridx = 1;
        JPasswordField passField = new JPasswordField(15);
        form.add(passField, g);

        g.gridx = 0; g.gridy = 2; g.gridwidth = 2;
        JButton loginBtn = new JButton("Login");
        loginBtn.setPreferredSize(new Dimension(200, 35));
        form.add(loginBtn, g);

        main.add(form, BorderLayout.CENTER);

        JLabel hint = new JLabel("admin@fast.edu/admin  |  ali@fast.edu/123  |  umar@fast.edu/123",
                SwingConstants.CENTER);
        hint.setFont(new Font("SansSerif", Font.ITALIC, 11));
        hint.setForeground(Color.GRAY);
        hint.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        main.add(hint, BorderLayout.SOUTH);

        add(main);

        loginBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String pass  = new String(passField.getPassword()).trim();

            if (email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter both email and password.",
                        "Missing Fields", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User user = DataStore.getInstance().login(email, pass);
            if (user == null) {
                JOptionPane.showMessageDialog(this,
                        "Invalid email or password. Try again.",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            dispose();
            if (user instanceof Admin)
                new AdminDashboard((Admin) user).setVisible(true);
            else if (user instanceof Teacher)
                new TeacherDashboard((Teacher) user).setVisible(true);
            else
                new StudentDashboard((Student) user).setVisible(true);
        });

        getRootPane().setDefaultButton(loginBtn);
    }
}

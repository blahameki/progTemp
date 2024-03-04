package viseletRaktar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
 
public class LoginFrame extends JFrame implements ActionListener {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Container container = getContentPane();
    JLabel userLabel = new JLabel("USERNAME");
    JLabel passwordLabel = new JLabel("PASSWORD");
    JTextField userTextField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("LOGIN");
    JButton resetButton = new JButton("RESET");
    JCheckBox showPassword = new JCheckBox("Show Password");
    JButton createUserButton = new JButton("REGISTRATION");
    JTextField newUserField = new JTextField();
    JPasswordField newPasswordField = new JPasswordField();
    JPasswordField confirmPasswordField = new JPasswordField();
    JButton regConfirmButton = new JButton("CONFIRM");

 
 
    LoginFrame() {
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
 
    }
 
    public void setLayoutManager() {
        container.setLayout(null);
    }
 
    public void setLocationAndSize() {
        userLabel.setBounds(50, 150, 100, 30);
        passwordLabel.setBounds(50, 220, 100, 30);
        userTextField.setBounds(150, 150, 150, 30);
        passwordField.setBounds(150, 220, 150, 30);
        showPassword.setBounds(150, 250, 150, 30);
        loginButton.setBounds(50, 300, 100, 30);
        resetButton.setBounds(200, 300, 100, 30);
        createUserButton.setBounds(150, 350, 150, 30); 
    }
 
    public void addComponentsToContainer() {
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(showPassword);
        container.add(loginButton);
        container.add(resetButton);
        container.add(createUserButton);
    }
 
    public void addActionEvent() {
        loginButton.addActionListener(this);
        resetButton.addActionListener(this);
        showPassword.addActionListener(this);
        createUserButton.addActionListener(this);
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String userText = userTextField.getText();
            char[] pwdChars = passwordField.getPassword();
            String pwdText = new String(pwdChars);

            if (validateLogin(userText, pwdText)) {
                return;
            }
        }
        //Coding Part of RESET button
        if (e.getSource() == resetButton) {
            userTextField.setText("");
            passwordField.setText("");
        }
       //Coding Part of showPassword JCheckBox
        if (e.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
 
 
        }
        if (e.getSource() == createUserButton) {
	        // Új felhasználó létrehozása
        	openRegistrationWindow();
        }
    }
    public boolean validateLogin(String username, String password) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/viselet_raktar", "root", "Beton.Hofi03$")) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String userGroup = resultSet.getString("user_group");
                    if (userGroup.equals("admin") || userGroup.equals("tancos")) {
                        JOptionPane.showMessageDialog(this, "Login Successful");
                        openAdminMenu();
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(this, "Invalid Username or Password");
        return false;
    }
    
    private void openAdminMenu() {
        AdminMenu adminMenu = new AdminMenu(); // Hozz létre egy AdminMenu objektumot
        adminMenu.setVisible(true); // Állítsd láthatóvá az admin menüt
        this.dispose(); // Bezárja a jelenlegi LoginFrame ablakot
    }
    
	boolean usernameExists(String username) {
	    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/viselet_raktar", "root", "Beton.Hofi03$")) {
	        String query = "SELECT * FROM users WHERE username = ?";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setString(1, username);
	            ResultSet resultSet = preparedStatement.executeQuery();
	            return resultSet.next();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return true; // Ha hiba történik, akkor biztonságosság kedvéért igaz értéket adunk vissza
	    }
	}
	
	 private void openRegistrationWindow() {
		 this.dispose();
		 
        JFrame registrationFrame = new JFrame("Register New User");
        registrationFrame.setSize(400, 300);
        registrationFrame.setLayout(null);

        JLabel newUserLabel = new JLabel("Username");
        newUserLabel.setBounds(50, 50, 100, 30);
        JLabel newPasswordLabel = new JLabel("Password");
        newPasswordLabel.setBounds(50, 100, 100, 30);
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setBounds(50, 150, 150, 30);

        newUserField.setBounds(170, 50, 150, 30);
        registrationFrame.add(newUserLabel);
        registrationFrame.add(newUserField);

        newPasswordField.setBounds(170, 100, 150, 30);
        registrationFrame.add(newPasswordLabel);
        registrationFrame.add(newPasswordField);

        confirmPasswordField.setBounds(170, 150, 150, 30);
        registrationFrame.add(confirmPasswordLabel);
        registrationFrame.add(confirmPasswordField);

        regConfirmButton.setBounds(100, 200, 100, 30);
        registrationFrame.add(regConfirmButton);
        
        ActionListener[] listeners = regConfirmButton.getActionListeners();
        if (listeners.length > 0) {
            regConfirmButton.removeActionListener(listeners[0]);
        }

        regConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUser = newUserField.getText();
                String newPassword = new String(newPasswordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (newUser.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(registrationFrame, "Please fill in all fields");
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(registrationFrame, "Passwords do not match");
                    return;
                }

                if (usernameExists(newUser)) {
                    JOptionPane.showMessageDialog(registrationFrame, "Username already exists");
                    return;
                }

                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/viselet_raktar", "root", "Beton.Hofi03$")) {
                    String query = "INSERT INTO users (username, password, user_group) VALUES (?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setString(1, newUser);
                        preparedStatement.setString(2, newPassword);
                        preparedStatement.setString(3, "tancos");
                        preparedStatement.executeUpdate();
                        JOptionPane.showMessageDialog(registrationFrame, "User created successfully");
                        registrationFrame.dispose();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(registrationFrame, "Error creating user");
                }
            }
        });

        registrationFrame.setVisible(true);
    }
}
  
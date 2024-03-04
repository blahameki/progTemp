package viseletRaktar;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class LoginFrameTest {

    private LoginFrame loginFrame;

    @BeforeEach
    public void setUp() {
        loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
    }

    @AfterEach
    public void tearDown() {
        loginFrame.dispose();
    }

    @Test
    public void testLoginWithValidCredentials() {
        // Teszteljük a bejelentkezést helyes felhasználónév és jelszó esetén
        loginFrame.userTextField.setText("admin1");
        loginFrame.passwordField.setText("adminpassword1");
        loginFrame.loginButton.doClick();
        // Helyes bejelentkezés esetén el kell tűnnie a loginFrame-nek
        assertFalse(loginFrame.isVisible());
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        // Teszteljük a bejelentkezést helytelen felhasználónév és/vagy jelszó esetén
        loginFrame.userTextField.setText("invalidUser");
        loginFrame.passwordField.setText("invalidPassword");
        loginFrame.loginButton.doClick();
        // Helytelen bejelentkezés esetén a loginFrame továbbra is éles
        assertTrue(loginFrame.isVisible());
    }
    
    @Test
    public void testLoginWithEmptyCredentials() {
        loginFrame.userTextField.setText("");
        loginFrame.passwordField.setText("");
        loginFrame.loginButton.doClick();
        assertTrue(loginFrame.isVisible());
    }
    
    @Test
    public void testShowPasswordCheckbox() {
        // Ellenőrizzük, hogy az eredetileg beállított jelszó nem látható
        assertFalse(loginFrame.passwordField.getEchoChar() == 0);

        // Kattintsunk a "Show Password" jelölőnégyzetre
        loginFrame.showPassword.doClick();

        // Ellenőrizzük, hogy a jelölőnégyzet kiválasztása után a jelszó láthatóvá válik
        assertTrue(loginFrame.passwordField.getEchoChar() == 0);
    }

    @Test
    public void testResetButton() {
        // Teszteljük a Reset gomb működését
        loginFrame.userTextField.setText("username");
        loginFrame.passwordField.setText("password");
        loginFrame.resetButton.doClick();
        // A Reset gombnak törölnie kell a felhasználónév és jelszó mezőket
        assertEquals("", loginFrame.userTextField.getText());
        assertEquals("", String.valueOf(loginFrame.passwordField.getPassword()));
    }

    @Test
    public void testNewUserRegistration() {
        loginFrame.createUserButton.doClick();
        assertNotNull(loginFrame.newUserField);
        assertNotNull(loginFrame.newPasswordField);
        assertNotNull(loginFrame.confirmPasswordField);

        loginFrame.newUserField.setText("newUser");
        loginFrame.newPasswordField.setText("newPassword");
        loginFrame.confirmPasswordField.setText("newPassword");

        loginFrame.regConfirmButton.doClick();
        assertTrue(loginFrame.regConfirmButton.isVisible());
    }
    
    @Test
    public void testEmptyUserRegistration() {
        loginFrame.createUserButton.doClick();
        assertNotNull(loginFrame.newUserField);
        assertNotNull(loginFrame.newPasswordField);
        assertNotNull(loginFrame.confirmPasswordField);

        loginFrame.newUserField.setText("");
        loginFrame.newPasswordField.setText("");
        loginFrame.confirmPasswordField.setText("");

        loginFrame.regConfirmButton.doClick();
        assertTrue(loginFrame.regConfirmButton.isVisible());
    }
    
    @Test
    public void testAdminMenuAfterSuccessfulLogin() {
        loginFrame.userTextField.setText("admin1");
        loginFrame.passwordField.setText("adminpassword1");
        loginFrame.loginButton.doClick();
        // Ellenőrizzük, hogy az loginFrame menü látható-e még a sikeres bejelentkezés után
        assertFalse(loginFrame.isVisible());
    }
}
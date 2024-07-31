package org.example.demodesktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.demodesktop.config.SessionManager;
import org.example.demodesktop.model.User;
import org.example.demodesktop.repository.UserRepository;
import org.example.demodesktop.view.ProductListPage;
import org.example.demodesktop.view.RegisterPage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.logging.Logger;

import static org.example.demodesktop.utils.UIUtils.showAlert;

public class LoginController {

    private static final Logger log = Logger.getLogger(LoginController.class.getName());
    private final UserRepository userRepository = new UserRepository();

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;

    @FXML
    private void handleLogin() {
        // print to log
        log.info("handle login clicked");
        // logic to handle login
        try {
            // init all fields
            String username = usernameField.getText();
            String password = passwordField.getText();
            // validate all fields
            if (username.isEmpty() && password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Bad Request", "Username and password cannot be empty");
                return;
            }
            // verify user
            if (userRepository.findByUsername(username) == null) {
                showAlert(Alert.AlertType.ERROR, "Bad Request", "username doesn't exist");
                return;
            }
            // if username exist save session & go to product-list-page
            if (isVerified(username, password)) {
                SessionManager.setLoggedIn(true);
                new ProductListPage().start((Stage) loginButton.getScene().getWindow());
                showAlert(Alert.AlertType.CONFIRMATION, "Login successfully", "Hi " + username);
            } else {
                // else -> show alert error
                showAlert(Alert.AlertType.ERROR, "Login failed", "username & password does not match");
            }
        } catch (Exception exception) {
            // print log on console
            log.info("Error occurred: " + exception);
            showAlert(Alert.AlertType.ERROR, "Internal server error", "Please contact the administrator");
        }
    }

    private boolean isVerified(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        return BCrypt.checkpw(password, user.getPassword());
    }

    @FXML
    private void handleRegister() throws IOException {
        // print to log
        log.info("handle register clicked");
        // go to register page
        new RegisterPage().start((Stage) registerButton.getScene().getWindow());
    }


}
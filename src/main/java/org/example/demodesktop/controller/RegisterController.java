package org.example.demodesktop.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.demodesktop.model.User;
import org.example.demodesktop.repository.UserRepository;
import org.example.demodesktop.view.LoginPage;
import org.mindrot.jbcrypt.BCrypt;


import java.io.IOException;
import java.util.logging.Logger;

import static org.example.demodesktop.utils.UIUtils.showAlert;

public class RegisterController {

    private static final Logger log = Logger.getLogger(RegisterController.class.getName());
    private final UserRepository userRepository = new UserRepository();

    @FXML
    private TextField usernameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button registerButton;

    @FXML
    private void handleRegister() {
        // print to log
        log.info("register button clicked");
        // logic to handle register
        try {
            // init all field
            String username = usernameField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String password = passwordField.getText();
            // validate input user, if not valid show error alert
            if(username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Bad request", "Please fill all fields");
                return;
            }
            // validate user in table
            if (userRepository.findByUsername(username) != null) {
                showAlert(Alert.AlertType.ERROR, "Username already exists", "Please choose another username");
                return;
            }

            // create user object
            User user = new User();
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            user.setPassword(encryptedPassword);
            // save data into table
            userRepository.save(user);
            // back to login page
            log. info("User saved");
            showAlert(Alert.AlertType.CONFIRMATION, "Register Successfully","You now have access this app");
            new LoginPage().start((Stage) registerButton.getScene().getWindow());
        } catch (Exception exception) {
            // print the error in console
            log.warning("Error occurred: " + exception);
            showAlert(Alert.AlertType.ERROR, "Internal server error", "Please contact the administrator");
        }
    }

    @FXML
    private void handleBack() throws IOException {
        // print to log
        log.info("back button clicked");
        // go to previous page
        new LoginPage().start((Stage) registerButton.getScene().getWindow());
    }
}
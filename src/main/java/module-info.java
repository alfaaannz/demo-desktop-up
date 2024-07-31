module org.example.demodesktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.sql;
    requires jbcrypt;


    opens org.example.demodesktop to javafx.fxml;
    exports org.example.demodesktop;
    exports org.example.demodesktop.controller;
    exports org.example.demodesktop.repository;
    exports org.example.demodesktop.model;
    opens org.example.demodesktop.controller to javafx.fxml;
}
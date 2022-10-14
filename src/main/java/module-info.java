module hr.algebra.codenames {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens hr.algebra.codenames to javafx.fxml;
    exports hr.algebra.codenames;
    exports hr.algebra.codenames.controller;
    opens hr.algebra.codenames.controller to javafx.fxml;
}
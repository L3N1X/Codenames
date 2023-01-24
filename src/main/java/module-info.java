module hr.algebra.codenames {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.naming;
    requires java.rmi;
    requires simple.xml;


    opens hr.algebra.codenames to javafx.fxml;
    exports hr.algebra.codenames;
    exports hr.algebra.codenames.controller;
    exports hr.algebra.codenames.model;
    opens hr.algebra.codenames.controller to javafx.fxml;
    opens hr.algebra.codenames.xml.model to simple.xml;
    opens hr.algebra.codenames.model to simple.xml;
    exports hr.algebra.codenames.rmi.server to java.rmi;
}
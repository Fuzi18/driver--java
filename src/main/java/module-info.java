module hu.petrik.driver {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens hu.petrik.driver to javafx.fxml;
    exports hu.petrik.driver;
}
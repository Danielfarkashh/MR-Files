module com.example.mr {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.example.mr to javafx.fxml;
    exports com.example.mr;
}
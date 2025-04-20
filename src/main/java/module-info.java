module com.example.termproject2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.termproject2 to javafx.fxml;
    exports com.example.termproject2;
}
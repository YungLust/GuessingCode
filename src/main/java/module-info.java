module org.example.passwordguessing {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.passwordguessing to javafx.fxml;
    exports org.example.passwordguessing;
}
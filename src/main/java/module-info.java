module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens company.freightroutingnn to javafx.fxml;
    exports company.freightroutingnn;
}
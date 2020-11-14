module com.xledbd {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.xledbd to javafx.fxml;
    exports com.xledbd;
}
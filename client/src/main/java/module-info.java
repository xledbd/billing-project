module com.xledbd {
    requires javafx.controls;
    requires javafx.fxml;
    requires gson;

    opens com.xledbd to javafx.fxml, gson;
    exports com.xledbd;
}
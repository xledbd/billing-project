module com.xledbd {
    requires javafx.controls;
    requires javafx.fxml;
    requires gson;
    requires java.sql;

    opens com.xledbd to javafx.fxml, gson;
    opens com.xledbd.entity to gson;
    exports com.xledbd;
    exports com.xledbd.entity;
}
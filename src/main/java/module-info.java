module com.example.graphalgogui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens gui to javafx.fxml;
    exports gui;
}
module com.example.graphalgogui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.wiaczek.socki.graphalgogui to javafx.fxml;
    exports com.wiaczek.socki.graphalgogui;
}
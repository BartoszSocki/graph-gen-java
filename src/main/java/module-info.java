module com.example.graphalgogui {
    requires javafx.controls;
    requires javafx.fxml;

    // nie wiem co napisałem, ale dzięki temu działa
    exports graph.control;
    opens graph.control to javafx.fxml;

    opens com.wiaczek.socki.graphalgogui to javafx.fxml;
    exports com.wiaczek.socki.graphalgogui;
}
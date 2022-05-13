module main {
    requires javafx.controls;
    requires javafx.fxml;

    // nie wiem co napisałem, ale dzięki temu działa
    exports graph.control;
    opens graph.control to javafx.fxml;

    exports graph;

    opens main to javafx.fxml;
    exports main;
}
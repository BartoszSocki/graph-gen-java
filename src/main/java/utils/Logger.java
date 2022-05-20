package utils;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public final class Logger {

    private static TextArea errorField;

    public static void setTextField(TextArea e)
    {
        errorField = e;
    }


    public static void displayError(String title, String msg)
    {
        errorField.setText(String.format("%s: %s", title, msg));
    }
    public static void clear()
    {
        errorField.setText("");
    }
}

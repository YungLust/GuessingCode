package org.example.passwordguessing;

import javafx.scene.control.Alert;
import java.text.DecimalFormat;

public class UIUtils {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00000");

    /**
     * opens error modal with title and message
     *
     * @param title title of the window that is displayed on window header
     * @param message the actual message that is displayed inside a window
     */
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * converts floating point number to String
     *
     * @param value floating point number
     * @return String that represents the floating point number
     */
    public static String formatDecimal(double value) {
        return DECIMAL_FORMAT.format(value);
    }
}

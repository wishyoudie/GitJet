package gitjet;

import javafx.scene.control.Control;
import javafx.stage.Stage;

public class Utils {

    /**
     * Kill window of control element.
     * @param ctrl Control element.
     */
    public static void killWindow(Control ctrl) {
        Stage stage = (Stage) ctrl.getScene().getWindow();
        stage.close();
    }
}

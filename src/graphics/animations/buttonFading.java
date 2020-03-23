package graphics.animations;

import com.sun.javafx.scene.control.skin.ButtonSkin;
import javafx.animation.FadeTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class buttonFading extends ButtonSkin {
    public buttonFading(Button control) {
        super(control);

        final FadeTransition fadeIn = new FadeTransition(Duration.millis(100));
        fadeIn.setNode(control);
        fadeIn.setToValue(1);
        control.setOnMouseEntered(event -> fadeIn.playFromStart());

        final FadeTransition fadeOut = new FadeTransition(Duration.millis(100));
        fadeOut.setNode(control);
        fadeOut.setToValue(0.9);
        control.setOnMouseExited(event -> fadeOut.playFromStart());

        control.setOpacity(0.9);
    }
}

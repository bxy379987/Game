package comp1110.ass2;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;

public class MusicControllerEntity {
    private Group mediaGroup;
    private MediaPlayer mediaPlayer;
    private Slider slider;
    private ImageView button;
    private boolean isVisible;
    public MusicControllerEntity(double x, double y, MediaPlayer mediaPlayer, Group root) {
        // init
        this.isVisible = false;
        this.mediaPlayer = mediaPlayer;
        this.mediaGroup = new Group();
        // button
        this.button = new ImageView(new Image("comp1110/ass2/assets/voice.png",
                30, 30, false, false));
        this.button.setLayoutX(0);
        this.button.setLayoutY(0);
        this.mediaGroup.getChildren().add(this.button);
        this.button.setOnMousePressed(event -> {
            this.isVisible = !this.isVisible;
            this.slider.setVisible(this.isVisible);
        });

        // slider
        this.slider = new Slider(0, 1, 0.5);
        this.slider.setVisible(this.isVisible);
        mediaPlayer.volumeProperty().bind(this.slider.valueProperty());
        this.slider.setStyle(
                "-fx-control-inner-background: #b2823a; " +
                "-fx-background-color: #2a2c2c; " +
                "-fx-background-radius: 0; " +
                "-fx-border-width: 0; " +
                "-fx-padding: 0;"
        );
        this.slider.setLayoutX(40);
        this.slider.setLayoutY(7);
        mediaGroup.getChildren().add(this.slider);
        // media group
        this.mediaGroup.setLayoutX(x);
        this.mediaGroup.setLayoutY(y);
        root.getChildren().add(mediaGroup);

    }
}
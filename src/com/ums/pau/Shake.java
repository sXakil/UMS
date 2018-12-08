package com.ums.pau;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class Shake {
    private static Timeline timeline;
    private static Interpolator EASE = Interpolator.SPLINE(0.25D, 0.1D, 0.25D, 1.0D);

    public Shake(Node node) {
        timeline = new Timeline(
                new KeyFrame(Duration.millis(0.0D),
                        new KeyValue(node.translateXProperty(), 0, EASE)),
                new KeyFrame(Duration.millis(50.0D),
                        new KeyValue(node.translateXProperty(), -5, EASE)),
                new KeyFrame(Duration.millis(100.0D),
                        new KeyValue(node.translateXProperty(), 5, EASE)),
                new KeyFrame(Duration.millis(150.0D),
                        new KeyValue(node.translateXProperty(), -5, EASE)),
                new KeyFrame(Duration.millis(200.0D),
                        new KeyValue(node.translateXProperty(), 0, EASE)));
    }

    public void play() {
        timeline.play();
    }
}


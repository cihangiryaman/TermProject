package com.example.termproject2;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public abstract class Enemy
{
    private Pane _pane;
    private int _health = 10;
    private double _speed;
    private double _positionX;
    private double _positionY;
    private ImageView _image = new ImageView(new Image("warrior.jpg"));

    Enemy(Pane pane, int initialHealth, double initialSpeed)
    {
        _pane = pane;
        _speed = initialSpeed;
        _health = initialHealth;
        _image.setFitHeight(20);
        _image.setFitWidth(20);
    }

    Enemy(Pane pane, String imageName, int initialHealth, double initialSpeed)
    {
        _pane = pane;
        _speed = initialSpeed;
        _health = initialHealth;
        _image = new ImageView(new Image(imageName));
        _image.setFitHeight(20);
        _image.setFitWidth(20);
    }

    public void walk(int level, Pane pane) throws Exception {
        int[][] coordinates1 = {{2, 0}, {2, 1}, {2, 2}, {2, 3}, {3, 3}, {4, 3}, {5, 3}, {5, 4}, {5, 5}, {5, 6}, {5, 7}, {5, 8}, {5, 9}};
        int[][] coordinates2 = {{0, 2}, {1, 2}, {2, 2}, {3, 2}, {3, 3}, {3, 4}, {3, 5}, {2, 5}, {1, 5}, {1, 6}, {1, 7}, {2, 7}, {3, 7}, {4, 7}, {5, 7}, {6, 7}, {7, 7}, {7, 8}, {7, 9}};
        int[][] coordinates3 = {{5, 0}, {5, 1}, {5, 2}, {4, 2}, {3, 2}, {3, 3}, {3, 4}, {4, 4}, {5, 4}, {6, 4}, {7, 4}, {7, 5}, {7, 6}, {7, 7}, {6, 7}, {5, 7}, {4, 7}, {4, 8}, {4, 9}};
        int[][] coordinates4 = {{2, 0}, {2, 1}, {2, 2}, {2, 3}, {3, 3}, {4, 3}, {5, 3}, {6, 3}, {7, 3}, {8, 3}, {8, 4}, {8, 5}, {8, 6}, {7, 6}, {6, 6}, {5, 6}, {4, 6}, {4, 7}, {4, 8}, {4, 9}, {5, 9}, {6, 9}, {7, 9}, {8, 9}, {9, 9}, {10, 9}, {10, 8}, {10, 7}, {10, 6}, {11, 6}, {12, 6}, {12, 7}, {12, 8}, {12, 9}, {12, 10}, {12, 11}, {12, 12}, {11, 12}, {10, 12}, {9, 12}, {8, 12}, {8, 13}, {8, 14}};
        int[][] coordinates5 = {{0, 3}, {1, 3}, {2, 3}, {3, 3}, {3, 2}, {3, 1}, {4, 1}, {5, 1}, {6, 1}, {6, 2}, {6, 3}, {6, 4}, {6, 5}, {6, 6}, {7, 6}, {8, 6}, {8, 5}, {8, 4}, {9, 4}, {10, 4}, {10, 5}, {10, 6}, {10, 7}, {10, 8}, {10, 9}, {9, 9}, {8, 9}, {7, 9}, {6, 9}, {5, 9}, {4, 9}, {3, 9}, {3, 10}, {3, 11}, {3, 12}, {4, 12}, {5, 12}, {6, 12}, {7, 12}, {8, 12}, {9, 12}, {10, 12}, {11, 12}, {12, 12}, {12, 13}, {12, 14}};

        Path path;

        double durationPerTile = _speed;//0.2
        double tileSize = 40;
        Circle circle = new Circle(10, Color.RED);

        if (level == 1) {
            //Initializing circle coordinates
            circle.setCenterX(coordinates1[0][1] *tileSize + tileSize/2 - 10);
            circle.setCenterY(coordinates1[0][0]* tileSize + tileSize/2);
            pane.getChildren().add(circle);

            path = new Path();
            //Initial path coordinates
            path.getElements().add(new MoveTo(coordinates1[0][1] *tileSize + tileSize/2.0, coordinates1[0][0]*tileSize + tileSize/2.0 + 80));

            //Using this for multiple animations
            SequentialTransition st = new SequentialTransition();
            for (int i = 0; i < coordinates1.length; i++) {
                //Drawing the line that following indicated coordinates
                path.getElements().add(new LineTo(coordinates1[i][1] * tileSize + tileSize/2.0, coordinates1[i][0] * tileSize + tileSize/2.0+80 ));
            }


            double totalDuration = coordinates1.length * durationPerTile;
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5)); //Delay
            PathTransition pt = new PathTransition();

            pt.setPath(path);
            pt.setNode(circle);
            pt.jumpTo(Duration.ZERO);
            pt.setDuration(javafx.util.Duration.seconds(totalDuration));
            pt.setInterpolator(Interpolator.LINEAR);

            st.getChildren().addAll(pauseTransition, pt);
            st.setCycleCount(1);
            st.play();
        } else if (level == 2) {
            //Initializing circle coordinates
            circle.setCenterX(coordinates2[0][1] * tileSize + tileSize/2 -90);
            circle.setCenterY(coordinates2[0][0]* tileSize + tileSize/2 + 80);
            pane.getChildren().add(circle);

            path = new Path();
            //Initial path coordinates
            path.getElements().add(new MoveTo(coordinates2[0][1] *tileSize + tileSize/2.0, coordinates2[0][0]*tileSize + tileSize/2.0 + 80));

            //Using this for multiple animations
            SequentialTransition st = new SequentialTransition();
            for (int i = 0; i < coordinates2.length; i++) {
                //Drawing the line that following indicated coordinates
                path.getElements().add(new LineTo(coordinates2[i][1] * tileSize + tileSize/2.0, coordinates2[i][0] * tileSize + tileSize/2.0+80 ));
            }


            double totalDuration = coordinates2.length * durationPerTile;
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5)); //Delay
            PathTransition pt = new PathTransition();

            pt.setPath(path);
            pt.setNode(circle);
            pt.jumpTo(Duration.ZERO);
            pt.setDuration(javafx.util.Duration.seconds(totalDuration));
            pt.setInterpolator(Interpolator.LINEAR);

            st.getChildren().addAll(pauseTransition, pt);
            st.setCycleCount(1);
            st.play();
        } else if (level == 3) {
            //Initializing circle coordinates
            circle.setCenterX(coordinates3[0][1]* tileSize + tileSize/2 + 120);
            circle.setCenterY(coordinates3[0][0]*tileSize + tileSize/2 - 200);
            pane.getChildren().add(circle);

            path = new Path();
            //Initial path coordinates
            path.getElements().add(new MoveTo(coordinates3[0][1] *tileSize + tileSize/2.0 + 130, coordinates3[0][0]*tileSize + tileSize/2.0));

            //Using this for multiple animations
            SequentialTransition st = new SequentialTransition();
            for (int i = 0; i < coordinates3.length; i++) {
                //Drawing the line that following indicated coordinates
                path.getElements().add(new LineTo(coordinates3[i][1] * tileSize + tileSize/2.0 + 130, coordinates3[i][0] * tileSize + tileSize/2.0));
            }


            double totalDuration = coordinates3.length * durationPerTile;
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5)); //Delay
            PathTransition pt = new PathTransition();

            pt.setPath(path);
            pt.setNode(circle);
            pt.jumpTo(Duration.ZERO);
            pt.setDuration(javafx.util.Duration.seconds(totalDuration));
            pt.setInterpolator(Interpolator.LINEAR);

            st.getChildren().addAll(pauseTransition, pt);
            st.setCycleCount(1);
            st.play();
        } else if (level == 4) {
            //Initializing circle coordinates
            circle.setCenterX(coordinates4[0][1]* tileSize + tileSize/2 + 120);
            circle.setCenterY(coordinates4[0][0]*tileSize + tileSize/2 - 80);
            pane.getChildren().add(circle);

            path = new Path();
            //Initializing path coordinates
            path.getElements().add(new MoveTo(coordinates4[0][1] *tileSize + tileSize/2.0 + 130, coordinates4[0][0]*tileSize + tileSize/2.0));

            //Using this for multiple animations
            SequentialTransition st = new SequentialTransition();
            for (int i = 0; i < coordinates4.length; i++) {
                //Drawing the line that following indicated coordinates
                path.getElements().add(new LineTo(coordinates4[i][1] * tileSize + tileSize/2.0 + 130, coordinates4[i][0] * tileSize + tileSize/2.0));
            }


            double totalDuration = coordinates4.length * durationPerTile;
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5)); //Delay
            PathTransition pt = new PathTransition();

            pt.setPath(path);
            pt.setNode(circle);
            pt.jumpTo(Duration.ZERO);
            pt.setDuration(javafx.util.Duration.seconds(totalDuration));
            pt.setInterpolator(Interpolator.LINEAR);

            st.getChildren().addAll(pauseTransition, pt);
            st.setCycleCount(1);
            st.play();
        } else if (level == 5) {

            //Initializing circle coordinates
            circle.setCenterX(coordinates5[0][1]* tileSize + tileSize/2);
            circle.setCenterY(coordinates5[0][0]*tileSize + tileSize/2);
            pane.getChildren().add(circle);

            path = new Path();
            //Initial path coordinates
            path.getElements().add(new MoveTo(coordinates5[0][1] *tileSize + tileSize/2.0 + 130, coordinates5[0][0]*tileSize + tileSize/2.0));

            //Using this for multiple animations
            SequentialTransition st = new SequentialTransition();
            for (int i = 0; i < coordinates5.length; i++) {
                //Drawing the line that following indicated coordinates
                path.getElements().add(new LineTo(coordinates5[i][1] * tileSize + tileSize/2.0 + 130, coordinates5[i][0] * tileSize + tileSize/2.0));
            }

            double totalDuration = coordinates5.length * durationPerTile;
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5)); //Delay
            PathTransition pt = new PathTransition();

            pt.setPath(path);
            pt.setNode(circle);
            pt.jumpTo(Duration.ZERO);
            pt.setDuration(javafx.util.Duration.seconds(totalDuration));
            pt.setInterpolator(Interpolator.LINEAR);

            st.getChildren().addAll(pauseTransition, pt);
            st.setCycleCount(1);
            st.play();
        }
        else {
            throw new Exception("Invalid level number.");
        }
    }

    public void explode() {
        // Shockwave
        Circle shockwave = new Circle(_positionX, _positionY, 1);
        shockwave.setFill(Color.DARKORANGE);
        shockwave.setStroke(Color.DARKORANGE);
        shockwave.setStrokeWidth(1);

        // Particles
        int particleCount = 20;
        Circle[] particles = new Circle[particleCount];
        for (int i = 0; i < particleCount; i++) {
            particles[i] = new Circle(2.25, randomColor());
            particles[i].setCenterX(_positionX);
            particles[i].setCenterY(_positionY);
            particles[i].setOpacity(0);
        }
        _pane.getChildren().addAll(shockwave);
        _pane.getChildren().addAll(particles);

        // Shockwave animation
        ScaleTransition shockwaveScale = new ScaleTransition(Duration.millis(100), shockwave);
        shockwaveScale.setFromX(1);
        shockwaveScale.setFromY(1);
        shockwaveScale.setToX(30);
        shockwaveScale.setToY(30);

        FadeTransition shockwaveFade = new FadeTransition(Duration.millis(150), shockwave);
        shockwaveFade.setFromValue(0.8);
        shockwaveFade.setToValue(0);

        ParallelTransition shockwaveAnim = new ParallelTransition(shockwaveScale, shockwaveFade);

        // Particle animations
        ParallelTransition particleAnim = new ParallelTransition();
        for (int i = 0; i < particleCount; i++) {
            Circle p1 = particles[i];

            FadeTransition ft = new FadeTransition(Duration.millis(10), p1);
            ft.setFromValue(0);
            ft.setToValue(1);

            TranslateTransition tt = new TranslateTransition(Duration.millis(500), p1);
            tt.setByX(Math.random() * 120 - 60);
            tt.setByY(Math.random() * 120 - 60);

            FadeTransition fadeOut = new FadeTransition(Duration.millis(50), p1);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setDelay(Duration.millis(30));

            SequentialTransition st = new SequentialTransition(ft, tt, fadeOut);
            particleAnim.getChildren().add(st);
        }

        // Combining all animations
        SequentialTransition explosion = new SequentialTransition(new ParallelTransition(shockwaveAnim), particleAnim);

        explosion.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                _pane.getChildren().removeAll(shockwave);
                _pane.getChildren().removeAll(particles);

            }
        });
        explosion.play();
    }

    public Color randomColor() {
        double r = Math.random();
        if (r < 0.3) return Color.ORANGE;
        else if (r < 0.6) return Color.RED;
        else return Color.YELLOW;
    }

    public double getSpeed() {
        return _speed;
    }

    public void setSpeed(int speed) {
        this._speed = speed;
    }

    public int getHealth() {
        return _health;
    }

    public void setHealth(int health) {
        this._health = health;
    }

    public double getPositionX()
    {
        return _positionX;

    }
    public double getPositionY()
    {
        return _positionY;
    }

    public void setPosition(double x, double y) {
        this._positionX = x;
        this._positionY = y;
    }
}

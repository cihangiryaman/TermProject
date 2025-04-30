package com.example.termproject2;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;

public abstract class Enemy
{
    private Pane _pane;
    private int _health;
    private double _speed;
    private double _positionX;
    private double _positionY;
    private boolean _isExploding = false;
    double tileSize = 40;
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

    PathTransition pt;
    SequentialTransition st;


    Circle circle = new Circle(10,Color.RED);

    public void walk(int level) throws Exception {

        TextDecoder textDecoder = new TextDecoder(new File("level" + level + ".txt"));
        ArrayList<Cell> coordinates = textDecoder.getGrayCells();

        Path path;

        double durationPerTile = _speed;//0.2


        if (level == 1) {
            //Initializing circle coordinates
            circle.setCenterX(coordinates.get(0).x*tileSize + tileSize/2 - 90);
            circle.setCenterY(coordinates.get(0).y* tileSize + tileSize/2);
            _pane.getChildren().add(circle);

            path = new Path();
            //Initial path coordinates
            path.getElements().add(new MoveTo(coordinates.get(0).y *tileSize + tileSize/2.0, coordinates.get(0).x*tileSize + tileSize/2.0));

            pt = new PathTransition();
            //Using this for multiple animations
            st = new SequentialTransition();
            for (int i = 0; i < coordinates.size(); i++) {
                //Drawing the line that following indicated coordinates
                path.getElements().add(new LineTo(coordinates.get(i).y * tileSize + tileSize/2.0, coordinates.get(i).x * tileSize + tileSize/2.0));
            }


            double totalDuration = coordinates.size() * durationPerTile;
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5)); //Delay


            pt.setPath(path);
            pt.setNode(circle);
            pt.jumpTo(Duration.ZERO);
            pt.setDuration(javafx.util.Duration.seconds(totalDuration));
            pt.setInterpolator(Interpolator.LINEAR);

            st.getChildren().addAll(pauseTransition, pt);
            st.setCycleCount(1);
            st.play();


        }
        else if (level == 2) {
            //Initializing circle coordinates
            circle.setCenterX(coordinates.get(0).x * tileSize + tileSize/2 - 10);
            circle.setCenterY(coordinates.get(0).y* tileSize + tileSize/2 - 80);
            _pane.getChildren().add(circle);

            path = new Path();
            //Initial path coordinates
            path.getElements().add(new MoveTo(coordinates.get(0).y *tileSize + tileSize/2.0, coordinates.get(0).x*tileSize + tileSize/2.0));

            pt = new PathTransition();
            //Using this for multiple animations
            st = new SequentialTransition();
            for (int i = 0; i < coordinates.size(); i++) {
                //Drawing the line that following indicated coordinates
                path.getElements().add(new LineTo(coordinates.get(i).y * tileSize + tileSize/2.0, coordinates.get(i).x * tileSize + tileSize/2.0));
            }


            double totalDuration = coordinates.size() * durationPerTile;
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5)); //Delay


            pt.setPath(path);
            pt.setNode(circle);
            pt.jumpTo(Duration.ZERO);
            pt.setDuration(javafx.util.Duration.seconds(totalDuration));
            pt.setInterpolator(Interpolator.LINEAR);

            st.getChildren().addAll(pauseTransition, pt);
            st.setCycleCount(1);
            st.play();


        }
        else if (level == 3) {
            //Initializing circle coordinates
            circle.setCenterX(coordinates.get(0).x* tileSize + tileSize/2 - 210);
            circle.setCenterY(coordinates.get(0).y*tileSize + tileSize/2 );
            _pane.getChildren().add(circle);

            path = new Path();
            //Initial path coordinates
            path.getElements().add(new MoveTo(coordinates.get(0).y *tileSize + tileSize/2.0, coordinates.get(0).x*tileSize + tileSize/2.0));

            pt = new PathTransition();
            //Using this for multiple animations
            st = new SequentialTransition();
            for (int i = 0; i < coordinates.size(); i++) {
                //Drawing the line that following indicated coordinates
                path.getElements().add(new LineTo(coordinates.get(i).y * tileSize + tileSize/2.0, coordinates.get(i).x * tileSize + tileSize/2.0));
            }


            double totalDuration = coordinates.size() * durationPerTile;
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5)); //Delay


            pt.setPath(path);
            pt.setNode(circle);
            pt.jumpTo(Duration.ZERO);
            pt.setDuration(javafx.util.Duration.seconds(totalDuration));
            pt.setInterpolator(Interpolator.LINEAR);

            st.getChildren().addAll(pauseTransition, pt);
            st.setCycleCount(1);
            st.play();


        }
        else if (level == 4) {
            //Initializing circle coordinates
            circle.setCenterX(coordinates.get(0).x* tileSize + tileSize/2 - 90);
            circle.setCenterY(coordinates.get(0).y*tileSize + tileSize/2);
            _pane.getChildren().add(circle);

            path = new Path();
            //Initializing path coordinates
            path.getElements().add(new MoveTo(coordinates.get(0).y *tileSize + tileSize/2.0, coordinates.get(0).x*tileSize + tileSize/2.0));

            pt = new PathTransition();
            //Using this for multiple animations
            st = new SequentialTransition();
            for (int i = 0; i < coordinates.size(); i++) {
                //Drawing the line that following indicated coordinates
                path.getElements().add(new LineTo(coordinates.get(i).y * tileSize + tileSize/2.0, coordinates.get(i).x * tileSize + tileSize/2.0));
            }


            double totalDuration = coordinates.size() * durationPerTile;
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5)); //Delay


            pt.setPath(path);
            pt.setNode(circle);
            pt.jumpTo(Duration.ZERO);
            pt.setDuration(javafx.util.Duration.seconds(totalDuration));
            pt.setInterpolator(Interpolator.LINEAR);

            st.getChildren().addAll(pauseTransition, pt);
            st.setCycleCount(1);
            st.play();


        }
        else if (level == 5) {

            //Initializing circle coordinates
            circle.setCenterX(coordinates.get(0).x* tileSize + tileSize/2 - 10);
            circle.setCenterY(coordinates.get(0).y*tileSize + tileSize/2 - 120);

            _pane.getChildren().add(circle);

            path = new Path();
            //Initial path coordinates
            path.getElements().add(new MoveTo(coordinates.get(0).y *tileSize + tileSize/2.0, coordinates.get(0).x*tileSize + tileSize/2.0));

            pt = new PathTransition();
            //Using this for multiple animations
            st = new SequentialTransition();
            for (int i = 0; i < coordinates.size(); i++) {
                //Drawing the line that following indicated coordinates
                path.getElements().add(new LineTo(coordinates.get(i).y * tileSize + tileSize/2.0, coordinates.get(i).x * tileSize + tileSize/2.0));

            }

            double totalDuration = coordinates.size() * durationPerTile;
            PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.5)); //Delay


            pt.setPath(path);
            pt.setNode(circle);
            pt.jumpTo(Duration.ZERO);
            pt.setDuration(javafx.util.Duration.seconds(totalDuration));
            pt.setInterpolator(Interpolator.LINEAR);

            st.getChildren().addAll(pauseTransition, pt);
            st.setCycleCount(1);
            st.play();

            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    setPosition(circle.getTranslateX() + circle.getCenterX(), circle.getTranslateY() + circle.getCenterY());

                    System.out.println(_health);
                }
            };
            timer.start();



        }
        else {
            throw new Exception("Invalid level number.");
        }
    }

    public void explode() {
        double explodeX = circle.getTranslateX() + circle.getCenterX();
        double explodeY = circle.getTranslateY() + circle.getCenterY();

        // Shockwave
        Circle shockwave = new Circle(explodeX,explodeY,1);
        shockwave.setFill(Color.DARKORANGE);
        shockwave.setStroke(Color.DARKORANGE);
        shockwave.setStrokeWidth(1);

        // Particles
        int particleCount = 20;
        Circle[] particles = new Circle[particleCount];
        for (int i = 0; i < particleCount; i++) {
            particles[i] = new Circle(explodeX,explodeY,2.25, randomColor());
            particles[i].setOpacity(0);
        }

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

        explosion.play();
        _pane.getChildren().add(shockwave);
        _pane.getChildren().addAll(particles);
        explosion.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                _pane.getChildren().remove(shockwave);
                _pane.getChildren().removeAll(particles);
                _pane.getChildren().remove(circle);
                Map.activeEnemies.remove(Enemy.this);

            }
        });
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

    public void setHealth(int amount) {
        this._health += amount;
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
    public boolean isExploding(){
        return _isExploding;
    }
    public void setExploding(boolean exploding){
        this._isExploding = exploding;
    }
    public void stopMovement(){
        if(st != null){
            st.stop();
        }
    }
    public Circle getCircle() {
        return circle;
    }
}

package com.example.termproject2;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
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
        double durationPerTile = 1/_speed;

        if (level == 1) {
            circle.setCenterX(coordinates.getFirst().x * tileSize + tileSize/2 - 90);
            circle.setCenterY(coordinates.getFirst().y * tileSize + tileSize/2);
        }
        else if (level == 2) {
            circle.setCenterX(coordinates.getFirst().x * tileSize + tileSize/2 - 10);
            circle.setCenterY(coordinates.getFirst().y * tileSize + tileSize/2 - 80);
        }
        else if (level == 3) {
            circle.setCenterX(coordinates.getFirst().x * tileSize + tileSize/2 - 210);
            circle.setCenterY(coordinates.getFirst().y * tileSize + tileSize/2);
        }
        else if (level == 4) {
            circle.setCenterX(coordinates.getFirst().x * tileSize + tileSize/2 - 90);
            circle.setCenterY(coordinates.getFirst().y * tileSize + tileSize/2);
        }
        else if (level == 5) {
            circle.setCenterX(coordinates.getFirst().x * tileSize + tileSize/2 - 10);
            circle.setCenterY(coordinates.getFirst().y * tileSize + tileSize/2 - 120);
        }
        else {
            throw new Exception("Invalid level number.");
        }

        _pane.getChildren().add(circle);

        path = new Path();
        //Initial path coordinates
        path.getElements().add(new MoveTo(coordinates.getFirst().y *tileSize + tileSize/2.0, coordinates.getFirst().x*tileSize + tileSize/2.0));

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
            }
        };
        timer.start();
    }

    public void explode() {
        // 1. Patlama noktasını al (tüm transform'ları hesaba katar)
        double explodeX = circle.getTranslateX() + circle.getCenterX();
        double explodeY = circle.getTranslateY() + circle.getCenterY();

        // 2. Shockwave
        Circle shockwave = new Circle(0, 0, 1);
        shockwave.setTranslateX(explodeX);
        shockwave.setTranslateY(explodeY);
        shockwave.setFill(Color.DARKORANGE);
        shockwave.setStroke(Color.DARKORANGE);
        shockwave.setStrokeWidth(1);

        // 3. Partiküller
        int particleCount = 20;
        Circle[] particles = new Circle[particleCount];
        for (int i = 0; i < particleCount; i++) {
            Circle p = new Circle(0, 0, 2.25, randomColor());
            p.setTranslateX(explodeX);
            p.setTranslateY(explodeY);
            p.setOpacity(0);
            particles[i] = p;
        }

        // 4. Shockwave Animasyonu
        ScaleTransition shockScale = new ScaleTransition(Duration.millis(100), shockwave);
        shockScale.setFromX(1); shockScale.setFromY(1);
        shockScale.setToX(30); shockScale.setToY(30);

        FadeTransition shockFade = new FadeTransition(Duration.millis(150), shockwave);
        shockFade.setFromValue(0.8); shockFade.setToValue(0);

        ParallelTransition shockAnim = new ParallelTransition(shockScale, shockFade);

        // 5. Partikül Animasyonları
        ParallelTransition particlesAnim = new ParallelTransition();
        for (Circle p : particles) {
            FadeTransition in = new FadeTransition(Duration.millis(10), p);
            in.setFromValue(0); in.setToValue(1);

            TranslateTransition move = new TranslateTransition(Duration.millis(500), p);
            move.setByX(Math.random() * 120 - 60);
            move.setByY(Math.random() * 120 - 60);

            FadeTransition out = new FadeTransition(Duration.millis(50), p);
            out.setFromValue(1); out.setToValue(0);
            out.setDelay(Duration.millis(30));

            SequentialTransition seq = new SequentialTransition(in, move, out);
            particlesAnim.getChildren().add(seq);
        }

        // 6. Circle'ın Kendi Patlama Animasyonu
        ScaleTransition circleScale = new ScaleTransition(Duration.millis(200), circle);
        circleScale.setFromX(1); circleScale.setFromY(1);
        circleScale.setToX(5); circleScale.setToY(5);

        FadeTransition circleFade = new FadeTransition(Duration.millis(200), circle);
        circleFade.setFromValue(1); circleFade.setToValue(0);

        // 7. Tümünü Birleştir
        ParallelTransition all = new ParallelTransition(shockAnim, particlesAnim, circleScale, circleFade);

        // 8. Sahneye ekle ve bitince temizle
        _pane.getChildren().add(shockwave);
        _pane.getChildren().addAll(particles);

        all.setOnFinished(e -> {
            _pane.getChildren().remove(shockwave);
            _pane.getChildren().removeAll(particles);
            _pane.getChildren().remove(circle);
            Map.activeEnemies.remove(this);
        });

        all.play();
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

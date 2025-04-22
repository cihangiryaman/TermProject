package com.example.termproject2;

import java.io.File;
import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public abstract class Enemy
{
    private int _health;
    private int _speed;
    private int _positionX;
    private int _positionY;
    private File _imagePath = new File("C:\\Users\\Cihangir\\IdeaProjects\\TermProject1\\src\\main\\resources\\com\\example\\termproject1\\Images\\Meltdown_3F.webp");

    Enemy(int initialHealth, int initialSpeed)
    {
        _speed = initialSpeed;
        _health = initialHealth;
        _imagePath = new File("C:\\Users\\Cihangir\\IdeaProjects\\TermProject1\\src\\main\\resources\\com\\example\\termproject1\\Images\\Meltdown_3F.webp");
    }

    Enemy(String imagePath, int initialHealth, int initialSpeed)
    {
        _speed = initialSpeed;
        _health = initialHealth;
        _imagePath = new File(imagePath);
    }

    public void move() //Yukarıda PositionX ve PositionY değişkenleri tanımladım kullanmayacaksan bunları silebilirsin.
    // Yalnızca move methodu ile alakalı onlar.
    {

    }

    public void explode(Pane p, double x, double y)
    {
        // Shockwave
        Circle shockwave = new Circle(x, y, 1);
        shockwave.setFill(Color.DARKORANGE);
        shockwave.setStroke(Color.DARKORANGE);
        shockwave.setStrokeWidth(1);

        // Particles
        int particleCount = 20;
        Circle[] particles = new Circle[particleCount];
        for (int i = 0; i < particleCount; i++) {
            particles[i] = new Circle(2.25, randomColor());
            particles[i].setCenterX(x);
            particles[i].setCenterY(y);
            particles[i].setOpacity(0);
        }
        p.getChildren().addAll(shockwave);
        p.getChildren().addAll(particles);

        // Shockwave animation
        ScaleTransition shockwaveScale = new ScaleTransition(Duration.millis(100), shockwave);
        shockwaveScale.setFromX(1); shockwaveScale.setFromY(1);
        shockwaveScale.setToX(30); shockwaveScale.setToY(30);

        FadeTransition shockwaveFade = new FadeTransition(Duration.millis(150), shockwave);
        shockwaveFade.setFromValue(0.8); shockwaveFade.setToValue(0);

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
        SequentialTransition explosion = new SequentialTransition(new ParallelTransition(shockwaveAnim),particleAnim);

        explosion.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                p.getChildren().removeAll(shockwave);
                p.getChildren().removeAll(particles);

            }
        });
        explosion.play();
    }

    public Color randomColor() {
        double r = Math.random();
        if (r < 0.3)
            return Color.ORANGE;
        else if (r < 0.6)
            return Color.RED;
        else
            return Color.YELLOW;
    }

    public int get_speed()
    {
        return _speed;
    }

    public void set_speed(int speed)
    {
        this._speed = speed;
    }

    public int get_health()
    {
        return _health;
    }

    public void set_health(int health)
    {
        this._health = health;
    }

    public int get_positionX() {
        return _positionX;
    }

    public int get_positionY() {
        return _positionY;
    }
}

package com.example.termproject2;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.File;

public abstract class Tower
{
    private String _name;
    private int _price;
    private int _damage;
    private int _range;
    private int _positionX;
    private int _positionY;
    private ImageView _image;
    private double _reloadTimeSeconds;

    Tower(String name,int price, int damage, int range, double reloadTimeSeconds)
    {
        _reloadTimeSeconds = reloadTimeSeconds;
        _name = name;
        _damage = damage;
        _price = price;
        _range = range;
        _image = new ImageView(new Image("Tower.png"));
        _image.setFitHeight(32);
        _image.setFitWidth(32);
    }

    Tower(String name,String ImageName, int price, int damage, int range, double reloadTimeSeconds)
    {
        _reloadTimeSeconds = reloadTimeSeconds;
        _name = name;
        _damage = damage;
        _price = price;
        _range = range;
        _image = new ImageView(new Image(ImageName));
        _image.setFitHeight(32);
        _image.setFitWidth(32);
    }

    public void shoot()
    {
        attackFirstEnemyInRange();

        Timeline shootTimer = new Timeline(new KeyFrame(Duration.seconds(_reloadTimeSeconds), e -> {
            attackFirstEnemyInRange();
        }));
        shootTimer.setCycleCount(Animation.INDEFINITE);
        shootTimer.play();
    }

    private void attackFirstEnemyInRange() {
        for (Enemy enemy : Map.activeEnemies)
        {
            double dx = enemy.getPositionX() - _positionX;
            double dy = enemy.getPositionY() - _positionY;
            double dist2 = dx * dx + dy * dy;
            if (dist2 <= _range * _range) {
                enemy.setHealth(-_damage);

                // Mermi animasyonu
                Circle bullet = new Circle(5, Color.ORANGERED);
                bullet.setTranslateX(_positionX - 10);
                bullet.setTranslateY(_positionY - 10);

                Pane enemyPane = (Pane) enemy.getCircle().getParent();
                if (enemyPane != null) {
                    enemyPane.getChildren().add(bullet);

                    TranslateTransition transition = new TranslateTransition(Duration.millis(200), bullet);
                    transition.setToX(enemy.getPositionX());
                    transition.setToY(enemy.getPositionY());

                    transition.setOnFinished(event1 -> enemyPane.getChildren().remove(bullet));
                    transition.play();
                }

                if (enemy.getHealth() <= 0 && !enemy.isExploding()) {
                    enemy.setExploding(true);
                    Platform.runLater(() -> {
                        enemy.stopMovement();
                        PauseTransition delay = new PauseTransition(Duration.millis(50));
                        delay.setOnFinished(ev -> enemy.explode());
                        delay.play();
                    });
                }
                break;
            }
        }
    }

    public int getPrice()
    {
        return _price;
    }

    public void setRange(int _range)
    {
        this._range = _range;
    }

    public int getRange()
    {
        return _range;
    }

    public int getDamage()
    {
        return _damage;
    }

    public void setDamage(int _damage)
    {
        this._damage = _damage;
    }

    public void setPosition(int x, int y)
    {
        this._positionX = x;
        this._positionY = y;
    }

    public double getPositionX()
    {
        return _positionX;
    }

    public double getPositionY()
    {
        return _positionY;
    }

    public ImageView getImage()
    {
        return _image;
    }

    public String get_name()
    {
        return _name;
    }
}

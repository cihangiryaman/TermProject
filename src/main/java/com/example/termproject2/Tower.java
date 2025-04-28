package com.example.termproject2;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.ArrayList;

public abstract class Tower
{
    private String _name;
    private int _price;
    private int _damage = 300;
    private int _range;
    private int _positionX;
    private int _positionY;
    private ImageView _image;

    Tower(String name,int price, int damage, int range)
    {
        _name = name;
        _damage = damage;
        _price = price;
        _range = range;
        _image = new ImageView(new Image("Tower.png"));
        _image.setFitHeight(32);
        _image.setFitWidth(32);
    }

    Tower(String name,String ImageName, int price, int damage, int range)
    {
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
        Timeline shootTimer = new Timeline(new KeyFrame(Duration.seconds(0.2), e -> {
            for (Enemy enemy : Map.activeEnemies) {
                double dx = enemy.getPositionX() - _positionX;
                double dy = enemy.getPositionY() - _positionY;
                double distanceSquared = dx * dx + dy * dy;

                if (distanceSquared <= _range * _range) {
                    enemy.setHealth(_damage);
                    if (enemy.getHealth() <= 0)
                    {
                        enemy.explode();
                    }
                    break;
                }
            }
        }));
        shootTimer.setCycleCount(Animation.INDEFINITE);
        shootTimer.play();
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

    public ImageView getImage()
    {
        return _image;
    }

    public String get_name()
    {
        return _name;
    }
}

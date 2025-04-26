package com.example.termproject2;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.ArrayList;

public abstract class Tower
{
    private int _price;
    private int _damage;
    private int _range;
    private int _positionX;
    private int _positionY;
    private InputStream _image = Tower.class.getResourceAsStream("/Castle.png");

    Tower(int price, int damage, int range)
    {
        _damage = damage;
        _price = price;
        _range = range;
    }

    Tower(String ImageName, int price, int damage, int range)
    {
        _damage = damage;
        _price = price;
        _range = range;
        _image = Tower.class.getResourceAsStream("/Images/" + ImageName);
    }

    public void shoot(StackPane cell)
    {
        Timeline shootTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            for (Enemy enemy : Map.activeEnemies) {
                double dx = enemy.getPositionX() - _positionX;
                double dy = enemy.getPositionY() - _positionY;
                double distanceSquared = dx * dx + dy * dy;

                if (distanceSquared <= _range * _range) {
                    enemy.setHealth(enemy.getHealth() - _damage);
                    if (enemy.getHealth() <= 0)
                    {
                        // remove visual and from active list
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
        this._positionY = x;
        this._positionX = x;
    }
}

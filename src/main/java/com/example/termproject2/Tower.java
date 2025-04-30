package com.example.termproject2;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;

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
            System.out.println("Tower konumu: ("+_positionX+", "+_positionY+"), menzil²="+(_range*_range));
            for (Enemy enemy : Map.activeEnemies) {
                double dx = enemy.getPositionX() - _positionX;
                double dy = enemy.getPositionY() - _positionY;
                double dist2 = dx*dx + dy*dy;
                System.out.println("  Enemy konumu: ("+enemy.getPositionX()+", "+enemy.getPositionY()+"), dist²="+dist2);
                if (dist2 <= _range*_range) {
                    if (enemy.getHealth() <= 0)
                    {
                        enemy.explode();
                        Map.activeEnemies.remove(enemy);
                    }
                    else { enemy.setHealth(-_damage); }
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

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

    public void shoot() { }

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

    public String getName()
    {
        return _name;
    }

    public double getReloadTimeSeconds()
    {
        return _reloadTimeSeconds;
    }
}

package com.example.termproject2;

import javafx.scene.layout.StackPane;

import java.io.InputStream;

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

    public void shoot()
    {

    }

    public int get_price()
    {
        return _price;
    }

    public void set_range(int _range)
    {
        this._range = _range;
    }

    public int get_range()
    {
        return _range;
    }

    public int get_damage()
    {
        return _damage;
    }

    public void set_damage(int _damage)
    {
        this._damage = _damage;
    }
}

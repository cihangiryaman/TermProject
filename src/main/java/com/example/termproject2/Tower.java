package com.example.termproject2;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public abstract class Tower
{
    private String _name;
    private int _price;
    private int _damage;
    private int _range;
    private int _positionX;
    private int _positionY;
    private ImageView _image;
    private String _imagePath;
    private double _reloadTimeSeconds;
    private StackPane parentCell;
    private int level = 1;
    private boolean isDeleted;

    Tower(String name,int price, int damage, int range, double reloadTimeSeconds)
    {
        isDeleted = false;
        _reloadTimeSeconds = reloadTimeSeconds;
        _name = name;
        _damage = damage;
        _price = price;
        _range = range;
        _image = new ImageView(new Image("Tower.png"));
        _image.setFitHeight(32);
        _image.setFitWidth(32);
        _imagePath = "Tower.png";
    }

    Tower(String name,String ImageName, int price, int damage, int range, double reloadTimeSeconds)
    {
        isDeleted = false;
        _reloadTimeSeconds = reloadTimeSeconds;
        _name = name;
        _damage = damage;
        _price = price;
        _range = range;
        _image = new ImageView(new Image(ImageName));
        _image.setFitHeight(32);
        _image.setFitWidth(32);
        _imagePath = ImageName;
    }

    public void shoot() {}

    public void setprice(int price)
    {
        _price = price;
    }

    public void set_damage(int damage)
    {
        _damage = damage;
    }
    public void setimage(String imagePath)
    {
        _image = new ImageView(new Image(imagePath));
    }

    public void levelUp() {

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

    public String getName()
    {
        return _name;
    }

    public double getReloadTimeSeconds()
    {
        return _reloadTimeSeconds;
    }

    public void setReloadTimeSeconds(double reloadTimeSeconds)
    {
        _reloadTimeSeconds = reloadTimeSeconds;
    }

    public String get_imagePath() {
        return _imagePath;
    }

    public void set_imagePath(String _imagePath) {
        this._imagePath = _imagePath;
    }

    public StackPane getParentCell()
    {
        return parentCell;
    }

    public void setParentCell(StackPane cell)
    {
        parentCell = cell;
    }

    public int getLevel()
    {
        return level;
    }
    public void setLevel(int level)
    {
        this.level = level;
    }
    public boolean isDeleted()
    {
        return isDeleted;
    }

    public void delete()
    {
        isDeleted = true;
    }
}

package com.example.termproject2;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class LaserTower extends Tower
{
    LaserTower(String name, int price, int damage, int range) {
        super(name, price, damage, range, 0.1);
    }

    LaserTower(String name, String ImageName, int price, int damage, int range) {
        super(name, ImageName, price, damage, range, 0.1);
    }

    @Override
    public void shoot()
    {

    }
}

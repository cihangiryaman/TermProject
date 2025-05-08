package com.example.termproject2;

import javafx.scene.layout.Pane;

public class TankEnemy extends Enemy
{
    TankEnemy(Pane pane, int initialHealth, double initialSpeed, MapPane mapPane)
    {
        super(pane, initialHealth, initialSpeed, mapPane);
    }

    TankEnemy(Pane pane, String imageName, int initialHealth, double initialSpeed, MapPane mapPane)
    {
        super(pane, imageName, initialHealth, initialSpeed, mapPane);
    }
}

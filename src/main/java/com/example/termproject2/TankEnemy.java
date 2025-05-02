package com.example.termproject2;

import javafx.scene.layout.Pane;

public class TankEnemy extends Enemy
{
    TankEnemy(Pane pane, int initialHealth, double initialSpeed)
    {
        super(pane, initialHealth, initialSpeed);
    }

    TankEnemy(Pane pane, String imageName, int initialHealth, double initialSpeed)
    {
        super(pane, imageName, initialHealth, initialSpeed);
    }
}

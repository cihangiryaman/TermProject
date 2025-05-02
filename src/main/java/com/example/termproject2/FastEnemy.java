package com.example.termproject2;

import javafx.scene.layout.Pane;

public class FastEnemy extends Enemy
{
    FastEnemy(Pane pane, int initialHealth, double initialSpeed)
    {
        super(pane, initialHealth, initialSpeed);
    }

    FastEnemy(Pane pane, String imageName, int initialHealth, double initialSpeed)
    {
        super(pane, imageName, initialHealth, initialSpeed);
    }
}

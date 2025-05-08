package com.example.termproject2;

import javafx.scene.layout.Pane;

public class FastEnemy extends Enemy
{
    FastEnemy(Pane pane, int initialHealth, double initialSpeed, MapPane mapPane)
    {
        super(pane, initialHealth, initialSpeed, mapPane);
    }

    FastEnemy(Pane pane, String imageName, int initialHealth, double initialSpeed, MapPane mapPane)
    {
        super(pane, imageName, initialHealth, initialSpeed, mapPane);
    }
}

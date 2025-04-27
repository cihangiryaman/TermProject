package com.example.termproject2;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class Infantry extends Enemy
{
    Infantry(Pane pane, int initialHealth, double initialSpeed)
    {
        super(pane, initialHealth, initialSpeed);
    }

    Infantry(Pane pane, String imagePath, int initialHealth, double initialSpeed)
    {
        super(pane, imagePath, initialHealth, initialSpeed);
    }
}

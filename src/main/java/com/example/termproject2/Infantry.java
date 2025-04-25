package com.example.termproject2;

import javafx.scene.layout.StackPane;

public class Infantry extends Enemy
{
    Infantry(int initialHealth, double initialSpeed)
    {
        super(initialHealth, initialSpeed);
    }

    Infantry(String imagePath, int initialHealth, double initialSpeed)
    {
        super(imagePath, initialHealth, initialSpeed);
    }
}

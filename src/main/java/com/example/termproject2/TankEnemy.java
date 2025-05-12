package com.example.termproject2;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class TankEnemy extends Enemy
{
    TankEnemy(Pane pane, int initialHealth, double initialSpeed, MapPane mapPane)
    {
        super(pane, initialHealth, initialSpeed, mapPane);
        image = new ImageView("Enemy.png"); //Image of the Enemy
        image.setFitHeight(40);
        image.setFitWidth(40);
    }

    TankEnemy(Pane pane, String imageName, int initialHealth, double initialSpeed, MapPane mapPane)
    {
        super(pane, imageName, initialHealth, initialSpeed, mapPane);
        image = new ImageView("Enemy.png"); //Image of the Enemy
        image.setFitHeight(40);
        image.setFitWidth(40);
    }
}

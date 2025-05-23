package com.example.termproject2;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FastEnemy extends Enemy
{

    FastEnemy(Pane pane, int initialHealth, double initialSpeed, MapPane mapPane)
    {
        super(pane, initialHealth, initialSpeed, mapPane);
        image = new ImageView("wolf.png"); //Image of the Enemy
        image.setFitHeight(40);
        image.setFitWidth(40);
        healthBar = new Rectangle(maxHealthWidth,3.5, Color.BLUE);
    }

    FastEnemy(Pane pane, String imageName, int initialHealth, double initialSpeed, MapPane mapPane)
    {
        super(pane, imageName, initialHealth, initialSpeed, mapPane);
        image = new ImageView("wolf.png"); //Image of the Enemy
        image.setFitHeight(40);
        image.setFitWidth(40);
        healthBar = new Rectangle(maxHealthWidth,3.5, Color.BLUE);
    }
}

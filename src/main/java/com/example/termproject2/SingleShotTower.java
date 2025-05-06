package com.example.termproject2;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class SingleShotTower extends Tower
{
    SingleShotTower(int price, int damage, int range)
    {
        super("Single Shot Tower", price, damage, range, 0.5);
    }

    SingleShotTower(String ImageName, int price, int damage, int range)
    {
        super("Single Shot Tower", ImageName, price, damage, range, 0.5);
    }

    @Override
    public void shoot()
    {
        attackFirstEnemyInRange();

        Timeline shootTimer = new Timeline(new KeyFrame(Duration.seconds(getReloadTimeSeconds()), e -> {
            attackFirstEnemyInRange();
        }));
        shootTimer.setCycleCount(Animation.INDEFINITE);
        shootTimer.play();
    }

    @Override
    public void levelUp() {
        int level = getLevel();
        if (level < 3) {
            level++;
            setLevel(level);
            setprice(getPrice() * 2);
            set_damage(getDamage() * 2);
            //This one should have the codes that will change the imageView of the castle according to level
        }
        else
        {
            //This one should have something to inform player that the castle already at max level
        }
    }

    private void attackFirstEnemyInRange()
    {
        for (Enemy enemy : Map.activeEnemies) {
            double centerX = getPositionX() - 5;
            double centerY = getPositionY() - 20;

            double dx = enemy.getPositionX() - getPositionX();
            double dy = enemy.getPositionY() - getPositionY();
            double dist2 = dx * dx + dy * dy;
            if (dist2 <= getRange() * getRange()) {

                // Mermi animasyonu
                Circle bullet = new Circle(5, Color.ORANGERED);
                bullet.setTranslateX(centerX);
                bullet.setTranslateY(centerY - 5);

                Pane enemyPane = (Pane) enemy.getCircle().getParent();
                if (enemyPane != null) {
                    enemyPane.getChildren().add(bullet);

                    double distance = Math.sqrt(dist2);
                    double bulletSpeed = 300.0; // px/s
                    double durationMillis = (distance / bulletSpeed) * 1000;

                    TranslateTransition transition = new TranslateTransition(Duration.millis(durationMillis), bullet);
                    transition.setToX(enemy.getPositionX());
                    transition.setToY(enemy.getPositionY() - 12);
                    transition.setInterpolator(Interpolator.EASE_BOTH);

                    transition.setOnFinished(event1 -> {
                        enemyPane.getChildren().remove(bullet);
                        enemy.setHealth(-getDamage());

                        if (enemy.getHealth() <= 0 && !enemy.isExploding()) {
                            enemy.setExploding(true);
                            Platform.runLater(() -> {
                                enemy.stopMovement();
                                PauseTransition delay = new PauseTransition(Duration.millis(50));
                                delay.setOnFinished(ev -> enemy.explode());
                                delay.play();
                            });
                        }
                    });
                    transition.play();
                }
                break;
            }
        }
    }
}

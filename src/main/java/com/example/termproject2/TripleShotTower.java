package com.example.termproject2;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TripleShotTower extends Tower
{
    TripleShotTower(int price, int damage, int range) {
        super("Triple Shot Tower", price, damage, range, 0.5);
    }

    TripleShotTower(String ImageName, int price, int damage, int range) {
        super("Triple Shot Tower", ImageName, price, damage, range, 0.5);
    }

    @Override
    public void shoot()
    {
        attackEnemiesInRange();
        Timeline shootTimer = new Timeline(new KeyFrame(Duration.seconds(getReloadTimeSeconds()), e -> {
            attackEnemiesInRange();
        }));
        shootTimer.setCycleCount(Animation.INDEFINITE);
        shootTimer.play();
    }

    private void attackEnemiesInRange() {
        double centerX = getPositionX() - 5;
        double centerY = getPositionY() - 20;

        List<Enemy> enemiesInRange = new ArrayList<>();
        for (Enemy enemy : Map.activeEnemies) {
            double dx = enemy.getPositionX() - getPositionX();
            double dy = enemy.getPositionY() - getPositionY();
            double dist2 = dx * dx + dy * dy;
            if (dist2 <= getRange() * getRange()) {
                enemiesInRange.add(enemy);
            }
        }

        // En yakın 3 düşmanı sırala ve al
        enemiesInRange.sort((a, b) -> {
            double distA = Math.pow(a.getPositionX() - getPositionX(), 2) + Math.pow(a.getPositionY() - getPositionY(), 2);
            double distB = Math.pow(b.getPositionX() - getPositionX(), 2) + Math.pow(b.getPositionY() - getPositionY(), 2);
            return Double.compare(distA, distB);
        });

        int shotsFired = 0;
        for (Enemy enemy : enemiesInRange) {
            if (shotsFired >= 3) break;
            fireBulletAt(enemy, centerX, centerY);
            shotsFired++;
        }
    }

    private void fireBulletAt(Enemy enemy, double startX, double startY) {
        Circle bullet = new Circle(3.5, Color.ORANGERED);
        bullet.setTranslateX(startX);
        bullet.setTranslateY(startY - 5);

        Pane enemyPane = (Pane) enemy.getCircle().getParent();
        if (enemyPane != null) {
            enemyPane.getChildren().add(bullet);

            double dx = enemy.getPositionX() - getPositionX();
            double dy = enemy.getPositionY() - getPositionY();
            double distance = Math.sqrt(dx * dx + dy * dy);
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
    }
}

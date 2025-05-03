package com.example.termproject2;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class LaserTower extends Tower
{
    LaserTower(int price, int damage, int range) {
        super("Laser Tower", price, damage, range, 0.1);
    }

    LaserTower(String ImageName, int price, int damage, int range) {
        super("Laser Tower", ImageName, price, damage, range, 0.1);
    }

    @Override
    public void shoot() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), e -> {
            Enemy target = findClosestEnemyInRange();
            if (target != null) {
                drawLaserAndDamage(target);
            } else {
                removeLaser();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private Enemy findClosestEnemyInRange() {
        Enemy closest = null;
        double minDist = Double.MAX_VALUE;
        for (Enemy enemy : Map.activeEnemies) {
            double dx = enemy.getPositionX() - getPositionX();
            double dy = enemy.getPositionY() - getPositionY();
            double dist2 = dx * dx + dy * dy;
            if (dist2 <= getRange() * getRange() && dist2 < minDist) {
                minDist = dist2;
                closest = enemy;
            }
        }
        return closest;
    }

    private javafx.scene.shape.Line currentLaser;

    private void drawLaserAndDamage(Enemy enemy) {
        Pane enemyPane = (Pane) enemy.getCircle().getParent();
        if (enemyPane == null) return;

        double startX = getPositionX();
        double startY = getPositionY();
        double endX = enemy.getPositionX();
        double endY = enemy.getPositionY();

        if (currentLaser == null) {
            currentLaser = new javafx.scene.shape.Line();
            currentLaser.setStroke(Color.RED);
            currentLaser.setStrokeWidth(2);
            enemyPane.getChildren().add(currentLaser);
        }

        currentLaser.setStartX(startX);
        currentLaser.setStartY(startY);
        currentLaser.setEndX(endX);
        currentLaser.setEndY(endY);

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
    }

    private void removeLaser() {
        if (currentLaser != null) {
            Pane parent = (Pane) currentLaser.getParent();
            if (parent != null) {
                parent.getChildren().remove(currentLaser);
            }
            currentLaser = null;
        }
    }
}

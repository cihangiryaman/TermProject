package com.example.termproject2;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class LaserTower extends Tower {
    LaserTower(int price, int damage, int range) {
        super("LaserTower1.png", price, damage, range, 0.1);
    }

    LaserTower(String ImageName, int price, int damage, int range) {
        super("Light Channeler", ImageName, price, damage, range, 0.1);
    }

    @Override
    public void shoot()
    {
        attackEnemiesInRange();
        Timeline shootTimer = new Timeline(new KeyFrame(Duration.seconds(getReloadTimeSeconds()), e -> {
            if (!isDeleted())
                attackEnemiesInRange();
        }));
        shootTimer.setCycleCount(Animation.INDEFINITE);
        shootTimer.play();
    }

    private void attackEnemiesInRange()
    {
        // Menzildeki düşmanları bulma
        List<Enemy> enemiesInRange = new ArrayList<>();
        for (Enemy enemy : Map.activeEnemies) {
            double dx = enemy.getPositionX() - getPositionX();
            double dy = enemy.getPositionY() - getPositionY();
            double dist2 = dx * dx + dy * dy;
            if (dist2 <= getRange() * getRange()) {
                enemiesInRange.add(enemy);
            }
        }

        for (Enemy enemy : enemiesInRange) {
            fireBulletAt(enemy);
        }
    }

    private void fireBulletAt(Enemy enemy)
    {
        Pane overlay = MapPane.getOverlayPane();
        Bounds towerBounds = getParentCell().localToScene(getParentCell().getBoundsInLocal());
        double towerCenterX = towerBounds.getMinX() + towerBounds.getWidth() / 2;
        double towerCenterY = towerBounds.getMinY() + towerBounds.getHeight() / 2;

        Bounds enemyBounds = enemy.getCircle().localToScene(enemy.getCircle().getBoundsInLocal());
        double enemyCenterX = enemyBounds.getMinX() + enemyBounds.getWidth() / 2;
        double enemyCenterY = enemyBounds.getMinY() + enemyBounds.getHeight() / 2;
        Point2D towerPoint = overlay.sceneToLocal(towerCenterX, towerCenterY);
        Point2D enemyPoint = overlay.sceneToLocal(enemyCenterX, enemyCenterY);

        // Lazer oluşturma
        Line laser = new Line();
        laser.setStartX(towerPoint.getX());
        laser.setStartY(towerPoint.getY());
        laser.setEndX(enemyPoint.getX());
        laser.setEndY(enemyPoint.getY());
        laser.setStroke(Color.RED);
        laser.setStrokeWidth(2.5);

        // Overlay pane'e ekle
        overlay.getChildren().add(laser);

        // Lazer efekti için animasyon
        FadeTransition fade = new FadeTransition(Duration.millis(100), laser);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> overlay.getChildren().remove(laser));
        fade.play();

        // Düşmana hasar ver
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

    public void levelUp()
    {
        int level = getLevel();
        if (level < 3) {
            level++;
            setLevel(level);
            setprice(getPrice() * 2);
            set_damage(getDamage() * 2);
        } else {
            // Maksimum seviyeye ulaşılmış
        }
    }
}
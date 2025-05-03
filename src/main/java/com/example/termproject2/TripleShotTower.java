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
    Pane pane;
    TripleShotTower(String name, int price, int damage, int range, double reloadTimeSeconds) {
        super(name, price, damage, range, reloadTimeSeconds);
        pane = new MapPane(new File("level5.txt")).getPane();
    }

    TripleShotTower(String name, String ImageName, int price, int damage, int range, double reloadTimeSeconds) {
        super(name, ImageName, price, damage, range, reloadTimeSeconds);
    }

    @Override
    public void shoot()
    {
        attackClosestEnemiesInRange(3);

        Timeline shootTimer = new Timeline(new KeyFrame(Duration.seconds(getReloadTimeSeconds()), e -> {
            attackClosestEnemiesInRange(3);
        }));
        shootTimer.setCycleCount(Animation.INDEFINITE);
        shootTimer.play();
    }

    @Override
    public void levelUp() {

    }

    private void attackClosestEnemiesInRange(int count) {
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

        enemiesInRange.sort(Comparator.comparingDouble(e ->
                Math.pow(e.getPositionX() - getPositionX(), 2) +
                        Math.pow(e.getPositionY() - getPositionY(), 2)));

        // Aynı anda 3 mermi gönder
        List<Enemy> targets = enemiesInRange.subList(0, Math.min(3, enemiesInRange.size()));
        for (Enemy enemy : targets) {
            shootBulletAt(enemy, centerX, centerY);
        }
    }

    private void shootBulletAt(Enemy enemy, double centerX, double centerY) {
        Circle bullet = new Circle(5, Color.ORANGERED);
        bullet.setTranslateX(centerX);
        bullet.setTranslateY(centerY - 5);

        javafx.scene.Parent parent = enemy.getCircle().getParent();
        if (parent instanceof Pane enemyPane) {
            enemyPane.getChildren().add(bullet);

            double dx = enemy.getPositionX() - getPositionX();
            double dy = enemy.getPositionY() - getPositionY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            double bulletSpeed = 300.0;
            double durationMillis = (distance / bulletSpeed) * 1000;

            TranslateTransition transition = new TranslateTransition(Duration.millis(durationMillis), bullet);
            transition.setToX(enemy.getPositionX());
            transition.setToY(enemy.getPositionY() - 12);
            transition.setInterpolator(Interpolator.EASE_BOTH);

            transition.setOnFinished(event -> {
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

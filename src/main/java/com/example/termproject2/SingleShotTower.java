package com.example.termproject2;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class SingleShotTower extends Tower
{
    SingleShotTower(int price, int damage, int range)
    {
        super("SingleShotTower1.png", price, damage, range, 0.5);
    }

    SingleShotTower(String ImageName, int price, int damage, int range)
    {
        super("SingleShotTower1.png", ImageName, price, damage, range, 0.5);
    }

    @Override
    public void shoot()
    {
        attackFirstEnemyInRange();

        Timeline shootTimer = new Timeline(new KeyFrame(Duration.seconds(getReloadTimeSeconds()), e -> {
            if (!isDeleted())
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
    }

    private void attackFirstEnemyInRange()
    {
        for (Enemy enemy : Map.activeEnemies) {
            double dx = enemy.getPositionX() - getPositionX();
            double dy = enemy.getPositionY() - getPositionY();
            double dist2 = dx * dx + dy * dy;
            if (dist2 <= getRange() * getRange()) {
                fireBulletAt(enemy);
                break;
            }
        }
    }

    private void fireBulletAt(Enemy enemy)
    {
        Pane overlay = MapPane.getOverlayPane();

        // Get tower position in scene coordinates
        Bounds towerBounds = getParentCell().localToScene(getParentCell().getBoundsInLocal());
        double towerCenterX = towerBounds.getMinX() + towerBounds.getWidth() / 2;
        double towerCenterY = towerBounds.getMinY() + towerBounds.getHeight() / 2;

        // Get enemy position in scene coordinates
        Bounds enemyBounds = enemy.getCircle().localToScene(enemy.getCircle().getBoundsInLocal());
        double enemyCenterX = enemyBounds.getMinX() + enemyBounds.getWidth() / 2;
        double enemyCenterY = enemyBounds.getMinY() + enemyBounds.getHeight() / 2;

        // Convert to overlay coordinates
        Point2D towerPoint = overlay.sceneToLocal(towerCenterX, towerCenterY);
        Point2D enemyPoint = overlay.sceneToLocal(enemyCenterX, enemyCenterY);

        // Create bullet
        Circle bullet = new Circle(5, Color.ORANGERED);
        // Don't use setTranslateX/Y, set actual position to start at exactly tower center
        bullet.setCenterX(towerPoint.getX());
        bullet.setCenterY(towerPoint.getY());

        // Add bullet to overlay
        overlay.getChildren().add(bullet);

        // Calculate bullet movement
        double distance = Math.sqrt(
                (enemyPoint.getX() - towerPoint.getX()) * (enemyPoint.getX() - towerPoint.getX()) +
                        (enemyPoint.getY() - towerPoint.getY()) * (enemyPoint.getY() - towerPoint.getY())
        );
        double bulletSpeed = 300.0; // px/s
        double durationMillis = (distance / bulletSpeed) * 1000;

        // Create a timeline to animate the bullet precisely
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.ZERO,
                        new javafx.animation.KeyValue(bullet.centerXProperty(), towerPoint.getX()),
                        new javafx.animation.KeyValue(bullet.centerYProperty(), towerPoint.getY())
                )
        );
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(durationMillis),
                        new javafx.animation.KeyValue(bullet.centerXProperty(), enemyPoint.getX(), Interpolator.EASE_BOTH),
                        new javafx.animation.KeyValue(bullet.centerYProperty(), enemyPoint.getY(), Interpolator.EASE_BOTH)
                )
        );

        timeline.setOnFinished(event -> {
            overlay.getChildren().remove(bullet);
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
        timeline.play();
    }
}
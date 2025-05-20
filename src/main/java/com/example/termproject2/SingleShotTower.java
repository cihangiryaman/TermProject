package com.example.termproject2;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import static com.example.termproject2.Map.activeTowers;

public class SingleShotTower extends Tower
{
    SingleShotTower(int price, int damage, int range)
    {
        super("SingleShotTower1.png", price, damage, range, 0.5);
    }

    SingleShotTower(String ImageName, int price, int damage, int range)
    {
        super("Single Archer Tower", ImageName, price, damage, range, 0.5);
    }

    @Override
    public void shoot()
    {
        attackFirstEnemyInRange();

        Timeline shootTimer = new Timeline(new KeyFrame(Duration.seconds(getReloadTimeSeconds()), e -> {
            if (activeTowers.contains(this))
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
            setprice((int)(getPrice() * 1.5));
            set_damage((int)(getDamage() * 1.5));
        }
    }

    private void attackFirstEnemyInRange() {
        Enemy closestEnemy = null;
        double closestDistanceSquared = Double.MAX_VALUE;

        for (Enemy enemy : Map.activeEnemies) {
            double dx = enemy.getPositionX() - getPositionX();
            double dy = enemy.getPositionY() - getPositionY();
            double dist2 = dx * dx + dy * dy;

            if (dist2 <= getRange() * getRange() && dist2 < closestDistanceSquared) {
                closestDistanceSquared = dist2;
                closestEnemy = enemy;
            }
        }

        if (closestEnemy != null) {
            fireBulletAt(closestEnemy);
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

        // Create arrow image as bullet
        Image arrowImage = new Image("arrow.png");
        ImageView arrow = new ImageView(arrowImage);
        arrow.setFitWidth(20);  // Resize if needed
        arrow.setFitHeight(8);
        arrow.setX(towerPoint.getX() - arrow.getFitWidth() / 2);
        arrow.setY(towerPoint.getY() - arrow.getFitHeight() / 2);

        // Rotate arrow to face the target
        double angle = Math.toDegrees(Math.atan2(enemyPoint.getY() - towerPoint.getY(), enemyPoint.getX() - towerPoint.getX()));
        arrow.setRotate(angle);

        overlay.getChildren().add(arrow);

        double distance = towerPoint.distance(enemyPoint);
        double bulletSpeed = 350.0;
        double durationMillis = (distance / bulletSpeed) * 1000;

        TranslateTransition transition = new TranslateTransition(Duration.millis(durationMillis), arrow);
        transition.setToX(enemyPoint.getX() - towerPoint.getX());
        transition.setToY(enemyPoint.getY() - towerPoint.getY());

        transition.setOnFinished(event -> {
            overlay.getChildren().remove(arrow);
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
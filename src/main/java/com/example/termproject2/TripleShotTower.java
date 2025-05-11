package com.example.termproject2;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class TripleShotTower extends Tower {
    TripleShotTower(int price, int damage, int range) {
        super("TripleShotTower1.png", price, damage, range, 0.5);
    }

    TripleShotTower(String ImageName, int price, int damage, int range) {
        super("Triple Archer Tower", ImageName, price, damage, range, 0.5);
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
        double centerX = getPositionX();
        double centerY = getPositionY();

        List<Enemy> enemiesInRange = new ArrayList<>();
        for (Enemy enemy : Map.activeEnemies) {
            double dx = enemy.getPositionX() - centerX;
            double dy = enemy.getPositionY() - centerY;
            double dist2 = dx * dx + dy * dy;
            if (dist2 <= getRange() * getRange()) {
                enemiesInRange.add(enemy);
            }
        }

        //en yakın 3 düşman
        enemiesInRange.sort((a, b) -> {
            double distA = Math.pow(a.getPositionX() - centerX, 2) + Math.pow(a.getPositionY() - centerY, 2);
            double distB = Math.pow(b.getPositionX() - centerX, 2) + Math.pow(b.getPositionY() - centerY, 2);
            return Double.compare(distA, distB);
        });

        int shotsFired = 0;
        for (Enemy enemy : enemiesInRange) {
            if (shotsFired >= 3)
                break;
            fireBulletAt(enemy);
            shotsFired++;
        }
    }

    private void fireBulletAt(Enemy enemy) {

        javafx.geometry.Bounds towerBounds = getParentCell().localToScene(getParentCell().getBoundsInLocal());
        double towerCenterX = towerBounds.getMinX() + towerBounds.getWidth() / 2;
        double towerCenterY = towerBounds.getMinY() + towerBounds.getHeight() / 2;
        javafx.geometry.Bounds enemyBounds = enemy.getCircle().localToScene(enemy.getCircle().getBoundsInLocal());
        double enemyCenterX = enemyBounds.getMinX() + enemyBounds.getWidth() / 2;
        double enemyCenterY = enemyBounds.getMinY() + enemyBounds.getHeight() / 2;

        Pane overlayPane = MapPane.getOverlayPane();
        javafx.geometry.Point2D towerPoint = overlayPane.sceneToLocal(towerCenterX, towerCenterY);
        javafx.geometry.Point2D enemyPoint = overlayPane.sceneToLocal(enemyCenterX, enemyCenterY);

        // Ok resmi oluştur
        Image arrowImage = new Image("arrow.png");
        ImageView arrow = new ImageView(arrowImage);
        arrow.setFitWidth(20);  // Gerekirse boyutlandırabilirsiniz
        arrow.setFitHeight(7);
        arrow.setX(towerPoint.getX() - arrow.getFitWidth() / 2);
        arrow.setY(towerPoint.getY() - arrow.getFitHeight() / 2);

        // Okun hedefe yönlendirilmesi (90 derece döndürme)
        double angle = Math.toDegrees(Math.atan2(enemyPoint.getY() - towerPoint.getY(), enemyPoint.getX() - towerPoint.getX()));
        arrow.setRotate(angle);

        // Okun overlayPane'e eklenmesi
        overlayPane.getChildren().add(arrow);

        // Mermi hızı ve hareket hesaplama
        double dx = enemyPoint.getX() - towerPoint.getX();
        double dy = enemyPoint.getY() - towerPoint.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        double bulletSpeed = 500.0; // px/s
        double durationMillis = (distance / bulletSpeed) * 1000;

        // Okun hareketini ayarlama
        TranslateTransition transition = new TranslateTransition(Duration.millis(durationMillis), arrow);
        transition.setByX(dx);
        transition.setByY(dy);
        transition.setInterpolator(Interpolator.EASE_BOTH);

        transition.setOnFinished(event -> {
            overlayPane.getChildren().remove(arrow);
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


    @Override
    public void levelUp() {
        int level = getLevel();
        if (level < 3) {
            level++;
            setLevel(level);
            setprice(getPrice() * 2);
            set_damage(getDamage() * 2);
            setimage("TripleShotTower" + level + ".png");
        }
        else
        {
            // Maksimum seviye
        }
    }
}
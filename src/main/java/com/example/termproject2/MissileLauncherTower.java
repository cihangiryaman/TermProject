package com.example.termproject2;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import static com.example.termproject2.Map.activeTowers;

public class MissileLauncherTower extends Tower {
    private final int explosionRadius;
    private final double explosionDamageMultiplier;

    MissileLauncherTower(int price, int damage, int range) {
        super("MissileLauncherTower", "MissileLauncherTower1.png", price, damage, range, 0.8); // Slightly slower reload time than normal tower
        this.explosionRadius = 80;
        this.explosionDamageMultiplier = 0.5;
    }

    MissileLauncherTower(String ImageName, int price, int damage, int range) {
        super("Bomber Tower", ImageName, price, damage, range, 0.8);
        this.explosionRadius = 80;
        this.explosionDamageMultiplier = 0.5;
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
            setprice(getPrice() * 2);
            set_damage(getDamage() * 2);
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
            fireExplosiveBulletAt(closestEnemy);
        }
    }

    private void fireExplosiveBulletAt(Enemy targetEnemy) {
        Pane overlay = MapPane.getOverlayPane();
        Bounds towerBounds = getParentCell().localToScene(getParentCell().getBoundsInLocal());
        double towerCenterX = towerBounds.getMinX() + towerBounds.getWidth() / 2;
        double towerCenterY = towerBounds.getMinY() + towerBounds.getHeight() / 2;
        Bounds enemyBounds = targetEnemy.getCircle().localToScene(targetEnemy.getCircle().getBoundsInLocal());
        double enemyCenterX = enemyBounds.getMinX() + enemyBounds.getWidth() / 2;
        double enemyCenterY = enemyBounds.getMinY() + enemyBounds.getHeight() / 2;
        Point2D towerPoint = overlay.sceneToLocal(towerCenterX, towerCenterY);
        Point2D enemyPoint = overlay.sceneToLocal(enemyCenterX, enemyCenterY);

        RadialGradient bulletGradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.5, true,
                javafx.scene.paint.CycleMethod.NO_CYCLE,
                new Stop(0, Color.YELLOW),
                new Stop(1, Color.RED)
        );

        Circle bullet = new Circle(6, bulletGradient);
        bullet.setCenterX(towerPoint.getX());
        bullet.setCenterY(towerPoint.getY());

        overlay.getChildren().add(bullet);

        double distance = Math.sqrt(
                Math.pow(enemyPoint.getX() - towerPoint.getX(), 2) +
                        Math.pow(enemyPoint.getY() - towerPoint.getY(), 2)
        );
        double bulletSpeed = 350.0;
        double durationMillis = (distance / bulletSpeed) * 1000;

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
            targetEnemy.setHealth(-getDamage());
            createExplosion(enemyPoint.getX(), enemyPoint.getY(), overlay);
            applyAreaDamage(enemyPoint.getX(), enemyPoint.getY());
        });
        timeline.play();
    }

    private void createExplosion(double x, double y, Pane overlay) {
        RadialGradient explosionGradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.5, true,
                javafx.scene.paint.CycleMethod.NO_CYCLE,
                new Stop(0, Color.YELLOW),
                new Stop(0.6, Color.ORANGE),
                new Stop(0.8, Color.RED),
                new Stop(1, Color.color(1, 0, 0, 0.0))
        );

        Circle explosion = new Circle(5, explosionGradient);
        explosion.setCenterX(x);
        explosion.setCenterY(y);
        overlay.getChildren().add(explosion);

        Timeline explosionTimeline = new Timeline();
        explosionTimeline.getKeyFrames().add(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(explosion.radiusProperty(), 5),
                        new KeyValue(explosion.opacityProperty(), 0.9)
                )
        );
        explosionTimeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(300),
                        new KeyValue(explosion.radiusProperty(), explosionRadius, Interpolator.EASE_OUT),
                        new KeyValue(explosion.opacityProperty(), 0.7)
                )
        );
        explosionTimeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(500),
                        new KeyValue(explosion.radiusProperty(), explosionRadius * 1.1, Interpolator.EASE_OUT),
                        new KeyValue(explosion.opacityProperty(), 0)
                )
        );

        explosionTimeline.setOnFinished(e -> overlay.getChildren().remove(explosion));
        explosionTimeline.play();
    }

    private void applyAreaDamage(double explosionX, double explosionY) {
        int splashDamage = (int) (getDamage() * explosionDamageMultiplier);

        for (Enemy enemy : Map.activeEnemies) {
            if (enemy.isExploding()) continue;
            Bounds enemyBounds = enemy.getCircle().localToScene(enemy.getCircle().getBoundsInLocal());
            double enemyCenterX = enemyBounds.getMinX() + enemyBounds.getWidth() / 2;
            double enemyCenterY = enemyBounds.getMinY() + enemyBounds.getHeight() / 2;

            Point2D enemyPoint = MapPane.getOverlayPane().sceneToLocal(enemyCenterX, enemyCenterY);

            double dx = enemyPoint.getX() - explosionX;
            double dy = enemyPoint.getY() - explosionY;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance <= explosionRadius) {
                double damageMultiplier = 1.0 - (distance / explosionRadius);
                int actualDamage = (int) (splashDamage * damageMultiplier);

                enemy.setHealth(-Math.max(1, actualDamage));

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
        }
    }
}
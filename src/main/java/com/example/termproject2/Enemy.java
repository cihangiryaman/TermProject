package com.example.termproject2;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;

import static com.example.termproject2.Map.activeEnemies;
import static com.example.termproject2.Map.activeTowers;

public abstract class Enemy
{
    private Pane _pane;
    private MapPane _mapPane;
    private int _health;
    private double _speed;
    private double _positionX;
    private double _positionY;
    private boolean _isExploding = false;
    double tileSize = 40;
    private ImageView _image = new ImageView(new Image("Enemy.png"));
    protected Rectangle healthBar;
    protected double maxHealthWidth = 20;
    private int maxHealth;
    private Circle circle = new Circle(1,Color.TRANSPARENT);
    ImageView image; //= new ImageView("Enemy.png"); //Image of the Enemy

    Enemy(Pane pane, int initialHealth, double initialSpeed, MapPane mapPane)
    {
        _pane = pane;
        _speed = initialSpeed;
        this.maxHealth = _health = initialHealth;
        _image.setFitHeight(20);
        _image.setFitWidth(20);
        _mapPane = mapPane;
        healthBar = new Rectangle(maxHealthWidth,3.5,Color.GREEN);
        healthBar.setTranslateX(-15);
        circle.setStroke(Color.TRANSPARENT);
    }

    Enemy(Pane pane, String imageName, int initialHealth, double initialSpeed, MapPane mapPane)
    {
        _pane = pane;
        _speed = initialSpeed;
        this.maxHealth = _health = initialHealth;
        _image = new ImageView(new Image(imageName));
        _image.setFitHeight(20);
        _image.setFitWidth(20);
        _mapPane = mapPane;
        healthBar = new Rectangle(maxHealthWidth,3.5,Color.RED);
        healthBar.setTranslateX(-15);
        circle.setStroke(Color.TRANSPARENT);
    }

    PathTransition pt;
    SequentialTransition st;


    public void walk(int level) throws Exception {

        TextDecoder textDecoder = new TextDecoder(new File("level" + level + ".txt"));
        ArrayList<Cell> coordinates = textDecoder.getGrayCells();

        Path path;
        double durationPerTile = 1/_speed;

        double startX = coordinates.getFirst().x * tileSize + tileSize / 2.0;
        double startY = coordinates.getFirst().y * tileSize + tileSize / 2.0;

        if (level == 1) {
            startX -= 90;
        } else if (level == 2) {
            startX -= 10;
            startY -= 80;
        } else if (level == 3) {
            startX -= 210;
        } else if (level == 4) {
            startX -= 90;
        } else if (level == 5) {
            startX -= 10;
            startY -= 120;
        } else {
            throw new Exception("Invalid level number.");
        }
        circle.setCenterX(startX);
        circle.setCenterY(startY);
        image.setTranslateX(startX - image.getFitWidth()/2);
        image.setTranslateY(startY - image.getFitHeight()/2);


        _pane.getChildren().add(circle);
        _pane.getChildren().add(image);
        _pane.getChildren().add(healthBar);

        path = new Path();
        //Initial path coordinates
        path.getElements().add(new MoveTo(coordinates.getFirst().y *tileSize + tileSize/2.0, coordinates.getFirst().x*tileSize + tileSize/2.0));

        pt = new PathTransition();
        //Using this for multiple animations
        st = new SequentialTransition();

        for (int i = 0; i < coordinates.size(); i++) {
            //Drawing the line that following indicated coordinates
            path.getElements().add(new LineTo(coordinates.get(i).y * tileSize + tileSize/2.0, coordinates.get(i).x * tileSize + tileSize/2.0));
        }

        double totalDuration = coordinates.size() * durationPerTile;
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.1)); //Delay


        pt.setPath(path);
        pt.setNode(circle);
        pt.jumpTo(Duration.ZERO);
        pt.setDuration(javafx.util.Duration.seconds(totalDuration));
        pt.setInterpolator(Interpolator.LINEAR);

        st.getChildren().addAll(pauseTransition, pt);
        st.setCycleCount(1);
        st.play();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double x = circle.getCenterX() + circle.getTranslateX();
                double y = circle.getCenterY() + circle.getTranslateY();
                setPosition(x,y);
                image.setTranslateX(x - image.getFitWidth() / 2);
                image.setTranslateY(y - image.getFitHeight() / 2);
                healthBar.setTranslateX(x - 10);
                healthBar.setTranslateY(y - 35);
            }
        };
        timer.start();
        st.setOnFinished(event -> {
            Platform.runLater(() -> {
                _mapPane.lives--;
                if(_mapPane.lives >= 1)
                    _mapPane.heartImage.setImage(new Image("heart"+_mapPane.lives+".png"));
                if (_mapPane.lives <= 0 && !_mapPane.isGameOver) {
                    _mapPane.isGameOver = true;
                    Map.sequentialTransition.stop();
                    _mapPane.waveTimeline.stop();
                    activeTowers.clear();
                    GameOverMenu gameOverMenu = new GameOverMenu();
                    try {
                        gameOverMenu.show(StageManager.currentStage);
                        activeEnemies.clear();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            // Düşmanı sahneden kaldır
            _pane.getChildren().remove(circle);
            _pane.getChildren().remove(healthBar);
            _pane.getChildren().remove(image);
            Map.activeEnemies.remove(this);
        });
    }

    public void explode() {

        Platform.runLater(() -> {
            _mapPane.money += this.getClass() == TankEnemy.class ? 20 : 10;
            _mapPane.moneyLabel.setText("Money: " + _mapPane.money + "$");
        });

        // 1. Patlama noktasını al (tüm transform'ları hesaba katar)
        double explodeX = circle.getTranslateX() + circle.getCenterX();
        double explodeY = circle.getTranslateY() + circle.getCenterY();

        // 2. Shockwave
        Circle shockwave = new Circle(0, 0, 1);
        shockwave.setTranslateX(explodeX);
        shockwave.setTranslateY(explodeY);
        shockwave.setFill(Color.DARKORANGE);
        shockwave.setStroke(Color.DARKORANGE);
        shockwave.setStrokeWidth(1);

        // 3. Partiküller
        int particleCount = 20;
        Circle[] particles = new Circle[particleCount];
        for (int i = 0; i < particleCount; i++) {
            Circle p = new Circle(0, 0, 2.25, randomColor());
            p.setTranslateX(explodeX);
            p.setTranslateY(explodeY);
            p.setOpacity(0);
            particles[i] = p;
        }

        // 4. Shockwave Animasyonu
        ScaleTransition shockScale = new ScaleTransition(Duration.millis(100), shockwave);
        shockScale.setFromX(1); shockScale.setFromY(1);
        shockScale.setToX(30); shockScale.setToY(30);

        FadeTransition shockFade = new FadeTransition(Duration.millis(150), shockwave);
        shockFade.setFromValue(0.8); shockFade.setToValue(0);

        ParallelTransition shockAnim = new ParallelTransition(shockScale, shockFade);

        // 5. Partikül Animasyonları
        ParallelTransition particlesAnim = new ParallelTransition();
        for (Circle p : particles) {
            FadeTransition in = new FadeTransition(Duration.millis(10), p);
            in.setFromValue(0); in.setToValue(1);

            TranslateTransition move = new TranslateTransition(Duration.millis(500), p);
            move.setByX(Math.random() * 120 - 60);
            move.setByY(Math.random() * 120 - 60);

            FadeTransition out = new FadeTransition(Duration.millis(50), p);
            out.setFromValue(1); out.setToValue(0);
            out.setDelay(Duration.millis(30));

            SequentialTransition seq = new SequentialTransition(in, move, out);
            particlesAnim.getChildren().add(seq);
        }

        // 6. Circle'ın Kendi Patlama Animasyonu
        ScaleTransition circleScale = new ScaleTransition(Duration.millis(200), circle);
        circleScale.setFromX(1); circleScale.setFromY(1);
        circleScale.setToX(5); circleScale.setToY(5);

        FadeTransition circleFade = new FadeTransition(Duration.millis(200), circle);
        circleFade.setFromValue(1); circleFade.setToValue(0);

        // 7. Tümünü Birleştir
        ParallelTransition all = new ParallelTransition(shockAnim, particlesAnim, circleScale, circleFade);

        // 8. Sahneye ekle ve bitince temizle
        _pane.getChildren().add(shockwave);
        _pane.getChildren().addAll(particles);
        _pane.getChildren().removeAll(circle,image);

        all.setOnFinished(e -> {
            _pane.getChildren().remove(shockwave);
            _pane.getChildren().removeAll(particles);

            Map.activeEnemies.remove(this);
        });

        all.play();
    }

    public Color randomColor() {
        double r = Math.random();
        if (r < 0.3) return Color.DARKGREEN;
        else if (r < 0.6) return Color.GREEN;
        else return Color.DARKOLIVEGREEN;
    }

    public double getSpeed() {
        return _speed;
    }

    public void setSpeed(int speed) {
        this._speed = speed;
    }

    public int getHealth() {
        return _health;
    }

    public void setHealth(int amount) {
        int initialHealth = this._health;
        this._health += amount;

        if(this._health < initialHealth){
            double percent = Math.max(0, Math.min(1, (double)_health/maxHealth));
            double widthDuringShoot = maxHealthWidth * percent;
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), new KeyValue(healthBar.widthProperty(),widthDuringShoot,Interpolator.EASE_BOTH)));
            timeline.play();
        }
        if(this._health <= 0) {
            PauseTransition delay = new PauseTransition(Duration.millis(200));
            delay.setOnFinished(e -> _pane.getChildren().remove(healthBar));
            delay.play();
        }
    }

    public double getPositionX()
    {
        return _positionX;

    }
    public double getPositionY()
    {
        return _positionY;
    }

    public void setPosition(double x, double y) {
        this._positionX = x;
        this._positionY = y;
    }
    public boolean isExploding(){
        return _isExploding;
    }
    public void setExploding(boolean exploding){
        this._isExploding = exploding;
    }
    public void stopMovement(){
        if(st != null){
            st.stop();
        }
    }
    public Circle getCircle() {
        return circle;
    }
    public MapPane getMapPane() {
        return _mapPane;
    }
    public void setMapPane(MapPane mapPane) {
        _mapPane = mapPane;
    }
}
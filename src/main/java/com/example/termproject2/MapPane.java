package com.example.termproject2;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;

import static com.example.termproject2.Map.activeTowers;

public class MapPane {
    int money;
    int lives;
    String[] rows;
    Label moneyLabel;
    Label livesLabel;
    TextDecoder textDecoder;
    Label waveCountdownLabel;
    int waveCountdownTime;
    int currentWave = 0;

    MapPane(File levelFile) {
        textDecoder = new TextDecoder(levelFile);
        lives = 5;
        money = 2000;
        rows = TextDecoder.getLines(levelFile);
        moneyLabel = new Label("Money: " + money + "$");
        moneyLabel.setFont(new Font("Arial", 20));
        livesLabel = new Label("Lives: " + lives);
        livesLabel.setFont(new Font("Arial", 20));
        int[] waveDelay = textDecoder.waveDelays;
        waveCountdownTime = waveDelay[0];
        waveCountdownLabel = new Label("Next wave in: " + waveCountdownTime + "s");
        waveCountdownLabel.setFont(Font.font("Arial", 18));
        waveCountdownLabel.setTextFill(Paint.valueOf("red"));

        Timeline timeline = getTimeline(waveDelay);
        timeline.play();
    }

    private static Pane overlayPane;

    public static void setOverlayPane(Pane pane) {
        overlayPane = pane;
    }

    public static Pane getOverlayPane() {
        return overlayPane;
    }

    private Timeline getTimeline(int[] waveDelay) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (waveCountdownTime > 0) {
                waveCountdownTime--;
                waveCountdownLabel.setText("Next wave in: " + waveCountdownTime + "s");
            } else {
                waveCountdownLabel.setText("Wave started!");

                if (currentWave < waveDelay.length - 1) {
                    currentWave++;
                    waveCountdownTime = waveDelay[currentWave];
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        return timeline;
    }

    public GridPane getPane() {
        GridPane map = new GridPane();

        ArrayList<Cell> grayCells = textDecoder.getGrayCells();
        ArrayList<Cell> cells = textDecoder.cells;
        int counter = 0;
        for (int i = 0; i < cells.size(); i++) {
            StackPane cell = new StackPane();
            Rectangle rectangle = new Rectangle(40, 40);

            if (cells.get(i).isGray)
            {
                if(counter == 0)
                {
                    Image pathImage = new Image("cave_entrance.png");
                    rectangle.setFill(new ImagePattern(pathImage));
                    cell.getChildren().add(rectangle);
                    counter++;
                }
                else
                {
                    Image pathImage = new Image("sand_template.jpg");
                    rectangle.setFill(new ImagePattern(pathImage));
                    cell.getChildren().add(rectangle);
                }
            }
            else
            {
                rectangle.setFill(Color.web("#597110",0));
                cell.getChildren().add(rectangle);

                rectangle.setOnDragOver(event ->
                {
                    //This method will make sure that the dragged object is moving and the original one stays
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    //This method makes sure that event that take place properly ends and does not put itself into a loop
                    event.consume();
                });
                int finalI = i;
                rectangle.setOnDragDropped(event ->
                {
                    int x = cells.get(finalI).x;
                    int y = cells.get(finalI).y;
                    //Sets cells size to be 40 to prevent strain on the Map
                    cell.setMinSize(40, 40);
                    cell.setPrefSize(40, 40);
                    cell.setMaxSize(40, 40);
                    Node node = getNodeAt(map, x, y);

                    if (node instanceof StackPane stackPane) {
                        if (stackPane.getChildren().size() > 1) {
                            event.consume();
                        }
                    }
                    //Get the content of the dragged object
                    Dragboard db = event.getDragboard();

                    if (db.hasString()) {
                        String data = db.getString();
                        String[] parts = data.split(";");
                        String towerType = parts[0];
                        String imagePath = parts[1];
                        int cost = Integer.parseInt(parts[2]);
                        int levelOfCastle = Integer.parseInt(parts[3]);

                        if (money >= cost) {
                            money -= cost;
                            moneyLabel.setText("Money: " + money + "$");
                            //Gets the image of the castle and puts on the cell on the map
                            ImageView castleImage = new ImageView(new Image(imagePath));
                            castleImage.setFitWidth(32);
                            castleImage.setFitHeight(60);
                            cell.getChildren().add(castleImage);

                            Tower newTower = getTower(towerType, imagePath, cost, levelOfCastle);

                            int columnIndex = GridPane.getColumnIndex(cell);
                            int rowIndex = GridPane.getRowIndex(cell);
                            newTower.setPosition(columnIndex * 40 + 20, rowIndex * 40 + 20);
                            activeTowers.add(newTower);
                            newTower.setParentCell(cell);
                            newTower.shoot();

                            for (Tower tower : activeTowers) {
                                StackPane stackPane = tower.getParentCell();
                                stackPane.setOnDragDetected(event1 ->
                                {
                                    Dragboard dragboard = castleImage.startDragAndDrop(TransferMode.MOVE);
                                    ClipboardContent content = new ClipboardContent();
                                    money += tower.getPrice();
                                    moneyLabel.setText("Money: " + money + "$");
                                    int level = tower.getLevel();

                                    content.putString(tower.getClass().getSimpleName() + ";" + tower.getImage().getImage().getUrl() + ";" + tower.getPrice() + ";" + level);
                                    dragboard.setContent(content);

                                    Circle range = new Circle(tower.getRange());
                                    range.setFill(Color.RED.deriveColor(0, 1, 1, 0.5));
                                    range.setStroke(Color.RED);
                                    range.setStrokeWidth(3);

                                    ImageView smallView = new ImageView(castleImage.getImage());
                                    smallView.setFitWidth(32);
                                    smallView.setFitHeight(32);

                                    StackPane visual = new StackPane(range, smallView);
                                    SnapshotParameters params = new SnapshotParameters();
                                    params.setFill(Color.TRANSPARENT);
                                    Image image = visual.snapshot(params, null);

                                    dragboard.setDragView(image, image.getWidth() / 2, image.getHeight() / 2);
                                    tower.delete();
                                    activeTowers.remove(tower);
                                    stackPane.getChildren().remove(castleImage);
                                    event.consume();
                                });

                                castleImage.setOnMouseClicked(e -> {
                                    if (tower.getPrice() * 2 <= money && tower.getLevel() < 3) {
                                        int level = tower.getLevel();
                                        System.out.println(level);
                                        money -= tower.getPrice() * 2;
                                        moneyLabel.setText("Money: " + money + "$");
                                        tower.levelUp();
                                        PauseTransition delay = new PauseTransition(Duration.millis(200));
                                        delay.play();
                                        castleImage.setImage(getLevelUpImage(level, tower));

                                        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), castleImage);
                                        fadeTransition.setFromValue(0);
                                        fadeTransition.setToValue(1);

                                        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), castleImage);
                                        scaleTransition.setFromX(0.7);
                                        scaleTransition.setFromY(0.7);
                                        scaleTransition.setToX(1);
                                        scaleTransition.setToY(1);
                                        new ParallelTransition(fadeTransition, scaleTransition).play();

                                        System.out.println("Tower upgraded");
                                    }
                                });
                            }
                            event.setDropCompleted(true);
                        } else {
                            event.setDropCompleted(false);
                        }
                    } else {
                        event.setDropCompleted(false);
                    }
                    event.consume();
                });
            }
            map.add(cell, cells.get(i).y, cells.get(i).x);
            playFadeAnimation(cell, cells.get(i).y, cells.get(i).x);
        }
        //Gets rid of the lines between cells
        map.setHgap(0);
        map.setVgap(0);
        return map;
    }

    private static Tower getTower(String towerType, String imagePath, int cost, int level) {

        if (towerType.equals("SingleShotTower")) {
            imagePath = "SingleShotTower" + level + ".png";
            Tower tower = new SingleShotTower(imagePath, cost, 200, 150);
            for (int i = 1; i < level; i++) {
                tower.levelUp();
            }
            return tower;
        } else if (towerType.equals("LaserTower")) {
            imagePath = "LaserTower" + level + ".png";
            Tower tower = new LaserTower(imagePath, cost, 40, 150);
            for (int i = 1; i < level; i++) {
                tower.levelUp();
            }
            return tower;
        } else if (towerType.equals("TripleShotTower")) {
            imagePath = "TripleShotTower" + level + ".png";
            Tower tower = new TripleShotTower(imagePath, cost, 200, 120);
            for (int i = 1; i < level; i++) {
                tower.levelUp();
            }
            return tower;
        } else {
            imagePath = "MissileLauncherTower" + level + ".png";
            Tower tower = new MissileLauncherTower(imagePath, cost, 300, 150);
            for (int i = 1; i < level; i++) {
                tower.levelUp();
            }
            return tower;
        }
    }

    private void playFadeAnimation(Node node, int row, int column) {
        /*First sets the node to be invisible then by a duration sets the node to be visible grade by grade. And the setDelay method
        will make sure that animation will start from top most left corner of the map to the bottom most right corner of the map*/
        node.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(300), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setDelay(Duration.millis((row + column) * 50));
        fade.play();
    }

    public StackPane returnCastle(Tower tower, Color color) {
        Rectangle background = new Rectangle(250, 120);
        background.setFill(color);
        background.setStroke(Color.BLACK);
        background.setArcHeight(10);
        background.setArcWidth(10);

        ImageView castleImage = tower.getImage();
        castleImage.setFitHeight(60);
        castleImage.setFitWidth(32);

        Label nameLabel = new Label(tower.getName());
        Label costLabel = new Label("Cost: " + tower.getPrice() + "$");

        VBox labelBox = new VBox(nameLabel, costLabel);
        labelBox.setAlignment(Pos.CENTER);
        labelBox.setSpacing(2);

        VBox contentBox = new VBox(castleImage, labelBox);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setSpacing(5);

        StackPane pane = new StackPane(background, contentBox);

        pane.setOnDragDetected(event -> {
            Dragboard dragboard = castleImage.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.putString(tower.getClass().getSimpleName() + ";" + tower.getImage().getImage().getUrl() + ";" + tower.getPrice() + ";" + 1);
            dragboard.setContent(content);

            Circle range = new Circle(tower.getRange());
            range.setFill(Color.RED.deriveColor(0, 1, 1, 0.5));
            range.setStroke(Color.RED);
            range.setStrokeWidth(3);

            ImageView smallView = new ImageView(castleImage.getImage());
            smallView.setFitWidth(32);
            smallView.setFitHeight(60);

            StackPane visual = new StackPane(range, smallView);
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            Image image = visual.snapshot(params, null);

            dragboard.setDragView(image, image.getWidth() / 2, image.getHeight() / 2);
            event.consume();
        });

        return pane;
    }

    //Creates the right pane of the map
    public VBox returnRightPane() {
        StackPane castle1 = returnCastle(new SingleShotTower("SingleShotTower1.png", 50, 300, 150), Color.WHEAT);
        StackPane castle2 = returnCastle(new LaserTower("LaserTower1.png", 120, 30, 180), Color.WHEAT);
        StackPane castle3 = returnCastle(new TripleShotTower("TripleShotTower1.png", 150, 300, 150), Color.WHEAT);
        StackPane castle4 = returnCastle(new MissileLauncherTower("MissileLauncherTower1.png", 400, 500, 200), Color.WHEAT);

        VBox rightPane = new VBox(livesLabel, moneyLabel, waveCountdownLabel, castle1, castle2, castle3, castle4);
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setSpacing(5);

        return rightPane;
    }

    public Node getNodeAt(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            Integer column = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);

            if (column == null) column = 0;
            if (rowIndex == null) rowIndex = 0;

            if (column == col && rowIndex == row) {
                return node;
            }
        }
        return null;
    }

    public Image getLevelUpImage(int level, Tower tower) {
        if (level == 2) {
            return switch (tower) {
                case SingleShotTower singleShotTower -> new Image("SingleShotTower2.png");
                case LaserTower laserTower -> new Image("LaserTower2.png");
                case TripleShotTower tripleShotTower -> new Image("TripleShotTower2.png");
                case null, default -> new Image("MissileLauncherTower2.png");
            };
        } else {
            return switch (tower) {
                case SingleShotTower singleShotTower -> new Image("SingleShotTower3.png");
                case LaserTower laserTower -> new Image("LaserTower3.png");
                case TripleShotTower tripleShotTower -> new Image("TripleShotTowerTower3.png");
                case null, default -> new Image("MissileLauncherTower3.png");
            };
        }
    }

}
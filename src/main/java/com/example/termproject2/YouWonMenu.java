package com.example.termproject2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class YouWonMenu extends Application {

    private int nextLevel;

    public YouWonMenu(int nextLevel) {
        this.nextLevel = nextLevel;
    }

    Button bt;
    Scene scene;
    @Override
    public void start(Stage primaryStage) {

        Text text1 = new Text("You Won");
        text1.setFill(Color.WHITE);
        text1.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 30));

        bt = new Button("Continue to Next Level");
        bt.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 15));
        bt.setStyle("-fx-background-color: #F44336; -fx-text-fill: #FFFFFF; "+ "-fx-background-radius: 10; -fx-padding: 20 30 20 30;");
        bt.setTranslateY(30);

        VBox vb = new VBox(text1, bt);
        vb.setAlignment(Pos.CENTER);
        vb.setBackground(new Background(new BackgroundFill(Color.DARKORANGE, CornerRadii.EMPTY,null)));

        bt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    new Map(nextLevel).start(new Stage());
                    ((Stage) bt.getScene().getWindow()).close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        scene = new Scene(vb,800,600);
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }


}

package com.example.termproject2;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;

//150123038 Deniz Arda Şanal
public class Walking_Deprecated extends Application
{
    private int[][] coordinates1 =
    {
            {2,0},{2,1},{2,2},{2,3},
            {3,3},{4,3},{5,3},
            {5,4},{5,5},{5,6},{5,7},{5,8},{5,9}
    };
    private int[][] coordinates2 =
    {
            {0,2}, {1,2}, {2,2}, {3,2},
            {3,3}, {3,4}, {3,5},
            {2,5}, {1,5},
            {1,6}, {1,7},
            {2,7}, {3,7}, {4,7}, {5,7}, {6,7}, {7,7},
            {7,8}, {7,9}
    };
    private int[][] coordinates3 =
    {
            {5,0}, {5,1}, {5,2},
            {4,2}, {3,2}, {3,3}, {3,4},
            {4,4}, {5,4}, {6,4}, {7,4},
            {7,5}, {7,6}, {7,7},
            {6,7}, {5,7}, {4,7},
            {4,8}, {4,9}
    };
    private int[][] coordinates4 =
    {
            {2,0}, {2,1}, {2,2}, {2,3},
            {3,3}, {4,3}, {5,3}, {6,3}, {7,3}, {8,3},
            {8,4}, {8,5}, {8,6},
            {7,6}, {6,6}, {5,6}, {4,6},
            {4,7}, {4,8}, {4,9},
            {5,9}, {6,9}, {7,9}, {8,9}, {9,9}, {10,9},
            {10,8}, {10,7}, {10,6},
            {11,6}, {12,6}, {12,7}, {12,8}, {12,9},
            {12,10}, {12,11}, {12,12},
            {11,12}, {10,12}, {9,12}, {8,12},
            {8,13}, {8,14}
    };
    private int[][] coordinates5 =
    {
            {0,3}, {1,3}, {2,3}, {3,3},
            {3,2}, {3,1},
            {4,1}, {5,1}, {6,1},
            {6,2}, {6,3}, {6,4}, {6,5}, {6,6},
            {7,6}, {8,6}, {8,5}, {8,4},
            {9,4}, {10,4}, {10,5}, {10,6}, {10,7}, {10,8}, {10,9},
            {9,9}, {8,9}, {7,9}, {6,9}, {5,9}, {4,9}, {3,9},
            {3,10}, {3,11}, {3,12},
            {4,12}, {5,12}, {6,12}, {7,12}, {8,12}, {9,12},
            {10,12}, {11,12}, {12,12},
            {12,13}, {12,14}
    };

    Path path;
    Pane pane1;

    private double durationPerTile = 0.2;
    private final int WIDTH = 10;
    private final int HEIGHT = 10;

    public void start(Stage primaryStage) throws Exception
    {
        pane1 = new Pane();
        moveAnimation(4,pane1);
        Scene scene = new Scene(pane1,WIDTH*80,HEIGHT*80);
        primaryStage.setTitle("Path Animation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Method for calling the desired level
    public void moveAnimation(int num, Pane pane) throws Exception
    {
        Circle circle = new Circle(10, Color.RED);

        if(num == 1)
        {
            path = new Path();
            path.getElements().add(new MoveTo(coordinates1[0][1]*50, coordinates1[0][0]*50));

            //Using this for combining multiple animations
            SequentialTransition st = new SequentialTransition();

            for(int i = 0; i < coordinates1.length; i++ ) {
                path.getElements().add(new LineTo(coordinates1[i][1]*50, coordinates1[i][0]*50));
            }
            circle.setCenterX(coordinates1[0][1]);
            circle.setCenterY(coordinates1[0][0]);
            double totalDuration = coordinates1.length * durationPerTile;
            PathTransition pt = new PathTransition();

            pt.setPath(path);
            pt.setNode(circle);
            pt.jumpTo(Duration.ZERO);
            pt.setDuration(javafx.util.Duration.seconds(totalDuration)); // adjusting speed and time
            pt.setInterpolator(Interpolator.LINEAR);

            pane.getChildren().add(circle);
            st.getChildren().add(pt);
            st.setCycleCount(1);
            st.play();
        }
        else if(num == 2)
        {
            path = new Path();
            path.getElements().add(new MoveTo(coordinates2[0][1]*50, coordinates2[0][0]*50));

            SequentialTransition st = new SequentialTransition();
            for(int i = 0; i < coordinates2.length; i++ ) {
                path.getElements().add(new LineTo(coordinates2[i][1]*50, coordinates2[i][0]*50));
            }
            circle.setCenterX(coordinates1[0][1]);
            circle.setCenterY(coordinates1[0][0]);
            double totalDuration = coordinates2.length * durationPerTile;
            PathTransition pt = new PathTransition();

            pt.setPath(path);
            pt.setNode(circle);
            pt.jumpTo(Duration.ZERO);
            pt.setDuration(javafx.util.Duration.seconds(totalDuration));
            pt.setInterpolator(Interpolator.LINEAR);

            pane.getChildren().add(circle);
            st.getChildren().add(pt);
            st.setCycleCount(1);
            st.play();
        }
        else if(num == 3)
        {
            path = new Path();
            path.getElements().add(new MoveTo(coordinates3[0][1]*50, coordinates3[0][0]*50));

            //Using this for multiple animations
            SequentialTransition st = new SequentialTransition();
            for(int i = 0; i < coordinates3.length; i++ ) {
                path.getElements().add(new LineTo(coordinates3[i][1]*50, coordinates3[i][0]*50));
            }
            circle.setCenterX(coordinates1[0][1]);
            circle.setCenterY(coordinates1[0][0]);
            double totalDuration = coordinates3.length * durationPerTile;
            PathTransition pt = new PathTransition();

            pt.setPath(path);
            pt.setNode(circle);
            pt.jumpTo(Duration.ZERO);
            pt.setDuration(javafx.util.Duration.seconds(totalDuration));
            pt.setInterpolator(Interpolator.LINEAR);

            pane.getChildren().add(circle);
            st.getChildren().add(pt);
            st.setCycleCount(1);
            st.play();
        }
        else if(num == 4)
        {

            path = new Path();
            path.getElements().add(new MoveTo(coordinates4[0][1]*50, coordinates4[0][0]*50));

            //Using this for multiple animations
            SequentialTransition st = new SequentialTransition();
            for(int i = 0; i < coordinates4.length; i++ ) {
                path.getElements().add(new LineTo(coordinates4[i][1]*50, coordinates4[i][0]*50));
            }
            circle.setCenterX(coordinates1[0][1]);
            circle.setCenterY(coordinates1[0][0]);
            double totalDuration = coordinates4.length * durationPerTile;
            PathTransition pt = new PathTransition();

            pt.setPath(path);
            pt.setNode(circle);
            pt.jumpTo(Duration.ZERO);
            pt.setDuration(javafx.util.Duration.seconds(totalDuration));
            pt.setInterpolator(Interpolator.LINEAR);

            pane.getChildren().add(circle);
            st.getChildren().add(pt);
            st.setCycleCount(1);
            st.play();
        }
        else if(num == 5)
        {

            path = new Path();
            path.getElements().add(new MoveTo(coordinates5[0][1]*50, coordinates5[0][0]*50));

            //Using this for multiple animations
            SequentialTransition st = new SequentialTransition();
            for(int i = 0; i < coordinates5.length; i++ ) {
                path.getElements().add(new LineTo(coordinates5[i][1]*50, coordinates5[i][0]*50));
            }
            circle.setCenterX(coordinates1[0][1]);
            circle.setCenterY(coordinates1[0][0]);
            double totalDuration = coordinates5.length * durationPerTile;
            PathTransition pt = new PathTransition();

            pt.setPath(path);
            pt.setNode(circle);
            pt.jumpTo(Duration.ZERO);
            pt.setDuration(javafx.util.Duration.seconds(totalDuration));
            pt.setInterpolator(Interpolator.LINEAR);

            pane.getChildren().add(circle);
            st.getChildren().add(pt);
            st.setCycleCount(1);
            st.play();
        }
        else
        {
            throw new Exception("Invalid level number.");
        }
    }


    public static void main(String[] args)
    {
        launch(args);
    }


}

package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Main extends Application {

    public static final int WIDTH = 800, HEIGHT = 600;
    public static Simulation sim;
    public static double gravite = 1e-1;
    public boolean gravityOn =false;
    public double radiusOfBalls = 20;
    public double mouseClickedTimer;
    public Point2D.Double mouseClickedPos;
    public final double mouseSpeed = 0.1;



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Pane root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        GraphicsContext context = canvas.getGraphicsContext2D();

        context.setFill(Color.RED);

        sim = new Simulation(primaryStage,0);



        scene.setOnKeyPressed((key)->{
            if(key.getCode() == KeyCode.SPACE){
                if(gravityOn){
                    sim.changeGravity(0);
                }else{
                    sim.changeGravity(gravite);
                }
                gravityOn= !gravityOn;
            } else if (key.getCode() == KeyCode.ESCAPE){
                sim.reset();
            }
        });

        scene.setOnScroll((event)-> {
            this.radiusOfBalls += event.getDeltaY()/event.getMultiplierY();
        });
        scene.setOnMousePressed((event)->{
            mouseClickedTimer= System.nanoTime();
            mouseClickedPos = new Point2D.Double(event.getX(),event.getY());

        });
        scene.setOnMouseReleased((event -> {
            double dt = (System.nanoTime()-mouseClickedTimer)*1e-9;

            double vx = (event.getX()-mouseClickedPos.x)/dt;
            double vy = (event.getY()-mouseClickedPos.y)/dt;

            sim.addBallWithVelocity(event.getX(), event.getY(),radiusOfBalls,vx*mouseSpeed,vy*mouseSpeed);



        }));


        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) * 1e-9;
                context.clearRect(0, 0, WIDTH, HEIGHT);
                sim.update(deltaTime);

                sim.drawAll(context);
                lastTime = now;
            }
        };
        timer.start();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

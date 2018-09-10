package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import java.util.HashSet;
import java.awt.geom.Point2D;


public class Simulation {
    private HashSet<Entity> entities;
    private Main vue;
    private double gravite;
    private Stage stage;

    public Simulation(Stage stage, double gravite){
        this.gravite = gravite; // constante gravitationnelle
        this.stage = stage;
        this.entities = new HashSet<Entity>();



    }
    public void addBall(double x, double y, double radius){
        Balle b = new Balle(x,y,radius);
        boolean intersects = false;
        if(entities.size() ==0){
            entities.add(b);
        } else{
            for(Entity other : this.entities){
                if(b.intersects(other)){
                    intersects = true;
                    break;
                }
            }
            if(!intersects){
                entities.add(b);
            }
        }

    }
    public void drawAll(GraphicsContext context){
        if(entities.size() !=0){
            for(Entity e : entities){
                e.draw(context);
            }
        }
    }

    public void update(double dt) {
        for(Entity e : entities){
            double forceX = 0;
            double forceY = 0;
            for(Entity other : entities){

                if(other != e){
                    double distanceSq = Point2D.distanceSq(e.getX(),e.getY(),other.getX(),other.getY());
                    forceX += ((gravite*e.getArea()*other.getArea())/distanceSq)*(other.getX()-e.getX());
                    forceY += ((gravite*e.getArea()*other.getArea())/distanceSq)*(other.getY()-e.getY());
                    e.setAcc(new Point2D.Double(forceX/e.getArea(), forceY/e.getArea()));
                }
                e.update(dt);
            }
        }
        for(Entity e : entities){
            for(Entity other : entities){

                if(other != e){
                    e.testCollision(other);
                }

            }
        }

    }
    public void changeGravity(double newG){

        gravite = newG;

    }
    public void reset(){
        entities.clear();
    }
    public void addBallWithVelocity(double x, double y, double radius, double vx, double vy){
        Balle b = new Balle(x,y,radius);
        b.vx = vx;
        b.vy = vy;
        boolean intersects = false;
        if(entities.size() ==0){
            entities.add(b);
        } else{
            for(Entity other : this.entities){
                if(b.intersects(other)){
                    intersects = true;
                    break;
                }
            }
            if(!intersects){
                entities.add(b);
            }
        }

    }
}

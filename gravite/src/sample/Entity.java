package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.geom.Point2D;

public abstract class Entity {

    protected double x, y;
    protected double vx, vy;
    protected double ax, ay;
    protected double hue, colorSpeed =0;

    public Entity(double x, double y) {
        this.x = x;
        this.y = y;


        hue = Math.random() * 360;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


    public abstract double getW();

    public abstract double getH();

    public abstract boolean intersects(Entity other);

    public void testCollision(Entity other) {
        if (this.intersects(other)) {
            double vx = this.vx;
            double vy = this.vy;

            this.vx = other.vx*0.95;
            this.vy = other.vy*0.95;

            other.vx = vx*0.95;
            other.vy = vy*0.95;

            pushOut(other);
        }
    }


    public abstract void pushOut(Entity other);

    public void draw(GraphicsContext context) {
        context.setFill(Color.hsb(hue, 0.8, 1));
    }
    
    /**
     * Met à jour la position et la vitesse de la balle
     *
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    public void update(double dt) {

        double oldvx = vx;
        double oldvy = vy;

        vx += dt * ax;
        vy += dt * ay;

        x += ax*dt*dt+oldvx*dt;
        y += ay*dt*dt+oldvy*dt;

        if (x + getW() / 2 > Main.WIDTH || x - getW() / 2 < 0) {
            vx *= -0.9;
        }
        if (y + getH() / 2 > Main.HEIGHT || y - getH() / 2 < 0) {
            vy *= -0.9;
        }

        x = Math.min(x, Main.WIDTH - getW() / 2);
        x = Math.max(x, getW() / 2);

        y = Math.min(y, Main.HEIGHT - getH() / 2);
        y = Math.max(y, getH() / 2);
        
        hue = (hue + dt * colorSpeed) % 360;
    }
    public void setAcc(Point2D.Double p){

        this.ax = p.x;
        this.ay = p.y;

    }
    public abstract double getArea();

}

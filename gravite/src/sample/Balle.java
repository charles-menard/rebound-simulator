package sample;

import javafx.scene.canvas.GraphicsContext;

public class Balle extends Entity {

    private double r;

    public Balle(double x, double y, double r) {
        super(x, y);
        this.r = r;
    }

    /**
     * Indique s'il y a intersection entre les deux balles
     *
     * @param other
     * @return true s'il y a intersection
     */
    public boolean intersects(Balle other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double d2 = dx * dx + dy * dy;

        return d2 < (this.r + other.r) * (this.r + other.r);
    }

    /**
     * @see https://yal.cc/rectangle-circle-intersection-test/
     */
    public boolean intersects(Carre other) {
        /**
         * Trouve le point (x, y) à l'intérieur du carré le plus proche du
         * centre du cercle et vérifie s'il se trouve dans le rayon du cercle
         */
        double deltaX = x - Math.max(
                other.getX() - other.getW() / 2,
                Math.min(x, other.getX() + other.getW() / 2));
        double deltaY = y - Math.max(
                other.getY() - other.getH() / 2,
                Math.min(y, other.getY() + other.getW() / 2));

        return deltaX * deltaX + deltaY * deltaY < r * r;
    }

    /**
     * Déplace les deux balles en intersection pour retrouver un déplacement
     * minimal
     *
     * @param other
     */
    public void pushOut(Balle other) {
        // Calculer la quantité qui overlap en X, same en Y
        // Déplacer les deux de ces quantités/2
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        double d2 = dx * dx + dy * dy;
        double d = Math.sqrt(d2);

        // Overlap en pixels
        double overlap = d - (this.r + other.r);

        // Direction dans laquelle se déplacer (normalisée)
        double directionX = dx / d;
        double directionY = dy / d;

        double deplacementX = directionX * overlap / 2;
        double deplacementY = directionY * overlap / 2;

        this.x += deplacementX;
        this.y += deplacementY;
        other.x -= deplacementX;
        other.y -= deplacementY;
    }

    public void pushOut(Carre other) {
        double deltaX = x - Math.max(
                other.getX() - other.getW() / 2,
                Math.min(x, other.getX() + other.getW() / 2));
        double deltaY = y - Math.max(
                other.getY() - other.getH() / 2,
                Math.min(y, other.getY() + other.getW() / 2));

        double overlap = r - Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double dx = other.x - this.x;
        double dy = other.y - this.y;

        double d = Math.sqrt(dx * dx + dy * dy);

        // Direction dans laquelle se déplacer (normalisée)
        double directionX = dx / d;
        double directionY = dy / d;

        double deplacementX = directionX * overlap / 2;
        double deplacementY = directionY * overlap / 2;

        this.x -= deplacementX;
        this.y -= deplacementY;
        other.x += deplacementX;
        other.y += deplacementY;
    }

    @Override
    public void pushOut(Entity other) {
        if (other instanceof Balle) {
            pushOut((Balle) other);
        } else {
            pushOut((Carre) other);
        }
    }

    @Override
    public double getW() {
        return 2 * r;
    }

    @Override
    public double getH() {
        return 2 * r;
    }

    @Override
    public boolean intersects(Entity other) {
        if (other instanceof Balle) {
            return intersects((Balle) other);
        } else {
            return intersects((Carre) other);
        }
    }

    @Override
    public void draw(GraphicsContext context) {
        super.draw(context);
        context.fillOval(
                this.x - this.r,
                this.y - this.r,
                2 * this.r,
                2 * this.r);
    }
    @Override
    public double getArea(){
        return Math.PI*Math.pow(this.r, 2);
    }

}

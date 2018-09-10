package sample;

import javafx.scene.canvas.GraphicsContext;

public class Carre extends Entity {

    private double largeur;

    public Carre(double x, double y, double largeur) {
        super(x, y);
        this.largeur = largeur;
    }

    @Override
    public double getW() {
        return largeur;
    }

    @Override
    public double getH() {
        return largeur;
    }

    /**
     * Indique si les deux carrés s'intersectent. Deux carrés ne sont *pas* en
     * intersection si : 1) S'il y en a un qui est au-dessus de l'autre 2) S'il
     * y en a un à gauche de l'autre Sinon, les deux overlappent
     *
     * @see https://stackoverflow.com/a/32088787
     * @param other L'autre carré
     * @return true si les carrés s'intersectent
     */
    public boolean intersects(Carre other) {
        return !( // Un des carrés est à gauche de l'autre
                x + largeur / 2 < other.x - other.largeur / 2
                || other.x + other.largeur / 2 < this.x - this.largeur / 2
                // Un des carrés est en haut de l'autre
                || y + largeur / 2 < other.y - other.largeur / 2
                || other.y + other.largeur / 2 < this.y - largeur / 2);
    }

    @Override
    public boolean intersects(Entity other) {

        if (other instanceof Balle) {
            return ((Balle) other).intersects(this);
        } else if (other instanceof Carre) {
            return this.intersects((Carre) other);
        }

        return false;
    }

    @Override
    public void draw(GraphicsContext context) {
        super.draw(context);
        context.fillRect(this.x - this.largeur / 2,
                this.y - this.largeur / 2,
                this.largeur, this.largeur);
    }

    @Override
    public void pushOut(Entity other) {
        if (other instanceof Balle) {
            ((Balle) other).pushOut(this);
        } else {
            pushOut((Carre) other);
        }
    }

    public void pushOut(Carre other) {
        double dx = x - other.x;
        double dy = y - other.y;

        double distX = (largeur + other.largeur) / 2 - Math.abs(dx);
        double distY = (largeur + other.largeur) / 2 - Math.abs(dy);

        if (Math.abs(distX) < Math.abs(distY)) {
            distX *= (x > other.x ? -1 : 1);
            distY = 0;
        } else {
            distY *= (y > other.y ? -1 : 1);
            distX = 0;
        }

        this.x -= distX / 2;
        this.y -= distY / 2;
        other.x += distX / 2;
        other.y += distY / 2;
    }
    @Override
    public double getArea(){ return largeur*largeur;}
}

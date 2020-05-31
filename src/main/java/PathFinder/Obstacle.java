package PathFinder;

import impl.Point;

import java.awt.*;

public class Obstacle {

    private int xStart;
    private int xEnd;
    private int yStart;
    private int yEnd;


    public Obstacle(Rectangle r) {
        this.xStart = (int) r.getX();
        this.xEnd = (int) r.getX() + (int) r.getWidth();
        this.yStart = (int) r.getY();
        this.yEnd = (int) r.getY() + (int) r.getHeight();
    }

    public boolean colision(Point p) {
        if (this.xStart >= p.getX() && this.xEnd <= p.getX()) {
            if (this.yStart >= p.getY() || this.yEnd <= p.getY()) {
                return true;
            }

        }
        return false;
    }

    @Override
    public String toString() {
        return "Obstacle{" +
                "xStart=" + xStart +
                ", xEnd=" + xEnd +
                ", yStart=" + yStart +
                ", yEnd=" + yEnd +
                '}';
    }
}

package PathFinder;

import interf.IUIConfiguration;
import maps.Maps;
import viewer.PathViewer;
import impl.Point;
import interf.IPoint;

import java.awt.*;
import java.util.*;
import java.awt.geom.Line2D;
import java.util.List;

import maps.Maps;


public class Cromossoma implements Comparable<Cromossoma> {

    private Point start;
    private Point end;
    private List<Point> path;
    private final int maxPathSize = 20;
    private int maxMapHeight;
    private int maxMapWidth;
    public List<Rectangle> rectangles;

    public static IUIConfiguration conf;

    static {
        try {
            conf = Maps.getMap(6);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Cromossoma(Point start, Point end, int maxMapHeight, int maxMapWidth) {
        this.start = start;
        this.end = end;
        this.path = new ArrayList<>(this.maxPathSize);
        this.maxMapHeight = maxMapHeight;
        this.maxMapWidth = maxMapWidth;
        this.putSomePoints();
        rectangles = conf.getObstacles();
    }

    public Cromossoma mutate(Cromossoma other) {

        return null;
    }

    public Cromossoma[] cross(Cromossoma other) {

        int thisPathListLength = this.path.size();
        int otherPathListLength = other.path.size();

        Cromossoma thisCromossoma = new Cromossoma(this);

        int i = 0;

        while (i < thisPathListLength / 2) {
            this.path.set(i, other.path.get(i));
            i++;
        }

        int j = 0;

        while(j < otherPathListLength/2) {
            other.path.set(i, thisCromossoma.path.get(i));
            j++;
        }

        int k = thisPathListLength;

        while(k < this.path.size() && k < other.path.size()){
            this.path.set(k, other.path.get(k));
            k++;
        }

        int z = otherPathListLength;

        while(z < other.path.size() && z < thisCromossoma.path.size()){
            other.path.set(z, thisCromossoma.path.get(z));
            z++;
        }

        Cromossoma[] cromossomas = new Cromossoma[2];
        cromossomas[0] = other;
        cromossomas[1] = thisCromossoma;

        return cromossomas;
    }

    public Cromossoma(Cromossoma other) {
        this.start = new Point(other.start.getX(), other.start.getY());
        this.end = new Point(other.end.getX(), other.end.getY());
        this.path = new LinkedList<>();

        for (Point p : other.path) {
            this.path.add(new Point(p.getX(), p.getY())); //deep copy
        }
        this.maxMapHeight = other.maxMapHeight;
        this.maxMapWidth = other.maxMapWidth;
        this.rectangles = other.rectangles;
    }

    public int getFitness() {
        return 0;
    }

    private int randomNum(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    private void putSomePoints() {

        int x = randomNum(1, 18);
        this.path.add(new Point(this.start.getX(), this.start.getY()));
        for (int i = 0; i < x; i++) {
            path.add(new Point(randomNum(0, this.maxMapWidth), randomNum(0, this.maxMapHeight)));
        }
        this.path.add(new Point(this.end.getX(), this.end.getY()));
    }


    @Override
    public int compareTo(Cromossoma cro) {
        if (cro.getFitness() < this.getFitness()) {
            return 1;
        } else if (cro.getFitness() > this.getFitness()) {
            return -1;
        } else {
            return 0;
        }
    }

    private boolean checkColision(int startX, int startY, int endX, int endY) {
        Line2D line = new Line2D.Double(startX, startY, endX, endY);

        for (Rectangle r : this.rectangles) {
            if (line.intersects(r)) {
                return true;
            }
        }
        return false;
    }

    private int checkALLColisions() {

        Iterator it = this.path.iterator();
        Point current = (Point) it.next();
        Point next;
        int count = 0;

        while (it.hasNext()) {
            next = current;
            current = (Point) it.next();
            if (checkColision(current.getX(), current.getY(), next.getX(), next.getY())) {
                count++;
            }
        }

        return count;
    }

    //pode ser adicionado ao checkALLColisions e altera lo para fitness
    private double distanceBetween(int startX, int startY, int endX, int endY) {
        return Math.sqrt(Math.pow(startX - endX, 2) + Math.pow(startY - endY, 2));
    }

    @Override
    public String toString() {
        return "Cromossoma{" +
                "start=" + start +
                ", end=" + end +
                ", path=" + path +
                '}';
    }
}

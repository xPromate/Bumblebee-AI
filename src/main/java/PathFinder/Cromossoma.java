package PathFinder;

import interf.IUIConfiguration;
import maps.Maps;
import impl.Point;
import interf.IPoint;

import java.awt.*;
import java.util.*;
import java.awt.geom.Line2D;
import java.util.List;

public class Cromossoma implements Comparable<Cromossoma> {

    private int maxMapHeight;
    private int maxMapWidth;
    private Point start;
    private Point end;
    private List<IPoint> path;
    private List<Rectangle> obstacles;
    private final int maxPathSize = 4;
    public static IUIConfiguration conf;

    static {
        try {
            conf = Maps.getMap(6);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Cromossoma() {
        this.maxMapHeight = conf.getHeight();
        this.maxMapWidth = conf.getWidth();
        this.start = (Point) conf.getStart();
        this.end = (Point) conf.getEnd();
        this.obstacles = conf.getObstacles();
        this.path = new ArrayList<>();
        this.addPoints();
    }

    public Cromossoma(Cromossoma other) {
        this.start = (Point) conf.getStart();
        this.end = (Point) conf.getEnd();
        this.path = new ArrayList<>();

        for (IPoint p : other.path) {
            this.path.add(new Point(p.getX(), p.getY())); //deep copy
        }

        this.maxMapHeight = other.maxMapHeight;
        this.maxMapWidth = other.maxMapWidth;
        this.obstacles = conf.getObstacles();
    }

    public Cromossoma(int maxSize) {
        this.maxMapHeight = conf.getHeight();
        this.maxMapWidth = conf.getWidth();
        this.start = (Point) conf.getStart();
        this.end = (Point) conf.getEnd();
        this.obstacles = conf.getObstacles();
        this.path = new ArrayList<>(maxSize);
    }

    private void addPoints() {
        int x = this.randomNumber(1, this.maxPathSize);
        this.path.add(this.start);

        for (int i = 0; i < x; i++) {
            this.path.add(new Point(this.randomNumber(0, this.maxMapWidth), this.randomNumber(0, this.maxMapHeight)));
        }

        this.path.add(this.end);
    }

    public Cromossoma mutate() {
        Cromossoma newC = new Cromossoma(this.path.size());
        newC.path.add(this.start);

        int i = 0;

        while (true) {
            Point p = new Point(this.randomNumber(0, this.maxMapWidth), this.randomNumber(0, this.maxMapHeight));

            if (!checkCollision(newC.path.get(i).getX(), newC.path.get(i).getY(), p.getX(), p.getY())) {
                newC.path.add(p);
                i++;
            }

            if (!checkCollision(newC.path.get(i).getX(), newC.path.get(i).getY(), newC.end.getX(), newC.end.getY())) {
                newC.path.add(this.end);
                break;
            }
        }

        return newC;
    }

    public Cromossoma[] cross(Cromossoma other) {
        Cromossoma child1 = new Cromossoma();
        Cromossoma child2 = new Cromossoma();

        int minSize = Math.min(this.path.size(), other.path.size());
        int maxSize = Math.max(this.path.size(), other.path.size());

        for (int i = 0; i < minSize - 1; i++) {
            child1.path.add(new Point((this.path.get(i).getX() + other.path.get(i).getX()) / 2, (this.path.get(i).getY() + other.path.get(i).getY()) / 2));
        }

        int random = this.randomNumber(1, minSize-1);

        if(minSize == this.path.size()){
            for(int i = 0; i < random ; i++){
                child2.path.add(new Point(this.path.get(i).getX(),this.path.get(i).getY()));
            }

            for(int i = random ; i < maxSize ; i++ ){
                child2.path.add(new Point(other.path.get(i).getX(),other.path.get(i).getY()));
            }
        }else{
            for(int i = 0; i < random ; i++){
                child2.path.add(new Point(other.path.get(i).getX(),other.path.get(i).getY()));
            }

            for(int i = random ; i < maxSize ; i++ ){
                child2.path.add(new Point(this.path.get(i).getX(),this.path.get(i).getY()));
            }
        }

        
        child1.path.add(conf.getEnd());
        child2.path.add(conf.getEnd());

        return new Cromossoma[]{child1, child2};
    }

    private boolean checkCollision(int startX, int startY, int endX, int endY) {
        Line2D line = new Line2D.Double(startX, startY, endX, endY);

        for (Rectangle r : this.obstacles) {
            if (line.intersects(r)) {
                return true;
            }
        }

        return false;
    }

    private int countCollisions(int startX, int startY, int endX, int endY) {
        Line2D line = new Line2D.Double(startX, startY, endX, endY);
        int count = 0;

        for (Rectangle r : this.obstacles) {
            if (line.intersects(r)) {
                count++;
            }
        }

        return count;
    }

    public double fitnessOfOne(Cromossoma c){
        Iterator<IPoint> it = c.path.iterator();
        Point current = (Point) it.next();
        Point next;
        boolean collided = false;
        double distance = 0;

        while (it.hasNext()) {
            next = current;
            current = (Point) it.next();

            distance += distanceBetween(current.getX(), current.getY(), next.getX(), next.getY());

            if (checkCollision(current.getX(), current.getY(), next.getX(), next.getY())) {
                collided = true;
            }
        }

        if (collided) {
            return Double.MAX_VALUE;
        } else {
            return distance;
        }
    }

    public double getFitness() {
        Iterator<IPoint> it = this.path.iterator();
        Point current = (Point) it.next();
        Point next;
        double distance = 0;
        int count = 0;

        while (it.hasNext()) {
            next = current;
            current = (Point) it.next();

            distance += distanceBetween(current.getX(), current.getY(), next.getX(), next.getY());

            count += countCollisions(current.getX(), current.getY(), next.getX(), next.getY());
        }

        return distance + 100000*count;
    }

    private double distanceBetween(int startX, int startY, int endX, int endY) {
        return Math.sqrt(Math.pow(startX - endX, 2) + Math.pow(startY - endY, 2));
    }

    private int randomNumber(int min, int max) {
        Random rand = new Random();

        return rand.nextInt((max - min) + 1) + min;
    }

    public List<IPoint> getPath() {
        return path;
    }

    @Override
    public int compareTo(Cromossoma o) {
        if (o.getFitness() < this.getFitness()) {
            return 1;
        } else if (o.getFitness() > this.getFitness()) {
            return -1;
        } else {
            return 0;
        }
    }
}

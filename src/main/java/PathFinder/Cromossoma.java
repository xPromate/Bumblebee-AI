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
    private List<IPoint> path;
    private final int maxPathSize = 4;
    private int maxMapHeight;
    private int maxMapWidth;
    public List<Rectangle> rectangles;

    public static IUIConfiguration conf;

    static {
        try {
            conf = Maps.getMap(6);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Cromossoma() {
        this.start = new Point(conf.getStart().getX(), conf.getStart().getY());
        this.end = new Point(conf.getEnd().getX(), conf.getEnd().getY());
        this.path = new ArrayList<>(this.maxPathSize);
        this.maxMapHeight = conf.getHeight();
        this.maxMapWidth = conf.getWidth();
        this.putSomePoints();
        rectangles = conf.getObstacles();
    }

    public Cromossoma(Cromossoma other) {
        this.start = new Point(other.start.getX(), other.start.getY());
        this.end = new Point(other.end.getX(), other.end.getY());
        this.path = new LinkedList<>();

        for (IPoint p : other.path) {
            this.path.add(new Point(p.getX(), p.getY())); //deep copy
        }
        this.maxMapHeight = other.maxMapHeight;
        this.maxMapWidth = other.maxMapWidth;
        this.rectangles = conf.getObstacles();
    }

    public Cromossoma(int maxSize) {
        this.start = new Point(conf.getStart().getX(), conf.getStart().getY());
        this.end = new Point(conf.getEnd().getX(), conf.getEnd().getY());
        this.path = new ArrayList<>(maxSize);
        this.maxMapHeight = conf.getHeight();
        this.maxMapWidth = conf.getWidth();
        rectangles = conf.getObstacles();
    }



    public Cromossoma mutate(){
        Cromossoma newC = new Cromossoma(this.path.size());
        int i = 0;
        boolean foundSolution = false;


        while(checkColision(this.path.get(i).getX(),this.path.get(i).getY(),this.path.get(i+1).getX(),this.path.get(i+1).getY()) == 0){
            if(checkColision(this.path.get(i).getX(),this.path.get(i).getY(),this.end.getX(),this.end.getY()) == 0){
                newC.path.add(this.end);
                foundSolution = true;
                break;
            }else{
                newC.path.add(new Point(this.path.get(i).getX(),this.path.get(i).getY())); //PODE SER O SET E EU TER Q PASSAR O INDEX
                i++;
            }

        }

        if(!foundSolution){
            for(int j = i ; j < this.path.size() -1 ; j++){
                Point p = new Point(randomNum(0, this.maxMapWidth), randomNum(0, this.maxMapHeight));
                if(checkColision(this.path.get(i).getX(),this.path.get(i).getY(),this.end.getX(),this.end.getY()) == 0){
                    newC.path.add(this.end);
                    break;
                }
                if(checkColision(this.path.get(i).getX(),this.path.get(i).getY(),p.getX(),p.getY()) == 0){
                    newC.path.add(p);
                }else{
                    j--;
                }
            }
        }





        //this.path.set(halfC, new Point(randomNum(0, this.maxMapWidth),randomNum(0,this.maxMapHeight)));

        return newC;
    }

    public Cromossoma[] cross(Cromossoma other) {

        Cromossoma child1 = new Cromossoma();
        Cromossoma child2 = new Cromossoma();


        int min = Math.min(this.path.size(), other.path.size());

        for(int i = 0; i < min - 1; i++){
            child1.path.add(new Point((this.path.get(i).getX() + other.path.get(i).getX()) / 2, (this.path.get(i).getY() + other.path.get(i).getY()) / 2));
            //child2.path.add(new Point(this.path.get(i).getX(), other.path.get(i).getY()));
            child2.path.add(new Point((this.path.get(i).getX() + other.path.get(i).getX()) / 2, (this.path.get(i).getY() + other.path.get(i).getY()) / 2));
        }

        child1.path.add(conf.getEnd());
        child2.path.add(conf.getEnd());

        //o segundo cross vai tentar concatnar duas metades :D

        return new Cromossoma[] {child1, child2};
    }




    private int randomNum(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    private void putSomePoints() {

        int x = randomNum(1, this.maxPathSize);
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

    private int checkColision(int startX, int startY, int endX, int endY) {
        Line2D line = new Line2D.Double(startX, startY, endX, endY);
        int count = 0;

        for (Rectangle r : this.rectangles) {
            if (line.intersects(r)) {
                count++;
            }
        }

        return count;
    }

    public double getFitness() {

        Iterator it = this.path.iterator();
        Point current = (Point) it.next();
        Point next;
        int count = 0;
        double distance = 0;

        while (it.hasNext()) {
            next = current;
            current = (Point) it.next();
            
            distance += distanceBetween(current.getX(), current.getY(), next.getX(), next.getY());
            count += checkColision(current.getX(), current.getY(), next.getX(), next.getY());
        }

        System.out.println(count);

        return count*1000000000 + distance + this.path.size()*100000;
    }

    //pode ser adicionado ao checkALLColisions e altera lo para fitness
    private double distanceBetween(int startX, int startY, int endX, int endY) {
        return Math.sqrt(Math.pow(startX - endX, 2) + Math.pow(startY - endY, 2));
    }

    public List<IPoint> getPath() {
        return path;
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

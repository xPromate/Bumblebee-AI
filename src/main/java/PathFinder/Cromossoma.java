package PathFinder;

import interf.IUIConfiguration;
import maps.Maps;
import viewer.PathViewer;
import impl.Point;
import interf.IPoint;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.awt.geom.Line2D;
import maps.Maps;



public class Cromossoma implements Comparable<Cromossoma>{

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

    public Cromossoma(Cromossoma other){
        this.start.setX(other.start.getX());
        this.start.setY(other.start.getY());
        this.end.setX(other.end.getX());
        this.end.setY(other.end.getY());
        for(Point p: other.path){
            this.path.add(new Point(p.getX(),p.getY())); //deep copy
        }
        this.maxMapHeight = other.maxMapHeight;
        this.maxMapWidth = other.maxMapWidth;
    }



    public int getFitness() {
        return 0;
    }

    private int randomNum(int min, int max){
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    private void putSomePoints(){

        int x = randomNum(1,18);
        this.path.add(new Point(this.start.getX(),this.start.getY()));
        for(int i = 0 ; i < x ; i++ ){
            path.add(new Point ( randomNum(0,this.maxMapWidth), randomNum(0,this.maxMapHeight)));
        }
        this.path.add(new Point(this.end.getX(),this.end.getY()));
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

    private boolean checkColision(int startX,int startY, int endX, int endY){
        Line2D line = new Line2D.Double(startX,startY,endX,endY);

        for(Rectangle r : this.rectangles){
            if(line.intersects(r)){
                return true;
            }
        }
        return false;
    }

    private int checkALLColisions(){

        Iterator it = this.path.iterator();
        Point current = (Point) it.next();
        Point next ;
        int count = 0;

        while(it.hasNext()){
            next = current;
            current = (Point) it.next();
            if(checkColision(current.getX(),current.getY(),next.getX(),next.getY())){
                count++;
            }
        }

        return count;
    }


}

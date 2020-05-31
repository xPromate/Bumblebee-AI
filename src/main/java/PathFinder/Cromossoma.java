package PathFinder;

import interf.IUIConfiguration;
import maps.Maps;
import viewer.PathViewer;
import impl.Point;
import interf.IPoint;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;


public class Cromossoma implements Comparable<Cromossoma>{

    private Point start;
    private Point end;
    private List<Point> path;
    private final int maxPathSize = 20;
    private int maxMapHeight;
    private int maxMapWidth;


    public Cromossoma(Point start, Point end, int maxMapHeight, int maxMapWidth) {
        this.start = start;
        this.end = end;
        this.path = new ArrayList<>(this.maxPathSize);
        this.maxMapHeight = maxMapHeight;
        this.maxMapWidth = maxMapWidth;
        this.putSomePoints();
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

    private void putSomePoints(){
        Random r = new Random();
        int x = r.nextInt(18);
        this.path.add(new Point(this.start.getX(),this.start.getY()));
        for(int i = 0 ; i < x ; i++ ){
            path.add(new Point ( r.nextInt(this.maxMapWidth), r.nextInt(this.maxMapHeight) ));
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
}

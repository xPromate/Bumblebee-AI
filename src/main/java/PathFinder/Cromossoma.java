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
    private final int maxPathSize = 20;
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

        for (IPoint p : other.path) {
            this.path.add(new Point(p.getX(), p.getY())); //deep copy
        }
        this.maxMapHeight = other.maxMapHeight;
        this.maxMapWidth = other.maxMapWidth;
        this.rectangles = other.rectangles;
    }

    public int getFitness() {

        int fitness = 0;



        return fitness;
    }

    public Cromossoma mutateRandom(){

        Random r = new Random();
        float random = (float) Math.random();

        if(random <= 0.25){ //muda caminho ao inicio

            int x = randomNum(1,this.path.size());

            this.path.set(0, new Point(this.start.getX(),this.start.getY()));

            for(int i = 1 ; i < x  ; i++ ){
                this.path.add(i, new Point(randomNum(0,this.maxMapHeight),randomNum(0,this.maxMapWidth)));
            }

        } else if (random <= 0.5){ //muda caminho ao fim

            int x = randomNum(1,this.path.size());

            for(int i = x ; i < this.path.size()  ; i++ ){
                this.path.add(i, new Point(randomNum(0,this.maxMapHeight),randomNum(0,this.maxMapWidth)));
            }

            this.path.add(new Point(this.end.getX(),this.end.getY()));

        }else if (random <= 0.75){ //retira caminho no inicio

            int x = randomNum(1, this.path.size()-1);

            this.path.set(0, new Point(this.start.getX(),this.start.getY()));

            for(int  i = 1 ; i < x ; i++){
                this.path.remove(i);
            }

        }else { //retira caminho no fim

            int x = randomNum(1, this.path.size() - 1);

            for (int i = x; i < path.size(); i++) {
                this.path.remove(i);
            }

            this.path.add(new Point(this.end.getX(), this.end.getY()));
        }

        return this;
    }

    public Cromossoma mutateByWorstPiece(){
        int half = (int) Math.ceil(this.path.size() / 2);
        int firstHalf = 0;
        int secondHalf = 0;


        for(int i = 0 ; i < this.path.size() ; i++){
            if( i < half){
                if( checkColision(this.path.get(i).getX(), this.path.get(i).getY(),this.path.get(i+1).getX(), this.path.get(i+1).getY()) ){
                    firstHalf++;
                }
            }else{
                if(!(i == this.path.size()-1)){
                    if( checkColision(this.path.get(i).getX(), this.path.get(i).getY(),this.path.get(i+1).getX(), this.path.get(i+1).getY()) ){
                        secondHalf++;
                    }
                }
            }
        }

        System.out.println(firstHalf + " " + secondHalf );

        if (firstHalf > secondHalf){
            this.path.set(0, new Point(this.start.getX(),this.start.getY()));
            for(int j = 1 ; j < half ; j++){
                this.path.set(j, new Point(this.randomNum(0,this.maxMapWidth),this.randomNum(0,this.maxMapHeight)));
            }
        }else{
            for(int j = half ; j < this.path.size() ; j++){
                this.path.set(j, new Point(this.randomNum(0,this.maxMapWidth),this.randomNum(0,this.maxMapHeight)));
            }
            this.path.add(new Point(this.end.getX(),this.end.getY()));
        }

        return this;
    }

    private int randomNum(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    private void putSomePoints() {

        int x = randomNum(1, 18);
        this.path.add(new Point(this.start.getX(), this.start.getY()));
        for (int i = 0; i < 2; i++) {
            path.add(new Point(randomNum(0, this.maxMapWidth), randomNum(0, this.maxMapHeight)));
        }
        this.path.add(new Point(this.end.getX(), this.end.getY()));

        System.out.println(this.path.toString());
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

    public int checkALLColisions() {

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

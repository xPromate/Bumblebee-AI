package PathFinder;

import interf.IUIConfiguration;
import impl.Path;
import interf.IUIConfiguration;
import maps.Maps;
import viewer.PathViewer;
import impl.Point;
import interf.IPoint;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

    public static IUIConfiguration conf;

    public static void main(String[] args) throws Exception {

        //conf = Maps.getMap(6);

        //PathViewer pv = new PathViewer(conf);

        //Obstacle obs = new Obstacle(conf.getObstacles().get(0));

        //System.out.println(obs.toString());

        //Point p = new Point(109,316);

        //System.out.println(obs.colision(p));

        Point p1 = new Point(100, 300);
        Point p2 = new Point(200, 400);

        Point p3 = new Point(50, 100);
        Point p4 = new Point(100, 150);

        Cromossoma c1 = new Cromossoma(p1, p2, 100, 200);
        Cromossoma c2 = new Cromossoma(p3, p4, 100, 200);

        System.out.println("Path cromossoma 1: " + c1.toString());
        System.out.println("Path cromossoma 2: " + c2.toString());

        c1.cross(c2);

        System.out.println("Path cross cromossoma 1: " + c1.toString());
        System.out.println("Path cross cromossoma 2: " + c2.toString());
    }
}

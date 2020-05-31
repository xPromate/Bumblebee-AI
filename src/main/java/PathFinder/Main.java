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

        conf = Maps.getMap(6);

        PathViewer pv = new PathViewer(conf);



        Obstacle obs = new Obstacle(conf.getObstacles().get(0));

        System.out.println(obs.toString());

        Point p = new Point(109,316);

        System.out.println(obs.colision(p));

    }
}

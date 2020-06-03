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

        AG ag = new AG();
        Cromossoma sol = ag.run();

        System.out.println("Melhor solução encontrada:");
        System.out.println(sol);

        PathViewer pv = new PathViewer(sol.conf);
        pv.paintPath(sol.getPath());
        sol.getFitness();
    }
}

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

        Cromossoma c1 = new Cromossoma();
        //Cromossoma c2 = new Cromossoma();

        PathViewer pv = new PathViewer(c1.conf);

        pv.setFitness(9999);
        pv.setStringPath("(ponto1, ponto2, bla bla bla...)");
        c1.mutateRandom();
        pv.paintPath(c1.getPath());

        System.out.println(c1.checkALLColisions());

        Thread.sleep(5000);


    }
}

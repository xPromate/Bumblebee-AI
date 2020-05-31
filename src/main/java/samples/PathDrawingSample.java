package samples;

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

/**
 * Exemplo que mostra como desenhar um caminho no visualizador.
 */
public class PathDrawingSample
{
    public static IUIConfiguration conf;

    public static void main(String args[]) throws Exception {


        conf = Maps.getMap(6);

        List<IPoint> points = new ArrayList<>();
        points.add(conf.getStart());
        points.add(new Point(280,300));
        //points.add(new Point(250,500));
        points.add(new Point(300,350));
        points.add(conf.getEnd());

        PathViewer pv = new PathViewer(conf);

        //System.out.println(conf.getObstacles());



        System.out.println(conf.getHeight());
        System.out.println(conf.getWidth());

        Iterator it =  conf.getObstacles().iterator();

        while(it.hasNext()){
            System.out.println(it.next());
        }

        //System.out.println(rectangles.get(1).contains(280,300));
        //rectangles.get(1).getLocation();


        pv.setFitness(9999);
        pv.setStringPath("(ponto1, ponto2, bla bla bla...)");

        //quando utilizado dentro de um ciclo permite ir atualizando o desenho e ver o algoritmo a progredir
        //por exemplo: desenhar o melhor caminho de cada geração
        pv.paintPath(points);
        Thread.sleep(2000);
        points.add(new Point(500,500));
        points.add(new Point(600,700));
        pv.paintPath(points);

    }


}

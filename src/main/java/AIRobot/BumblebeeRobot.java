package AIRobot;

import PathFinder.Cromossoma;
import PathFinder.AG;
import com.opencsv.CSVWriter;
import impl.Point;
import impl.UIConfiguration;
import interf.IPoint;
import robocode.*;
import robocode.Robot;
import utils.Utils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BumblebeeRobot extends AdvancedRobot {

    private List<Rectangle> obstacles;
    private List<IPoint> points;
    private List<BulletData> fireData;
    private HashMap<String, Rectangle> inimigos;
    public static UIConfiguration conf;
    private int currentPoint = -1;

    @Override
    public void run() {
        super.run();

        this.fireData = new ArrayList<>();

        obstacles = new ArrayList<>();
        inimigos = new HashMap<>();
        conf = new UIConfiguration((int) getBattleFieldWidth(), (int) getBattleFieldHeight(), obstacles);

        while (true) {
            this.setTurnRadarRight(360);

            //se se está a dirigir para algum ponto
            if (currentPoint >= 0) {
                IPoint ponto = points.get(currentPoint);
                //se já está no ponto ou lá perto...
                if (Utils.getDistance(this, ponto.getX(), ponto.getY()) < 2) {
                    currentPoint++;
                    //se chegou ao fim do caminho
                    if (currentPoint >= points.size())
                        currentPoint = -1;
                }

                advancedRobotGoTo(this, ponto.getX(), ponto.getY());
            }

            this.execute();
        }
    }

    @Override
    public void onMouseClicked(MouseEvent e) {
        super.onMouseClicked(e);

        conf.setStart(new impl.Point((int) this.getX(), (int) this.getY()));
        conf.setEnd(new impl.Point(e.getX(), e.getY()));

        AG ag = new AG();
        Cromossoma sol = ag.run((Point) conf.start, (Point) conf.end, conf.getHeight(), conf.getWidth(), conf.getObstacles());
        List<IPoint> path = sol.getPath();

        System.out.println("BumblebeeAI!!");

        points = new ArrayList<>();
        this.points.addAll(path);
        currentPoint = 0;
    }

    @Override
    public void onPaint(Graphics2D g) {
        super.onPaint(g);

        g.setColor(Color.YELLOW);
        obstacles.stream().forEach(x -> g.drawRect(x.x, x.y, (int) x.getWidth(), (int) x.getHeight()));

        if (points != null) {
            for (int i = 1; i < points.size(); i++)
                drawThickLine(g, points.get(i - 1).getX(), points.get(i - 1).getY(), points.get(i).getX(), points.get(i).getY(), 2, Color.yellow);
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        super.onScannedRobot(event);

        Bullet b = this.fireBullet(3);
        if (b == null)
            System.out.println("Não disparei");
        else {
            System.out.println("Disparei ao " + event.getName());
        }

        System.out.println(b.getVelocity() + " velocidade bala");
        System.out.println("Enemy spotted: " + event.getName());

        Point2D.Double ponto = getEnemyCoordinates(this, event.getBearing(), event.getDistance());
        ponto.x -= this.getWidth() * 2.5 / 2;
        ponto.y -= this.getHeight() * 2.5 / 2;

        Rectangle rect = new Rectangle((int) ponto.x, (int) ponto.y, (int) (this.getWidth() * 2.5), (int) (this.getHeight() * 2.5));

        if (inimigos.containsKey(event.getName())) //se já existe um retângulo deste inimigo
            obstacles.remove(inimigos.get(event.getName()));//remover da lista de retângulos

        obstacles.add(rect);
        inimigos.put(event.getName(), rect);

        //System.out.println("Enemies at:");
        //obstacles.forEach(x -> System.out.println(x));
    }

    @Override
    public void onRobotDeath(RobotDeathEvent event) {
        super.onRobotDeath(event);

        Rectangle rect = inimigos.get(event.getName());
        obstacles.remove(rect);
        inimigos.remove(event.getName());

    }

    public static Point2D.Double getEnemyCoordinates(Robot robot, double bearing, double distance) {
        double angle = Math.toRadians((robot.getHeading() + bearing) % 360);

        return new Point2D.Double((robot.getX() + Math.sin(angle) * distance), (robot.getY() + Math.cos(angle) * distance));
    }

    private void drawThickLine(Graphics g, int x1, int y1, int x2, int y2, int thickness, Color c) {

        g.setColor(c);
        int dX = x2 - x1;
        int dY = y2 - y1;

        double lineLength = Math.sqrt(dX * dX + dY * dY);

        double scale = (double) (thickness) / (2 * lineLength);

        double ddx = -scale * (double) dY;
        double ddy = scale * (double) dX;
        ddx += (ddx > 0) ? 0.5 : -0.5;
        ddy += (ddy > 0) ? 0.5 : -0.5;
        int dx = (int) ddx;
        int dy = (int) ddy;

        int xPoints[] = new int[4];
        int yPoints[] = new int[4];

        xPoints[0] = x1 + dx;
        yPoints[0] = y1 + dy;
        xPoints[1] = x1 - dx;
        yPoints[1] = y1 - dy;
        xPoints[2] = x2 - dx;
        yPoints[2] = y2 - dy;
        xPoints[3] = x2 + dx;
        yPoints[3] = y2 + dy;

        g.fillPolygon(xPoints, yPoints, 4);
    }

    public static void advancedRobotGoTo(AdvancedRobot robot, double x, double y) {
        x -= robot.getX();
        y -= robot.getY();

        double angleToTarget = Math.atan2(x, y);
        double targetAngle = robocode.util.Utils.normalRelativeAngle(angleToTarget - Math.toRadians(robot.getHeading()));
        double distance = Math.hypot(x, y);
        double turnAngle = Math.atan(Math.tan(targetAngle));
        robot.setTurnRight(Math.toDegrees(turnAngle));
        if (targetAngle == turnAngle)
            robot.setAhead(distance);
        else
            robot.setBack(distance);
        robot.execute();
    }

    @Override
    public void onBulletHit(BulletHitEvent event) {
        super.onBulletHit(event);

        this.fireData.add(new BulletData(event.getBullet().getVictim(), 1, 1, event.getBullet().getVelocity(), 1,1, event.getBullet().getPower(), true));
    }

    @Override
    public void onBulletMissed(BulletMissedEvent event) {
        super.onBulletMissed(event);

        this.fireData.add(new BulletData("null", 1, 1, event.getBullet().getVelocity(), 1,1, event.getBullet().getPower(), false));
    }

    @Override
    public void onRoundEnded(RoundEndedEvent event) {
        super.onRoundEnded(event);

        try {
            this.fireDataToCSV();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public void onBattleEnded(BattleEndedEvent event) {
        super.onBattleEnded(event);
    }

    public void fireDataToCSV() throws IOException {

        String path = Paths.get(new File(".").getAbsolutePath()).toAbsolutePath().toString();
        String[] split = path.split("\\.");
        String pathF = split[0] + "/fireData/fire_data.csv";

        try (Writer writer = Files.newBufferedWriter(Paths.get(String.valueOf(new File(pathF))))) {

            CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

            String[] headerRecord = {"robot_name", "distance", "gun_heat", "bullet_velocity", "bearing", "heading", "power_hit", "hit"};

            csvWriter.writeNext(headerRecord);

            for (BulletData b : this.fireData) {
                csvWriter.writeNext(new String[]{
                        b.getRobot_name(),
                        Double.toString(b.getDistance()),
                        Double.toString(b.getGun_heat()),
                        Double.toString(b.getBullet_velocity()),
                        Double.toString(b.getBearing()),
                        Double.toString(b.getHeading()),
                        Double.toString(b.getPower_hit()),
                        Boolean.toString(b.isHit()),
                });
            }
        }
    }
}
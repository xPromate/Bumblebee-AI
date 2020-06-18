package AIRobot;

public class BulletData {

    private String robot_name;
    private double distance;
    private double bearing;
    private double heading;
    private double bullet_power;
    private double x;
    private double y;
    private boolean hit;
    private boolean isMoving;

    public BulletData(String robot_name, double distance, double bearing, double bullet_power, double heading, boolean isMoving) {
        this.robot_name = robot_name;
        this.distance = distance;
        this.bearing = bearing;
        this.heading = heading;
        this.isMoving = isMoving;
        this.bullet_power = bullet_power;
    }

    public String getRobot_name() {
        return robot_name;
    }

    public double getDistance() {
        return distance;
    }

    public double getBearing() {
        return bearing;
    }

    public double getHeading() {
        return heading;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getBullet_power() {
        return bullet_power;
    }

    @Override
    public String toString() {
        return "BulletData{" +
                "robot_name='" + robot_name + '\'' +
                ", distance=" + distance +
                ", bearing=" + bearing +
                ", heading=" + heading +
                ", hit=" + hit +
                '}';
    }
}

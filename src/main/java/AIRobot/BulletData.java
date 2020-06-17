package AIRobot;

public class BulletData {

    private String robot_name;
    private double distance;

    private double bearing;
    private double heading;

    private boolean hit;
    private boolean isMoving;

    public BulletData(String robot_name, double distance, double bearing, double heading, boolean isMoving) {
        this.robot_name = robot_name;
        this.distance = distance;

        this.bearing = bearing;
        this.heading = heading;

        this.isMoving = isMoving;
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

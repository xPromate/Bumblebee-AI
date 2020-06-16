package AIRobot;

public class BulletData {

    private String robot_name;
    private double distance;
    private double bullet_velocity;
    private double bearing;
    private double heading;
    private double power_hit;
    private boolean hit;
    private boolean isMoving;

    public BulletData(String robot_name, double distance, double bullet_velocity, double bearing, double heading, double power_hit, boolean isMoving) {
        this.robot_name = robot_name;
        this.distance = distance;
        this.bullet_velocity = bullet_velocity;
        this.bearing = bearing;
        this.heading = heading;
        this.power_hit = power_hit;
        this.isMoving = isMoving;
    }

    public String getRobot_name() {
        return robot_name;
    }

    public double getDistance() {
        return distance;
    }

    public double getBullet_velocity() {
        return bullet_velocity;
    }

    public double getBearing() {
        return bearing;
    }

    public double getHeading() {
        return heading;
    }

    public double getPower_hit() {
        return power_hit;
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
                ", bullet_velocity=" + bullet_velocity +
                ", bearing=" + bearing +
                ", heading=" + heading +
                ", power_hit=" + power_hit +
                ", hit=" + hit +
                '}';
    }
}

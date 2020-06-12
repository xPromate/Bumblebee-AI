package AIRobot;

public class BulletData {

    private String robot_name;
    private double distance;
    private double gun_heat;
    private double bullet_velocity;
    private double bearing;
    private double heading;
    private double power_hit;
    private boolean hit;

    public BulletData(String robot_name, double distance, double gun_heat, double bullet_velocity, double bearing, double heading, double power_hit, boolean hit) {
        this.robot_name = robot_name;
        this.distance = distance;
        this.gun_heat = gun_heat;
        this.bullet_velocity = bullet_velocity;
        this.bearing = bearing;
        this.heading = heading;
        this.power_hit = power_hit;
        this.hit = hit;
    }

    public String getRobot_name() {
        return robot_name;
    }

    public double getDistance() {
        return distance;
    }

    public double getGun_heat() {
        return gun_heat;
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

    public boolean isHit() {
        return hit;
    }

    @Override
    public String toString() {
        return "BulletData{" +
                "robot_name='" + robot_name + '\'' +
                ", distance=" + distance +
                ", gun_heat=" + gun_heat +
                ", bullet_velocity=" + bullet_velocity +
                ", bearing=" + bearing +
                ", heading=" + heading +
                ", power_hit=" + power_hit +
                ", hit=" + hit +
                '}';
    }
}

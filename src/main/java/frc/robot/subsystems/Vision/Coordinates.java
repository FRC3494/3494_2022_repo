package frc.robot.subsystems.Vision;

import frc.robot.RobotConfig;

public class Coordinates {
    public double x;
    public double y;
    public double z = RobotConfig.Shooter.Aimbot.HubHeightMeters;
    public double range;
    public PointV point;
    public Coordinates( double yaw, double distance){
        this.x = distance * Math.sin(yaw);
        this.y = distance * Math.cos(yaw);
        this.range = distance;
        point = new PointV(x, y, z);
    }
}


package frc.robot.subsystems.Vision;

import java.util.Map;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.RobotConfig;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.ITickable;
import frc.robot.utilities.wpilibdi.DiSubsystem;

public class ComputerVision extends DiSubsystem implements IInitializable, ITickable, IDisposable {
    public static class TargetingCameraProperties {
        public static double Pitch = 0;
        public static double Yaw = 0;
        public static boolean TargetFound = false; 
    }

    NetworkTableEntry tv;
    NetworkTableEntry tx;
    NetworkTableEntry ty;

    public void onInitialize() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

        tv = table.getEntry("tv");
        tx = table.getEntry("tx");
        ty = table.getEntry("ty");
    }

    public void onTick() {
        TargetingCameraProperties.TargetFound = tv.getDouble(0) == 1;
        TargetingCameraProperties.Yaw = tx.getDouble(0.0) * (Math.PI / 180);
        TargetingCameraProperties.Pitch = ty.getDouble(0.0) * (Math.PI / 180);
    }
    
    public static double findShooterPower(double distance){
        double power = 0.0;
        power = getRPMLookUp(distance);
        return power;
    }
    public static double getheight(double distance, double angle, double v0){
        return distance*Math.tan(angle)- (9.81*Math.pow(distance, 2))/(2*Math.pow((v0*Math.cos(angle)), 2)) + RobotConfig.Shooter.VisionSettings.ShooterHieghtMeters;
    }
    public static double getVel(double distance, double angle, double height){
        return distance/Math.cos(angle) * Math.sqrt(-9.81/(2*(height-distance*Math.tan(angle)-RobotConfig.Shooter.VisionSettings.ShooterHieghtMeters)));
    }
    public static double getRPMEquation(double ballVelocity){
        return Math.pow(ballVelocity, 2) + 6*ballVelocity + 4;
    }
    public static double getRPMLookUp(double distance){
        Map.Entry<Double, Double> closeVelLess = Map.entry(0.0, 0.0);
        Map.Entry<Double, Double> closeVelMore = Map.entry(0.0, 0.0);
        //Find the vel in the lookup map of either side of the target Velocity
        for(Map.Entry<Double, Double> set :RobotConfig.Shooter.VisionSettings.velLookUpMap.entrySet()) {
            if(set.getKey() < distance &&  Math.abs(distance-set.getKey()) < Math.abs(distance-closeVelLess.getKey())){
                closeVelLess = set;
            }
            else if(set.getKey() > distance && Math.abs(distance-set.getKey()) < Math.abs(distance-closeVelMore.getKey())){
                closeVelMore = set;
            }
            else if(set.getKey() == distance){
                return set.getValue();
                
            }
        }
        //Using thoose use linear interpolation to estimate what our RPM (set.getValue()) should be
        
        double slope = (closeVelMore.getValue()-closeVelLess.getValue())  /  (closeVelMore.getKey()-closeVelLess.getKey());
        return (distance - closeVelLess.getKey()) * slope + closeVelLess.getValue();
    }
    public static double calculateDistanceToTargetMeters(double targetMetersAboveCamera, double targetPitchRadians) {
        return targetMetersAboveCamera/ Math.tan(targetPitchRadians);
    }

    public void onDispose() {
    }
}

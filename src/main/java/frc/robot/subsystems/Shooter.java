package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.RobotConfig;
import frc.robot.RobotMap;
import frc.robot.subsystems.Vision.Coordinates;
import frc.robot.subsystems.Vision.Line;
import frc.robot.subsystems.Vision.PointV;
import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.ITickable;

public class Shooter extends DiSubsystem implements IInitializable, IDisposable, ITickable {
    private CANSparkMax shooterMotor = new CANSparkMax(RobotMap.Shooter.SHOOTER_MOTOR_CHANNEL, MotorType.kBrushless);
    private SparkMaxPIDController shooterPidController;
    private RelativeEncoder shooterMotorEncoder;
    

    PhotonCamera camera;
    double rotationSpeed;
    double range;
    private double closestDistance = -2;
    List<Coordinates> coords = new ArrayList<Coordinates>();

    private PointV point1;
    private PointV point2;
    private PointV point3;
    private Line line1;
    private Line line2;
    private Line line1Perpendicular;
    private Line line2Perpendicular;
    
    private CANSparkMax turretMotor = new CANSparkMax(RobotMap.Shooter.TURRET_MOTOR_CHANNEL, MotorType.kBrushless);

    private DoubleSolenoid hoodMainSolenoid = new DoubleSolenoid(RobotMap.Pneumatics.SHOOTER_PCM, PneumaticsModuleType.CTREPCM, RobotMap.Shooter.HOOD_SOLENOID_CHANNEL, RobotMap.Shooter.HOOD_SOLENOID_CHANNEL + 1);

    private boolean aimbotEnabled = false;


    public void onInitialize() {
        this.shooterMotor.setIdleMode(IdleMode.kBrake);

        this.shooterMotorEncoder = this.shooterMotor.getEncoder();
        this.shooterPidController = this.shooterMotor.getPIDController();

        this.shooterPidController.setP(RobotConfig.Shooter.PIDF.P);
        this.shooterPidController.setI(RobotConfig.Shooter.PIDF.I);
        this.shooterPidController.setD(RobotConfig.Shooter.PIDF.D);
        this.shooterPidController.setFF(RobotConfig.Shooter.PIDF.FF);
    }

    public void run(double rpm) {
        this.shooterPidController.setReference(rpm, ControlType.kVelocity);
    }

    public void runHood(boolean position) {
        this.hoodMainSolenoid.set((position) ? Value.kForward : Value.kReverse);
    }

    public void runTurret(double power) {
        if (this.aimbotEnabled) return;

        this.turretMotor.set(power);
    }

    public void onDispose() {
        this.run(0);
    }

    public double getRPM() {
        return this.shooterMotorEncoder.getVelocity();
    }

    public void enableAimBot() {
        this.aimbotEnabled = true;
    }

    public void disableAimBot() {
        this.aimbotEnabled = false;
    }

    public void onTick() {
        if (this.aimbotEnabled) {
            // aimbot code here
        }
    }

    public PointV getCircleCenter(){
        var result = camera.getLatestResult();
        if(result.hasTargets()){  
            List<PhotonTrackedTarget> targets = result.getTargets();
            
            if(targets.size() >= 3){
                for(PhotonTrackedTarget target : targets){
                    range = PhotonUtils.calculateDistanceToTargetMeters(
                                    RobotConfig.Shooter.Aimbot.CameraHeightMeters,//CAMERA_HEIGHT_METERS,
                                    RobotConfig.Shooter.Aimbot.HubHeightMeters,
                                    RobotConfig.Shooter.Aimbot.CameraPitchRadians,
                                    target.getPitch());
                    coords.add(new Coordinates(target.getYaw(), range));
                } 
                point1 = coords.get(0).point; // Which three point we take may need to be changed
                point2 = coords.get(-1).point;
                point3 = coords.get((int)(coords.size())).point;
                line1 = new Line(point1, point2);
                line2 = new Line(point2, point3);
                line1Perpendicular = new Line(line1.m, line1.midpoint);
                line2Perpendicular = new Line(line2.m, line2.midpoint);
                return Line.solve(line1Perpendicular, line2Perpendicular);

            }
            else{
                System.out.println("Error in Vision pipeline; does not have enough of the required target points to get an accurate reading. What is currently being returned is the distance from the Camera to the nearest tape + Constant");
                closestDistance = PhotonUtils.calculateDistanceToTargetMeters(RobotConfig.Shooter.Aimbot.CameraHeightMeters, RobotConfig.Shooter.Aimbot.HubHeightMeters, RobotConfig.Shooter.Aimbot.CameraPitchRadians,targets.get(0).getPitch());
                for(PhotonTrackedTarget target : targets){
                    range = PhotonUtils.calculateDistanceToTargetMeters(
                                    RobotConfig.Shooter.Aimbot.CameraHeightMeters,//CAMERA_HEIGHT_METERS,
                                    RobotConfig.Shooter.Aimbot.HubHeightMeters,
                                    RobotConfig.Shooter.Aimbot.CameraPitchRadians,
                                    target.getPitch());
                    if(range < closestDistance){
                        coords.clear();
                        closestDistance = range;
                        coords.add(new Coordinates(target.getYaw(), range));
                    }
                }
                return new PointV(coords.get(0).x+RobotConfig.Shooter.Aimbot.hubCenterConstant, coords.get(0).y);
            }
            //PhotonUtils.calculateDistanceToTargetMeters(cameraHeightMeters, targetHeightMeters, cameraPitchRadians, targetPitchRadians)
            
            
        }
        else{
            System.out.println("Error in Vision Pipeline: cannot find any targets.");
            return new PointV(0, 0);
        }
        
    }
    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Shooter");

        builder.setActuator(true);

        builder.addDoubleProperty("Shooter RPM", this::getRPM, (double value) -> { });

        builder.addBooleanProperty("Hood Up", () -> {
            return this.hoodMainSolenoid.get() == Value.kForward;
        }, (boolean value) -> { });

        builder.addBooleanProperty("Aimbot Enabled", () -> {
            return this.aimbotEnabled;
        }, (boolean value) -> { });
    }
}

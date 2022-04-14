package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import frc.robot.RobotConfig;
import frc.robot.RobotMap;
import frc.robot.sensors.Linebreaker;
import frc.robot.subsystems.Vision.ComputerVision;
import frc.robot.subsystems.Vision.Coordinates;

import frc.robot.utilities.ShooterSetting;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.ITickable;
import frc.robot.utilities.wpilibdi.DiSubsystem;

public class Shooter extends DiSubsystem implements IInitializable, IDisposable, ITickable {
    private CANSparkMax shooterMotor = new CANSparkMax(RobotMap.Shooter.SHOOTER_MOTOR_CHANNEL, MotorType.kBrushless);
    private SparkMaxPIDController shooterPidController;
    private RelativeEncoder shooterMotorEncoder;

    private double distanceFromHub;
    ShooterSetting currentSetting = ShooterSetting.Off;
    
    private CANSparkMax turretMotor = new CANSparkMax(RobotMap.Shooter.TURRET_MOTOR_CHANNEL, MotorType.kBrushless);
    private SparkMaxPIDController turretPidController;
    private RelativeEncoder turretMotorEncoder;

    private Linebreaker zeroLinebreak = new Linebreaker(RobotMap.Shooter.ZERO_LINEBREAK_CHANNEL, true);

    private NetworkTableEntry pitchEntrys = null;
    boolean enableTurret = false;
    double targetPosition = 0;
    double zeroPosition = 0;
    boolean needsZero = true; // CHANGE TO TRUE YOU WALLNUT
    int zeroStage = 0;
    boolean runRelative = false;
    double relativePower = 0;
    boolean runLeft = true;

    private double aimBotVelocity;
    private DoubleSolenoid hoodSolenoid = new DoubleSolenoid(RobotMap.Pneumatics.SHOOTER_PCM, PneumaticsModuleType.CTREPCM, RobotMap.Shooter.HOOD_SOLENOID_CHANNEL, RobotMap.Shooter.HOOD_SOLENOID_CHANNEL + 1);

    private Solenoid ledRing = new Solenoid(RobotMap.Pneumatics.SHOOTER_PCM, PneumaticsModuleType.CTREPCM, RobotMap.Shooter.LIGHTS_CHANNEL);
    
    private boolean aimbotEnabled = false;


    double rotationSpeed;
    double range;
    List<Coordinates> coords = new ArrayList<Coordinates>();


    public void onInitialize() {
        if (pitchEntrys == null) pitchEntrys = 
        Shuffleboard.getTab("SmartDashboard")
        .add("Status", 0.0)
        .withWidget(BuiltInWidgets.kTextView)
        .withSize(3, 1)
        .getEntry();
        //this.shooterMotor.setIdleMode(IdleMode.kBrake);
        this.shooterMotor.setIdleMode(IdleMode.kCoast);

        this.shooterMotorEncoder = this.shooterMotor.getEncoder();
        this.shooterPidController = this.shooterMotor.getPIDController();

        this.shooterPidController.setP(RobotConfig.Shooter.ShooterPIDF.P);
        this.shooterPidController.setI(RobotConfig.Shooter.ShooterPIDF.I);
        this.shooterPidController.setD(RobotConfig.Shooter.ShooterPIDF.D);
        this.shooterPidController.setFF(RobotConfig.Shooter.ShooterPIDF.FF);


        this.turretMotor.setIdleMode(IdleMode.kBrake);
        //this.turretMotor.setSmartCurrentLimit(RobotConfig.Shooter.TURRET_CURRENT_LIMIT);
        
        this.turretMotorEncoder = this.turretMotor.getEncoder();
        this.turretPidController = this.turretMotor.getPIDController();

        this.turretPidController.setP(RobotConfig.Shooter.TurretPIDF.P);
        this.turretPidController.setI(RobotConfig.Shooter.TurretPIDF.I);
        this.turretPidController.setD(RobotConfig.Shooter.TurretPIDF.D);
        this.turretPidController.setFF(RobotConfig.Shooter.TurretPIDF.FF);

        this.turretPidController.setOutputRange(-RobotConfig.Shooter.TURRET_SPEED, RobotConfig.Shooter.TURRET_SPEED);
    }

    public void run(ShooterSetting setting) {
        //if (aimbotEnabled) return;

        this.currentSetting = setting;
        this.turretPidController.setIAccum(0);
    }

    public void enableTurret(boolean enable) {
        this.enableTurret = enable;
    }

    public ShooterSetting getSetting() {
        return this.currentSetting;
    }

    public void stop() {
        this.currentSetting = ShooterSetting.Off;
    }

    public void rezero() {
        this.needsZero = true;
    }

    public void runHood(boolean position) {
        this.hoodSolenoid.set((position) ? Value.kForward : Value.kReverse);
    }

    public boolean getHood() {
        return this.currentSetting.hood;
    }

    public void runTurret(double rotations) {
        if (this.aimbotEnabled) return;

        this.runRelative = false;
        this.targetPosition = rotations;
    }

    public void runTurretRelative(double speed) {
        if (this.aimbotEnabled) return;

        if (speed <= RobotConfig.Shooter.TURRET_SPEED / 2 && speed >= -RobotConfig.Shooter.TURRET_SPEED / 2) {
            this.runRelative = false;

            return;
        }

        this.runRelative = true;
        this.relativePower = speed;
    }

    public void onDispose() {
        this.stop();
    }

    public double getRPM() {
        return this.shooterMotorEncoder.getVelocity();
    }

    public boolean atRPM() {
        return Math.abs(this.getRPM() - this.currentSetting.rpm) <= RobotConfig.Shooter.SHOOTER_RPM_TOLERANCE;
    }

    public void enableAimBot() {
        this.aimbotEnabled = true;

        ledRing.set(true);
    }

    public void disableAimBot() {
        this.aimbotEnabled = false;

        ledRing.set(false);
    }

    public boolean aimbotEnabled() {
        return this.aimbotEnabled;
    }

    public void onTick() {
        //ledRing.set(this.turretMotorEncoder.getVelocity() != 0);
        ledRing.set(true);
        if (this.aimbotEnabled) {
            
            ledRing.set(true);
            //this.targetPosition += ComputerVision.TargetingCameraProperties.Yaw;
            distanceFromHub = ComputerVision.calculateDistanceToTargetMeters
            (RobotConfig.Shooter.VisionSettings.HubHeightMeters-RobotConfig.Shooter.VisionSettings.CAMERA_HEIGHT_METERS,          
            ComputerVision.TargetingCameraProperties.Pitch);
            //the 0.6778625 is half of the diameter of the outer side of the upper hub in meters
            //this gives us distance to center of the hub 
            /*System.out.println("x:" + ComputerVision.TargetingCameraProperties.x + "|| y:" +ComputerVision.TargetingCameraProperties.y);*/
            //this.aimBotVelocity = ComputerVision.findShooterPower(distanceFromHub);
            //System.out.println((distanceFromHub)+"||"+this.aimBotVelocity); 
            //pitchEntrys.setDouble(distanceFromHub);

            //Shouldn't add till tested
            
            //this.hoodSolenoid.set(Value.kForward);
            //this.currentSetting.rpm = this.aimBotVelocity*60/(0.1016*Math.PI);
            if(ComputerVision.TargetingCameraProperties.Yaw > -0.69813 && Math.abs(this.targetPosition -  (this.getTurretRotations() + ComputerVision.TargetingCameraProperties.Yaw/(2*Math.PI))) < RobotConfig.Shooter.Aimbot.turretError ){    
                this.targetPosition = (this.getTurretRotations() + ComputerVision.TargetingCameraProperties.Yaw/(2*Math.PI));   

        
            }
            else{
                if(this.getTurretRotations() ==  RobotConfig.Shooter.FORWARD_SOFT_LIMIT){
                    this.runLeft = false;
                }
                else if (this.getTurretRotations() == RobotConfig.Shooter.REVERSE_SOFT_LIMIT){
                    this.runLeft = true;
                }                
                if(runLeft){
                    this.targetPosition = RobotConfig.Shooter.FORWARD_SOFT_LIMIT;
                }
                else{ this.targetPosition = RobotConfig.Shooter.REVERSE_SOFT_LIMIT;}
                
            }
        }
        if (this.currentSetting.rpm == 0) this.shooterMotor.set(0);//this.shooterPidController.setReference(this.targetRPM, ControlType.k);
        else this.shooterPidController.setReference(this.currentSetting.rpm, ControlType.kVelocity);

        if (this.currentSetting.rpm > 0) this.runHood(this.currentSetting.hood);
        else this.runHood(false);

        //System.out.println(this.needsZero + " | " + this.zeroStage + " | " + this.enableTurret);

        if (!this.enableTurret) { 
            this.turretMotor.set(0);
            return;
        }

        if (this.needsZero) {
            switch (this.zeroStage) {
                case 0: 
                    if (this.zeroLinebreak.Broken()) {
                        this.turretMotor.set(0);
                        this.zeroStage++;
                    } else this.turretMotor.set(RobotConfig.Shooter.TURRET_SPEED / 4);
                    break;
                case 1:
                    if (this.zeroLinebreak.Broken()) {
                        this.turretMotor.set(0);
                        this.zeroStage++;
                    } else this.turretMotor.set(-RobotConfig.Shooter.TURRET_SPEED / 8);
                    break;
                case 2:
                    if (!this.zeroLinebreak.Broken()) {
                        this.turretMotor.set(0);
                        this.zeroStage++;
                    } else this.turretMotor.set(-RobotConfig.Shooter.TURRET_SPEED / 8);
                    break;
                default:
                    this.zeroPosition = this.turretMotorEncoder.getPosition();
                    this.zeroStage = 0;
                    this.needsZero = false;
            }

        } else if (!this.runRelative || this.getTurretRotations() > RobotConfig.Shooter.FORWARD_SOFT_LIMIT + 0.005 || this.getTurretRotations() < RobotConfig.Shooter.REVERSE_SOFT_LIMIT - 0.005) {
            if (this.targetPosition > RobotConfig.Shooter.FORWARD_SOFT_LIMIT){
                this.targetPosition = RobotConfig.Shooter.FORWARD_SOFT_LIMIT;
            } 
            if (this.targetPosition < RobotConfig.Shooter.REVERSE_SOFT_LIMIT){
                this.targetPosition = RobotConfig.Shooter.REVERSE_SOFT_LIMIT;
            }
            this.turretPidController.setReference(this.turretRotationsToMotorRotations(this.targetPosition), ControlType.kPosition);
        } else {
            if (this.targetPosition > RobotConfig.Shooter.FORWARD_SOFT_LIMIT && this.relativePower > 0) {
                this.turretMotor.set(0);
            } else if (this.targetPosition < RobotConfig.Shooter.REVERSE_SOFT_LIMIT && this.relativePower < 0) { 
                this.turretMotor.set(0);
            } else this.turretMotor.set(this.relativePower);

            this.targetPosition = getTurretRotations();
        }
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Shooter");

        builder.setActuator(true);

        builder.addStringProperty("Target Setting", () -> {
            return this.currentSetting.name;
        }, (String value) -> { });

        builder.addDoubleProperty("Shooter RPM", this::getRPM, (double value) -> { });

        builder.addDoubleProperty("Target RPM", () -> {
            return this.currentSetting.rpm;
        }, (double value) -> { });

        builder.addBooleanProperty("Hood Up", () -> {
            return this.hoodSolenoid.get() == Value.kForward;
        }, (boolean value) -> { });

        builder.addBooleanProperty("Aimbot Enabled", () -> {
            return this.aimbotEnabled;
        }, (boolean value) -> { });
    }

    public double turretRotationsToMotorRotations(double rotations) {
        return RobotConfig.Shooter.TURRET_VERSAPLANETARY_RATIO * RobotConfig.Shooter.TURRET_RATIO * rotations + this.zeroPosition;
    }

    public double turretMotorRotationsToRotations(double rotations) {
        return  ((rotations - this.zeroPosition) / RobotConfig.Shooter.TURRET_RATIO) / RobotConfig.Shooter.TURRET_VERSAPLANETARY_RATIO;
    }

    public double getTurretRotations() {
        return this.turretMotorRotationsToRotations(this.turretMotorEncoder.getPosition());
    }
}

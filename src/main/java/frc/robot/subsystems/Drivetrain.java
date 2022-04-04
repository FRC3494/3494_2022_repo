package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXSensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.music.Orchestra;

import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import frc.robot.RobotConfig;
import frc.robot.RobotMap;
import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.ITickable;
import frc.robot.utilities.wpilibdi.DiSubsystem;

public class Drivetrain extends DiSubsystem implements IInitializable, ITickable, IDisposable {
    private WPI_TalonFX leftLeader = new WPI_TalonFX(RobotMap.Drivetrain.LEFT_LEADER_CHANNEL);
    private WPI_TalonFX leftFollower = new WPI_TalonFX(RobotMap.Drivetrain.LEFT_FOLLOWER_CHANNEL);
    private TalonFXSensorCollection leftLeaderSensorCollection = this.leftLeader.getSensorCollection();

    private WPI_TalonFX rightLeader = new WPI_TalonFX(RobotMap.Drivetrain.RIGHT_LEADER_CHANNEL);
    private WPI_TalonFX rightFollower = new WPI_TalonFX(RobotMap.Drivetrain.RIGHT_FOLLOWER_CHANNEL);
    private TalonFXSensorCollection rightLeaderSensorCollection = this.rightLeader.getSensorCollection();

    @Inject
    private NavX navX;

    private Orchestra orchestra = new Orchestra();

    private DifferentialDriveOdometry odometry;
    private DifferentialDriveKinematics kinematics;
    private RamseteController ramseteController;

    double[] speedsAfterTipCorrection;

    //private SlewRateLimiter leftSlewRateLimiter = new SlewRateLimiter(RobotConfig.Drivetrain.SLEW_RATE);
    //private SlewRateLimiter rightSlewRateLimiter = new SlewRateLimiter(RobotConfig.Drivetrain.SLEW_RATE);

    public void onInitialize() {
        this.setBrakes(NeutralMode.Coast);

        this.leftLeader.config_kP(0, RobotConfig.Drivetrain.PIDF.P);
        this.leftLeader.config_kI(0, RobotConfig.Drivetrain.PIDF.I);
        this.leftLeader.config_kD(0, RobotConfig.Drivetrain.PIDF.D);
        this.leftLeader.config_kF(0, RobotConfig.Drivetrain.PIDF.FF);
        
        this.leftFollower.config_kP(0, RobotConfig.Drivetrain.PIDF.P);
        this.leftFollower.config_kI(0, RobotConfig.Drivetrain.PIDF.I);
        this.leftFollower.config_kD(0, RobotConfig.Drivetrain.PIDF.D);
        this.leftFollower.config_kF(0, RobotConfig.Drivetrain.PIDF.FF);
        
        this.rightLeader.config_kP(0, RobotConfig.Drivetrain.PIDF.P);
        this.rightLeader.config_kI(0, RobotConfig.Drivetrain.PIDF.I);
        this.rightLeader.config_kD(0, RobotConfig.Drivetrain.PIDF.D);
        this.rightLeader.config_kF(0, RobotConfig.Drivetrain.PIDF.FF);
        
        this.rightFollower.config_kP(0, RobotConfig.Drivetrain.PIDF.P);
        this.rightFollower.config_kI(0, RobotConfig.Drivetrain.PIDF.I);
        this.rightFollower.config_kD(0, RobotConfig.Drivetrain.PIDF.D);
        this.rightFollower.config_kF(0, RobotConfig.Drivetrain.PIDF.FF);

        this.rightLeader.setInverted(true);
        this.rightFollower.setInverted(true);

        this.leftFollower.follow(this.leftLeader);
        this.rightFollower.follow(this.rightLeader);
        
        this.orchestra.addInstrument(this.leftLeader);
        this.orchestra.addInstrument(this.leftFollower);
        
        this.orchestra.addInstrument(this.rightLeader);
        this.orchestra.addInstrument(this.rightFollower);
        
        this.leftLeaderSensorCollection.setIntegratedSensorPosition(0, 0);
        this.rightLeaderSensorCollection.setIntegratedSensorPosition(0, 0);

        this.odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(this.navX.getYaw()));
        this.kinematics = new DifferentialDriveKinematics(RobotConfig.Drivetrain.TRACK_WIDTH);
        this.ramseteController = new RamseteController(RobotConfig.Drivetrain.RAMSETE_B, RobotConfig.Drivetrain.RAMSETE_ZETA);
    }

    public void setBrakes(NeutralMode neutralMode) {
        this.leftLeader.setNeutralMode(neutralMode);
        this.leftFollower.setNeutralMode(neutralMode);
        this.rightLeader.setNeutralMode(neutralMode);
        this.rightFollower.setNeutralMode(neutralMode);
    }


    public void tankDrive(double leftPower, double rightPower) {
        if (autoNav) return;

        this.tankDrivePrivate(leftPower, rightPower);
    }

    public void arcadeDrive(double forwardPower, double turnPower) {
        if (autoNav) return;

        this.arcadeDrivePrivate(forwardPower, turnPower);
    }

    public void tankDriveWithAntiTip(double leftPower, double rightPower) {
        if (autoNav) return;

        this.tankDriveWithAntiTipPrivate(leftPower, rightPower);
    }

    public void arcadeDriveWithAntiTip(double forwardPower, double turnPower) {
        if (autoNav) return;

        this.arcadeDriveWithAntiTipPrivate(forwardPower, turnPower);
    }


    private void tankDrivePrivate(double leftPower, double rightPower) {
        this.leftLeader.set(ControlMode.PercentOutput, leftPower);
        this.rightLeader.set(ControlMode.PercentOutput, rightPower);
    }

    private void arcadeDrivePrivate(double forwardPower, double turnPower) {
        this.tankDrive(-(forwardPower + turnPower), -(forwardPower - turnPower));
    }

    private void tankDriveWithAntiTipPrivate(double leftPower, double rightPower) {
        this.speedsAfterTipCorrection = this.correctForPitch(leftPower, rightPower);
        this.tankDrive(this.speedsAfterTipCorrection[0], this.speedsAfterTipCorrection[1]);
    }

    private void arcadeDriveWithAntiTipPrivate(double forwardPower, double turnPower) {
        this.tankDriveWithAntiTip(-(forwardPower + turnPower), -(forwardPower - turnPower));
    }

    
    public double[] correctForPitch(double leftPower, double rightPower){
        double[] correctedInputs = {leftPower, rightPower};
        if (Math.abs(navX.getPitch()) < 45 && Math.abs(navX.getPitch()) > RobotConfig.Drivetrain.PITCH_THRESHOLD_DEGREES) {
            //correctionFactor keeps the tilt correction within a certain threshold so it doesn't correct too much

            double correctionOffset = RobotConfig.Drivetrain.CORRECTION_FACTOR * (((navX.getPitch() < 0) ? -Math.pow(-navX.getPitch(), 1.1) : Math.pow(navX.getPitch(), 1.1)) - RobotConfig.Drivetrain.PITCH_THRESHOLD_DEGREES);
            //double correctionOffset = this.pitchDegrees / 10;
            if (Math.abs(navX.getPitch()) < RobotConfig.Drivetrain.PITCH_ALARM_THRESHOLD) {
                correctedInputs[0] += correctionOffset;
                correctedInputs[1] += correctionOffset;
            } else {
                correctedInputs[0] = correctionOffset;
                correctedInputs[1] = correctionOffset;
            }
            normalize(correctedInputs);
        }
        return correctedInputs;
    }

    private void normalize(double[] motorSpeeds) {
        double max = Math.abs(motorSpeeds[0]);
        boolean normFlag = max > 1;

        for (int i = 1; i < motorSpeeds.length; i++) {
            if (Math.abs(motorSpeeds[i]) > max) {
                max = Math.abs(motorSpeeds[i]);
                normFlag = max > 1;
            }
        }

        if (normFlag) {
            for (int i = 0; i < motorSpeeds.length; i++) {
                motorSpeeds[i] /= max;
            }
        }
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType(BuiltInWidgets.kDifferentialDrive.getWidgetName());

        builder.setActuator(true);

        builder.addDoubleProperty("Left Motor Speed", this.leftLeader::get, (double value) -> { });
        builder.addDoubleProperty("Right Motor Speed", this.rightLeader::get, (double value) -> { });
    }

    public void singTheTheme() {
        this.orchestra.loadMusic("mbk.chrp");

        this.orchestra.play();
    }

    public void pauseTheTheme() {
        if (!this.orchestra.isPlaying()) return;

        this.orchestra.pause();
    }

    public boolean isDoneSinging() {
        return !this.orchestra.isPlaying();
    }

    public void onTick() {
        if (DriverStation.isEnabled()) this.setBrakes(NeutralMode.Brake);
        else this.setBrakes(NeutralMode.Coast);

        double leftEncoderTicks = this.leftLeaderSensorCollection.getIntegratedSensorPosition();
        double rightEncoderTicks = this.rightLeaderSensorCollection.getIntegratedSensorPosition();

        this.odometry.update(Rotation2d.fromDegrees(this.navX.getYaw()), this.falcon2Meters(leftEncoderTicks), this.falcon2Meters(rightEncoderTicks));

        //System.out.println("x: " + this.odometry.getPoseMeters().getX() + "\t\ty: " + this.odometry.getPoseMeters().getX() + "\t\tr: " + this.odometry.getPoseMeters().getRotation() + "\t\tnvr: " + this.navX.getYaw());
        //System.out.println("l: " + this.falcon2Meters(leftEncoderTicks) + "\t\tr: " + this.falcon2Meters(rightEncoderTicks));
        //System.out.println("x: " + leftEncoderTicks + "\t\tx': " + this.falcon2Meters(leftEncoderTicks));

        //System.out.println("here: " + this.autoNav);
        if (this.autoNav) {
            if (this.targetPoses.size() == 0) this.autoNav = false;

            ChassisSpeeds chassisSpeeds = this.ramseteController.calculate(this.odometry.getPoseMeters(), this.targetPoses.get(0), RobotConfig.Drivetrain.RAMSETE_LINEAR_VELOCITY, RobotConfig.Drivetrain.RAMSETE_ANGULAR_VELOCITY);
            DifferentialDriveWheelSpeeds wheelSpeeds = this.kinematics.toWheelSpeeds(chassisSpeeds);
            this.velocityTankDrivePrivate(this.baseVelocity2FalconVelocity(wheelSpeeds.leftMetersPerSecond/100), this.baseVelocity2FalconVelocity(wheelSpeeds.rightMetersPerSecond/100));
            if (this.ramseteController.atReference()) this.targetPoses.remove(0);

            //System.out.println(chassisSpeeds);
            //System.out.println(wheelSpeeds);
        }
    }

    public void funnyprint() { //remove
        System.out.println("x: " + this.odometry.getPoseMeters().getX() + "\t\ty: " + this.odometry.getPoseMeters().getX() + "\t\tr: " + this.odometry.getPoseMeters().getRotation() + "\t\tnvr: " + this.navX.getYaw());

    }

    public double falcon2Meters(double encoderTicks) {
        return (encoderTicks / 2048.0) * RobotConfig.Drivetrain.GEAR_RATIO * Math.PI * RobotConfig.Drivetrain.WHEEL_DIAMETER;
    }

    public double baseVelocity2FalconVelocity(double baseVelocity) {
        return ((60.0 / (Math.PI * RobotConfig.Drivetrain.WHEEL_DIAMETER)) * baseVelocity) / RobotConfig.Drivetrain.GEAR_RATIO;
    }

    public void onDispose() {
        this.tankDrive(0, 0);
    }

    boolean autoNav = false;
    List<Pose2d> targetPoses = new ArrayList<>();

    public void goToPose(Pose2d pose) {
        this.targetPoses.add(pose);
        this.autoNav = true;
    }

    public void goToRelativePose(Pose2d pose) {
        this.goToPose(this.odometry.getPoseMeters().plus(new Transform2d(new Pose2d(), pose)));
    }

    public void stopAutoNav() {
        this.targetPoses.clear();
        this.autoNav = false;
    }

    public boolean isAutoNav() {
        return this.autoNav;
    }

    private void velocityTankDrivePrivate(double leftPower, double rightPower) {
        this.leftLeader.set(ControlMode.Velocity, leftPower);
        this.rightLeader.set(ControlMode.Velocity, rightPower);
    }
}

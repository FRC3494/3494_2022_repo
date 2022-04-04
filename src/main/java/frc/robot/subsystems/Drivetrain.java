package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.music.Orchestra;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import frc.robot.RobotConfig;
import frc.robot.RobotMap;
import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.ITickable;
import frc.robot.utilities.wpilibdi.DiSubsystem;

public class Drivetrain extends DiSubsystem implements IInitializable, IDisposable, ITickable {
    private WPI_TalonFX leftLeader = new WPI_TalonFX(RobotMap.Drivetrain.LEFT_LEADER_CHANNEL);
    private WPI_TalonFX leftFollower = new WPI_TalonFX(RobotMap.Drivetrain.LEFT_FOLLOWER_CHANNEL);

    private WPI_TalonFX rightLeader = new WPI_TalonFX(RobotMap.Drivetrain.RIGHT_LEADER_CHANNEL);
    private WPI_TalonFX rightFollower = new WPI_TalonFX(RobotMap.Drivetrain.RIGHT_FOLLOWER_CHANNEL);

    @Inject
    private NavX navX;

    private Orchestra orchestra = new Orchestra();

    double[] speedsAfterTipCorrection;

    //private SlewRateLimiter leftSlewRateLimiter = new SlewRateLimiter(RobotConfig.Drivetrain.SLEW_RATE);
    //private SlewRateLimiter rightSlewRateLimiter = new SlewRateLimiter(RobotConfig.Drivetrain.SLEW_RATE);

    public void onInitialize() {
        this.leftLeader.setNeutralMode(NeutralMode.Brake);
        this.leftFollower.setNeutralMode(NeutralMode.Brake);
        this.rightLeader.setNeutralMode(NeutralMode.Brake);
        this.rightFollower.setNeutralMode(NeutralMode.Brake);
        // this.leftLeader.setNeutralMode(NeutralMode.Coast);
        // this.leftFollower.setNeutralMode(NeutralMode.Coast);
        // this.rightLeader.setNeutralMode(NeutralMode.Coast);
        // this.rightFollower.setNeutralMode(NeutralMode.Coast);

        this.rightLeader.setInverted(true);
        this.rightFollower.setInverted(true);

        this.leftFollower.follow(this.leftLeader);
        this.rightFollower.follow(this.rightLeader);
        
        this.orchestra.addInstrument(this.leftLeader);
        this.orchestra.addInstrument(this.leftFollower);
        
        this.orchestra.addInstrument(this.rightLeader);
        this.orchestra.addInstrument(this.rightFollower);
    }

    public void tankDrive(double leftPower, double rightPower) {
        
        this.speedsAfterTipCorrection = this.correctForPitch(leftPower, rightPower);
        this.leftLeader.set(ControlMode.PercentOutput, this.speedsAfterTipCorrection[0]);
        this.rightLeader.set(ControlMode.PercentOutput, this.speedsAfterTipCorrection[1]);

        //this.leftLeader.set(ControlMode.PercentOutput, leftPower);
        //this.rightLeader.set(ControlMode.PercentOutput, rightPower);
    }

    public void arcadeDrive(double forwardPower, double turnPower) {
        //this.leftLeader.set(ControlMode.PercentOutput, -(forwardPower - turnPower));
        //this.rightLeader.set(ControlMode.PercentOutput, -(forwardPower + turnPower));
        
        this.tankDrive(-(forwardPower + turnPower), -(forwardPower - turnPower));
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

    public void onDispose() {
        this.tankDrive(0, 0);
    }

    @Override
    public void onTick() {
        //System.out.println(navX.getPitch());
        
    }
}

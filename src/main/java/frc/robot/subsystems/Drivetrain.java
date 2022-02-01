package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.music.Orchestra;

import frc.robot.RobotMap;
import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class Drivetrain extends DiSubsystem implements IInitializable, IDisposable {
    private WPI_TalonFX leftLeader = new TalonFX_WPI(RobotMap.Drivetrain.LEFT_LEADER_CHANNEL);
    private WPI_TalonFX leftFollower = new TalonFX_WPI(RobotMap.Drivetrain.LEFT_FOLLOWER_CHANNEL);

    private WPI_TalonFX rightLeader = new TalonFX_WPI(RobotMap.Drivetrain.RIGHT_LEADER_CHANNEL);
    private WPI_TalonFX rightFollower = new TalonFX_WPI(RobotMap.Drivetrain.RIGHT_FOLLOWER_CHANNEL);

    private Orchestra orchestra = new Orchestra();

    public void onInitialize() {
        this.orchestra.addInstrument(this.leftLeader);
        this.orchestra.addInstrument(this.leftFollower);
        
        this.orchestra.addInstrument(this.rightLeader);
        this.orchestra.addInstrument(this.rightFollower);

        this.leftLeader.setNeutralMode(NeutralMode.Brake);
        this.leftFollower.setNeutralMode(NeutralMode.Brake);
        this.rightLeader.setNeutralMode(NeutralMode.Brake);
        this.rightFollower.setNeutralMode(NeutralMode.Brake);

        this.rightLeader.setInverted(true);
        this.rightFollower.setInverted(true);

        this.leftFollower.follow(this.leftLeader);
        this.rightFollower.follow(this.rightLeader);
    }

    public void run(double leftPower, double rightPower) {
        this.leftLeader.set(ControlMode.PercentOutput, leftPower);
        this.rightLeader.set(ControlMode.PercentOutput, rightPower);
    }

    public void singTheTheme() {
        this.orchestra.loadMusic("mbk.chrp");

        this.orchestra.play();
    }

    public void pauseTheTheme() {
        this.orchestra.pause();
    }

    public void onDispose() {
        this.run(0, 0);
    }
}

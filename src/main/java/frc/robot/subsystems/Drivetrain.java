package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.music.Orchestra;

import frc.robot.RobotConfig;
import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class Drivetrain extends DiSubsystem implements IInitializable, IDisposable {
    TalonFX leftMaster = new TalonFX(RobotConfig.Drivetrain.LEFT_LEADER_CHANNEL);
    TalonFX leftSlave = new TalonFX(RobotConfig.Drivetrain.LEFT_FOLLOWER_CHANNEL);

    TalonFX rightMaster = new TalonFX(RobotConfig.Drivetrain.RIGHT_LEADER_CHANNEL);
    TalonFX rightSlave = new TalonFX(RobotConfig.Drivetrain.RIGHT_FOLLOWER_CHANNEL);

    Orchestra orchestra = new Orchestra();

    @Override
    public void onInject() {
        this.orchestra.addInstrument(this.leftMaster);
        this.orchestra.addInstrument(this.leftSlave);
        
        this.orchestra.addInstrument(this.rightMaster);
        this.orchestra.addInstrument(this.rightSlave);

        this.leftMaster.setNeutralMode(NeutralMode.Brake);
        this.leftSlave.setNeutralMode(NeutralMode.Brake);
        this.rightMaster.setNeutralMode(NeutralMode.Brake);
        this.rightSlave.setNeutralMode(NeutralMode.Brake);

        this.rightMaster.setInverted(true);
        this.rightSlave.setInverted(true);

        this.leftSlave.follow(this.leftMaster);
        this.rightSlave.follow(this.rightMaster);
    }

    public void run(double leftPower, double rightPower) {
        this.leftMaster.set(ControlMode.PercentOutput, leftPower);
        this.rightMaster.set(ControlMode.PercentOutput, rightPower);
    }

    public void singTheTheme() {
        this.orchestra.loadMusic("mbk.chrp");

        this.orchestra.play();
    }

    public void pauseTheTheme() {
        this.orchestra.pause();
    }

    @Override
    public void onDispose() {
        this.run(0, 0);
    }
}

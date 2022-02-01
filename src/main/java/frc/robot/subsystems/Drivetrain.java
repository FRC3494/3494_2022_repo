package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.music.Orchestra;

import frc.robot.RobotMap;
import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class Drivetrain extends DiSubsystem implements IInitializable, IDisposable {
    private TalonFX leftDom = new TalonFX(RobotMap.Drivetrain.LEFT_DOM_CHANNEL);
    private TalonFX leftSub = new TalonFX(RobotMap.Drivetrain.LEFT_SUB_CHANNEL);

    private TalonFX rightDom = new TalonFX(RobotMap.Drivetrain.RIGHT_DOM_CHANNEL);
    private TalonFX rightSub = new TalonFX(RobotMap.Drivetrain.RIGHT_SUB_CHANNEL);

    private Orchestra orchestra = new Orchestra();

    public void onInitialize() {
        this.orchestra.addInstrument(this.leftDom);
        this.orchestra.addInstrument(this.leftSub);
        
        this.orchestra.addInstrument(this.rightDom);
        this.orchestra.addInstrument(this.rightSub);

        this.leftDom.setNeutralMode(NeutralMode.Brake);
        this.leftSub.setNeutralMode(NeutralMode.Brake);
        this.rightDom.setNeutralMode(NeutralMode.Brake);
        this.rightSub.setNeutralMode(NeutralMode.Brake);

        this.rightDom.setInverted(true);
        this.rightSub.setInverted(true);

        this.leftSub.follow(this.leftDom);
        this.rightSub.follow(this.rightDom);
    }

    public void run(double leftPower, double rightPower) {
        this.leftDom.set(ControlMode.PercentOutput, leftPower);
        this.rightDom.set(ControlMode.PercentOutput, rightPower);
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

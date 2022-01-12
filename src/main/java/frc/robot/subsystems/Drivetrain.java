package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.music.Orchestra;

import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class Drivetrain extends DiSubsystem implements IInitializable, IDisposable {
    @Inject(id = "drivetrainLeftMaster")
    TalonFX leftMaster;
    @Inject(id = "drivetrainLeftSlave")
    TalonFX leftSlave;

    @Inject(id = "drivetrainRightMaster")
    TalonFX rightMaster;
    @Inject(id = "drivetrainRightSlave")
    TalonFX rightSlave;

    Orchestra orchestra = new Orchestra();

    @Override
    public void Initialize() {
        orchestra.addInstrument(leftMaster);
        orchestra.addInstrument(leftSlave);
        
        orchestra.addInstrument(rightMaster);
        orchestra.addInstrument(rightSlave);

        leftMaster.setNeutralMode(NeutralMode.Brake);
        leftSlave.setNeutralMode(NeutralMode.Brake);
        rightMaster.setNeutralMode(NeutralMode.Brake);
        rightSlave.setNeutralMode(NeutralMode.Brake);

        rightMaster.setInverted(true);
        rightSlave.setInverted(true);

        leftSlave.follow(leftMaster);
        rightSlave.follow(rightMaster);
    }

    public void Tank(double leftPower, double rightPower) {
        leftMaster.set(ControlMode.PercentOutput, leftPower);
        rightMaster.set(ControlMode.PercentOutput, rightPower);
    }

    public void SingTheTheme() {
        orchestra.loadMusic("mbk.chrp");

        orchestra.play();
    }

    public void PauseTheTheme() {
        orchestra.pause();
    }

    @Override
    public void Dispose() {
        Tank(0, 0);
    }
}

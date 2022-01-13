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

    public void Tank(double leftPower, double rightPower) {
        this.leftMaster.set(ControlMode.PercentOutput, leftPower);
        this.rightMaster.set(ControlMode.PercentOutput, rightPower);
    }

    public void SingTheTheme() {
        this.orchestra.loadMusic("mbk.chrp");

        this.orchestra.play();
    }

    public void PauseTheTheme() {
        this.orchestra.pause();
    }

    @Override
    public void Dispose() {
        this.Tank(0, 0);
    }
}

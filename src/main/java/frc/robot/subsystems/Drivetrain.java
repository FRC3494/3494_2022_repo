package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

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

    @Override
    public void Initialize() {
        leftMaster.setNeutralMode(NeutralMode.Brake);
        leftSlave.setNeutralMode(NeutralMode.Brake);
        rightMaster.setNeutralMode(NeutralMode.Brake);
        rightSlave.setNeutralMode(NeutralMode.Brake);

        rightMaster.setInverted(true);
        rightSlave.setInverted(true);

        leftSlave.follow(leftMaster);
        rightSlave.follow(rightMaster);
    }

    public void TankDrive(double leftPower, double rightPower) {
        leftMaster.set(ControlMode.PercentOutput, leftPower);
        rightMaster.set(ControlMode.PercentOutput, rightPower);
    }

    @Override
    public void Dispose() {
        TankDrive(0, 0);
    }
}

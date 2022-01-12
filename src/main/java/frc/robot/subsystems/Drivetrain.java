package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class Drivetrain extends DiSubsystem implements IInitializable, IDisposable {
    @Inject(id = "drivetrainLeftLeader")
    TalonFX leftLeader;
    @Inject(id = "drivetrainLeftFollower")
    TalonFX leftFollower;

    @Inject(id = "drivetrainRightLeader")
    TalonFX rightLeader;
    @Inject(id = "drivetrainRightFollower")
    TalonFX rightFollwer;

    @Override
    public void initialize() {
        this.leftLeader.setNeutralMode(NeutralMode.Brake);
        this.leftFollower.setNeutralMode(NeutralMode.Brake);
        this.rightLeader.setNeutralMode(NeutralMode.Brake);
        this.rightFollwer.setNeutralMode(NeutralMode.Brake);

        this.rightLeader.setInverted(true);
        this.rightFollwer.setInverted(true);

        this.leftFollower.follow(leftLeader);
        this.rightFollwer.follow(rightLeader);
    }

    public void tankDrive(double leftPower, double rightPower) {
        this.leftLeader.set(ControlMode.PercentOutput, leftPower);
        this.rightLeader.set(ControlMode.PercentOutput, rightPower);
    }

    @Override
    public void dispose() {
        this.tankDrive(0, 0);
    }
}

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXSensorCollection;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.robot.RobotConfig;
import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class Shooter extends DiSubsystem implements IInitializable, IDisposable {
    TalonFX shooterMotor = new TalonFX(RobotConfig.Shooter.SHOOTER_MOTOR_CHANNEL);

    TalonFXSensorCollection shooterMotorSensors;

    @Override
    public void onInject() {
        this.shooterMotor.setNeutralMode(NeutralMode.Brake);

        this.shooterMotorSensors = this.shooterMotor.getSensorCollection();
    }

    public void run(double rpm) {
        this.shooterMotor.set(ControlMode.Velocity, this.RPMToAngularVelocity(rpm));
    }

    @Override
    public void onDispose() {
        this.run(0);
    }

    public double RPMToAngularVelocity(double rpm) {
        return rpm * (2048 / 600);
    }

    public double getRPM() {
        return this.shooterMotorSensors.getIntegratedSensorVelocity() * (600 / 2048);
    }

    public void enableAimBot() {
        //enable the aimbot here
    }

    public void disableAimBot() {
        //disable the aimbot here
    }
}

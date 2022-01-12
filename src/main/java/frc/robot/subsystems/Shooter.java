package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXSensorCollection;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class Shooter extends DiSubsystem implements IInitializable, IDisposable {
    @Inject(id = "shooterMotor")
    TalonFX shooterMotor;

    TalonFXSensorCollection shooterMotorSensors;

    @Override
    public void initialize() {
        this.shooterMotor.setNeutralMode(NeutralMode.Brake);

        this.shooterMotorSensors = shooterMotor.getSensorCollection();
    }

    public void shoot(double rpm) {
        this.shooterMotor.set(ControlMode.Velocity, RPMToAngularVelocity(rpm));
    }

    @Override
    public void dispose() {
        this.shoot(0);
    }

    public double RPMToAngularVelocity(double rpm) {
        return rpm * (2048 / 600);
    }

    public double getRPM() {
        return shooterMotorSensors.getIntegratedSensorVelocity() * (600 / 2048);
    }

    public void enableAimBot() {
        
    }

    public void disableAimBot() {
        
    }
}

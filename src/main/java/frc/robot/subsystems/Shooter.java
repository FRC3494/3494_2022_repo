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
    public void Initialize() {
        this.shooterMotor.setNeutralMode(NeutralMode.Brake);

        this.shooterMotorSensors = this.shooterMotor.getSensorCollection();
    }

    public void Run(double rpm) {
        this.shooterMotor.set(ControlMode.Velocity, this.RPMToAngularVelocity(rpm));
    }

    @Override
    public void Dispose() {
        this.Run(0);
    }

    public double RPMToAngularVelocity(double rpm) {
        return rpm * (2048 / 600);
    }

    public double GetRPM() {
        return this.shooterMotorSensors.getIntegratedSensorVelocity() * (600 / 2048);
    }

    public void EnableAimBot() {
        //enable the aimbot here
    }

    public void DisableAimBot() {
        //disable the aimbot here
    }
}

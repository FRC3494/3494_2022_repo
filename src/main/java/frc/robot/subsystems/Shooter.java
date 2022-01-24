package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXSensorCollection;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.robot.RobotMap;
import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.ITickable;

public class Shooter extends DiSubsystem implements IInitializable, IDisposable, ITickable {
    private TalonFX shooterMotor = new TalonFX(RobotMap.Shooter.SHOOTER_MOTOR_CHANNEL);
    
    private TalonSRX hoodMotor = new TalonSRX(RobotMap.Shooter.HOOD_MOTOR_CHANNEL);
    private TalonSRX turretMotor = new TalonSRX(RobotMap.Shooter.TURRET_MOTOR_CHANNEL);

    private TalonFXSensorCollection shooterMotorSensors;

    private boolean aimbotEnabled = false;

    public void onInitialize() {
        this.shooterMotor.setNeutralMode(NeutralMode.Brake);

        this.shooterMotorSensors = this.shooterMotor.getSensorCollection();
    }

    public void run(double rpm) {
        this.shooterMotor.set(ControlMode.Velocity, this.RPMToAngularVelocity(rpm));
    }

    public void runHood(double power) {
        this.hoodMotor.set(ControlMode.PercentOutput, power);
    }

    public void runTurret(double power) {
        if (this.aimbotEnabled) return;

        this.turretMotor.set(ControlMode.PercentOutput, power);
    }

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
        this.aimbotEnabled = true;
    }

    public void disableAimBot() {
        this.aimbotEnabled = false;
    }

    public void onTick() {
        if (this.aimbotEnabled) {
            // aimbot code here
        }
    }
}

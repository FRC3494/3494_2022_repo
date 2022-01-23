package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.robot.RobotConfig;
import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class Intake extends DiSubsystem implements IInitializable, IDisposable {
    TalonSRX frontIntakeMotor = new TalonSRX(RobotConfig.Intake.FRONT_INTAKE_MOTOR_CHANNEL);
    TalonSRX backIntakeMotor = new TalonSRX(RobotConfig.Intake.BACK_INTAKE_MOTOR_CHANNEL);

    @Override
    public void onInject() {
        this.frontIntakeMotor.setNeutralMode(NeutralMode.Brake);
        this.backIntakeMotor.setNeutralMode(NeutralMode.Brake);

        this.backIntakeMotor.follow(this.frontIntakeMotor);
    }

    public void run(double power) {
        // add code for actuation when intake is requested

        this.frontIntakeMotor.set(ControlMode.PercentOutput, power);
    }

    @Override
    public void onDispose() {
        this.run(0);
    }
}
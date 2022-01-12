package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class Intake extends DiSubsystem implements IInitializable, IDisposable {
    @Inject(id = "frontIntakeMotor")
    TalonSRX frontIntakeMotor;
    @Inject(id = "frontIntakeInnerMotor")
    TalonSRX frontIntakeInnerMotor;
    @Inject(id = "backIntakeMotor")
    TalonSRX backIntakeMotor;

    @Override
    public void initialize() {
        this.frontIntakeMotor.setNeutralMode(NeutralMode.Brake);
        this.frontIntakeInnerMotor.setNeutralMode(NeutralMode.Brake);
        this.backIntakeMotor.setNeutralMode(NeutralMode.Brake);

        this.backIntakeMotor.follow(frontIntakeMotor);
        this.frontIntakeInnerMotor.follow(frontIntakeMotor);
    }

    public void run(double power) {
        // add code for actuation when intake is requested

        this.frontIntakeMotor.set(ControlMode.PercentOutput, power);
    }

    @Override
    public void dispose() {
        this.run(0);
    }
}
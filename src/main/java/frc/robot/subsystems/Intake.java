package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.robot.RobotConfig;
import frc.robot.RobotMap;
import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class Intake extends DiSubsystem implements IInitializable, IDisposable {
    private TalonSRX frontIntakeMotor = new TalonSRX(RobotMap.Intake.FRONT_INTAKE_MOTOR_CHANNEL);
    private TalonSRX backIntakeMotor = new TalonSRX(RobotMap.Intake.BACK_INTAKE_MOTOR_CHANNEL);
    
    private TalonSRX frontIntakeDeployMotor = new TalonSRX(RobotMap.Intake.FRONT_INTAKE_DEPLOY_MOTOR_CHANNEL);
    private TalonSRX backIntakeDeployMotor = new TalonSRX(RobotMap.Intake.BACK_INTAKE_DEPLOY_MOTOR_CHANNEL);

    public void onInitialize() {
        this.frontIntakeMotor.setNeutralMode(NeutralMode.Brake);
        this.backIntakeMotor.setNeutralMode(NeutralMode.Brake);

        this.backIntakeMotor.follow(this.frontIntakeMotor);
    }

    public void run(double power) {
        if (power == 0) {
            this.frontIntakeDeployMotor.set(ControlMode.Position, 0);
            this.backIntakeDeployMotor.set(ControlMode.Position, 0);
        } else {
            this.frontIntakeDeployMotor.set(ControlMode.Position, RobotConfig.Intake.FRONT_DEPLOY_ANGLE);
            this.backIntakeDeployMotor.set(ControlMode.Position, RobotConfig.Intake.BACK_DEPLOY_ANGLE);
        }

        this.frontIntakeMotor.set(ControlMode.PercentOutput, power);
    }

    public void onDispose() {
        this.run(0);
    }
}
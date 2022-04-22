package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.RobotMap;
import com.fizzyapple12.javadi.DiInterfaces.IDisposable;
import com.fizzyapple12.javadi.DiInterfaces.IInitializable;
import com.fizzyapple12.wpilibdi.DiSubsystem;

public class Intake extends DiSubsystem implements IInitializable, IDisposable {
    private TalonSRX frontIntakeMotor = new TalonSRX(RobotMap.Intake.FRONT_INTAKE_MOTOR_CHANNEL);
    private TalonSRX backIntakeMotor = new TalonSRX(RobotMap.Intake.BACK_INTAKE_MOTOR_CHANNEL);
    
    private DoubleSolenoid frontIntakeDeploySolenoid = new DoubleSolenoid(RobotMap.Pneumatics.BASE_PCM, PneumaticsModuleType.CTREPCM, RobotMap.Intake.FRONT_INTAKE_DEPLOY_SOLENOID_CHANNEL, RobotMap.Intake.FRONT_INTAKE_DEPLOY_SOLENOID_CHANNEL + 1);
    private DoubleSolenoid backIntakeDeploySolenoid = new DoubleSolenoid(RobotMap.Pneumatics.BASE_PCM, PneumaticsModuleType.CTREPCM, RobotMap.Intake.BACK_INTAKE_DEPLOY_SOLENOID_CHANNEL, RobotMap.Intake.BACK_INTAKE_DEPLOY_SOLENOID_CHANNEL + 1);

    public void onInitialize() {
        this.frontIntakeMotor.setNeutralMode(NeutralMode.Brake);
        this.backIntakeMotor.setNeutralMode(NeutralMode.Brake);

        this.backIntakeMotor.setInverted(true);
    }

    public void run(double frontPower, double backPower) {
        this.frontIntakeDeploySolenoid.set((frontPower == 0) ? Value.kReverse : Value.kForward);
        this.backIntakeDeploySolenoid.set((backPower == 0) ? Value.kReverse : Value.kForward);

        this.frontIntakeMotor.set(ControlMode.PercentOutput, frontPower);
        this.backIntakeMotor.set(ControlMode.PercentOutput, backPower);
    }

    public void setFront(boolean deployed) {
        this.frontIntakeDeploySolenoid.set((deployed) ? Value.kReverse : Value.kForward);
    }

    public void onDispose() {
        this.run(0, 0);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Intake");

        builder.setActuator(true);

        builder.addDoubleProperty("Front Speed", this.frontIntakeMotor::getMotorOutputPercent, (double value) -> { });
        builder.addBooleanProperty("Front Deployed", () -> {
            return this.frontIntakeDeploySolenoid.get() == Value.kForward;
        }, (boolean value) -> { });

        builder.addDoubleProperty("Back Speed", this.backIntakeMotor::getMotorOutputPercent, (double value) -> { });
        builder.addBooleanProperty("Back Deployed", () -> {
            return this.backIntakeDeploySolenoid.get() == Value.kForward;
        }, (boolean value) -> { });
    }
}
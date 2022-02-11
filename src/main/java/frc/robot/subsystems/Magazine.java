package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.util.sendable.SendableBuilder;
import frc.robot.RobotMap;
import frc.robot.sensors.Linebreaker;
import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class Magazine extends DiSubsystem implements IInitializable, IDisposable {
    private TalonSRX leftTreeMotor = new TalonSRX(RobotMap.Magazine.LEFT_TREE_MOTOR_CHANNEL);;
    private Linebreaker leftTreeLinebreak = new Linebreaker(RobotMap.Magazine.LEFT_TREE_LINEBREAK_CHANNEL);
    
    private TalonSRX rightTreeMotor = new TalonSRX(RobotMap.Magazine.RIGHT_TREE_MOTOR_CHANNEL);
    private Linebreaker rightTreeLinebreak = new Linebreaker(RobotMap.Magazine.RIGHT_TREE_LINEBREAK_CHANNEL);
    
    private TalonSRX treeStemMotor = new TalonSRX(RobotMap.Magazine.TREE_STEM_MOTOR_CHANNEL);
    private Linebreaker treeStemLinebreak = new Linebreaker(RobotMap.Magazine.TREE_STEM_LINEBREAK_CHANNEL);

    public void onInitialize() {
        this.leftTreeMotor.setNeutralMode(NeutralMode.Brake);
        this.rightTreeMotor.setNeutralMode(NeutralMode.Brake);
        this.treeStemMotor.setNeutralMode(NeutralMode.Brake);

        this.leftTreeMotor.setInverted(true);
        this.rightTreeMotor.setInverted(true);
        this.treeStemMotor.setInverted(true);
    }

    public void run(double power) {
        if (power <= 0) {
            this.runRaw(power);
            return;
        }

        this.treeStemMotor.set(ControlMode.PercentOutput, power);
        this.leftTreeMotor.set(ControlMode.PercentOutput, (this.treeStemLinebreak.Broken() && this.leftTreeLinebreak.Broken()) ? 0 : power);
        this.rightTreeMotor.set(ControlMode.PercentOutput, (this.treeStemLinebreak.Broken() && this.rightTreeLinebreak.Broken()) ? ((this.leftTreeLinebreak.Broken()) ? -Math.abs(power) : 0) : power);
    }

    public void runRaw(double power) {
        this.treeStemMotor.set(ControlMode.PercentOutput, power);
        this.leftTreeMotor.set(ControlMode.PercentOutput, power);
        this.rightTreeMotor.set(ControlMode.PercentOutput, power);
    }

    public void onDispose() {
        this.runRaw(0);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Magazine");

        builder.setActuator(true);

        builder.addDoubleProperty("Left Tree Speed", this.leftTreeMotor::getMotorOutputPercent, (double value) -> { });
        builder.addBooleanProperty("Left Tree Full", this.leftTreeLinebreak::Broken, (boolean value) -> {});

        builder.addDoubleProperty("Right Tree Speed", this.rightTreeMotor::getMotorOutputPercent, (double value) -> { });
        builder.addBooleanProperty("Right Tree Full", this.rightTreeLinebreak::Broken, (boolean value) -> {});

        builder.addDoubleProperty("Tree Stem Speed", this.treeStemMotor::getMotorOutputPercent, (double value) -> { });
        builder.addBooleanProperty("Tree Stem Full", this.treeStemLinebreak::Broken, (boolean value) -> {});
    }
}

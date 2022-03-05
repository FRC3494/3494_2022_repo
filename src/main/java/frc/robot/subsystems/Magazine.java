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
    private Linebreaker leftTreeLinebreak = new Linebreaker(RobotMap.Magazine.LEFT_TREE_LINEBREAK_CHANNEL, true);
    
    private TalonSRX rightTreeMotor = new TalonSRX(RobotMap.Magazine.RIGHT_TREE_MOTOR_CHANNEL);
    private Linebreaker rightTreeLinebreak = new Linebreaker(RobotMap.Magazine.RIGHT_TREE_LINEBREAK_CHANNEL, true);
    
    private TalonSRX treeStemMotor = new TalonSRX(RobotMap.Magazine.TREE_STEM_MOTOR_CHANNEL);
    private Linebreaker treeStemLinebreak = new Linebreaker(RobotMap.Magazine.TREE_STEM_LINEBREAK_CHANNEL, true);

    public void onInitialize() {
        this.leftTreeMotor.setNeutralMode(NeutralMode.Brake);
        this.rightTreeMotor.setNeutralMode(NeutralMode.Brake);
        this.treeStemMotor.setNeutralMode(NeutralMode.Brake);

        this.leftTreeMotor.setInverted(true);
        this.rightTreeMotor.setInverted(false);
        this.treeStemMotor.setInverted(false);
    }

    public void run(double leftPower, double rightPower, double verticalPower, boolean sendShooter) {
        double leftRawPower = (this.treeStemLinebreak.Broken() && this.leftTreeLinebreak.Broken()) ? 0 : leftPower;
        double rightRawPower = (this.treeStemLinebreak.Broken() && this.rightTreeLinebreak.Broken()) ? ((this.leftTreeLinebreak.Broken()) ? -Math.abs(rightPower) : 0) : rightPower;
        double verticalRawPower = (this.treeStemLinebreak.Broken() && !sendShooter) ? 0 : verticalPower;

        this.runRaw(leftRawPower, rightRawPower, verticalRawPower);
    }

    public void runRaw(double leftPower, double rightPower, double verticalPower) {
        this.leftTreeMotor.set(ControlMode.PercentOutput, leftPower);
        this.rightTreeMotor.set(ControlMode.PercentOutput, rightPower);
        this.treeStemMotor.set(ControlMode.PercentOutput, verticalPower);
    }

    public void onDispose() {
        this.runRaw(0, 0, 0);
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

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.robot.RobotConfig;
import frc.robot.sensors.Linebreaker;
import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class Magazine extends DiSubsystem implements IInitializable, IDisposable {
    TalonSRX leftTreeUpperMotor = new TalonSRX(RobotConfig.Magazine.LEFT_TREE_UPPER_MOTOR_CHANNEL);
    TalonSRX leftTreeLowerMotor = new TalonSRX(RobotConfig.Magazine.LEFT_TREE_LOWER_MOTOR_CHANNEL);
    Linebreaker leftTreeLinebreak = new Linebreaker(RobotConfig.Magazine.LEFT_TREE_LINEBREAK_CHANNEL);
    
    TalonSRX rightTreeUpperMotor = new TalonSRX(RobotConfig.Magazine.RIGHT_TREE_UPPER_MOTOR_CHANNEL);
    TalonSRX rightTreeLowerMotor = new TalonSRX(RobotConfig.Magazine.RIGHT_TREE_LOWER_MOTOR_CHANNEL);
    Linebreaker rightTreeLinebreak = new Linebreaker(RobotConfig.Magazine.RIGHT_TREE_UPPER_MOTOR_CHANNEL);
    
    TalonSRX treeStemLeftMotor = new TalonSRX(RobotConfig.Magazine.TREE_STEM_LEFT_MOTOR_CHANNEL);
    TalonSRX treeStemRightMotor = new TalonSRX(RobotConfig.Magazine.TREE_STEM_RIGHT_MOTOR_CHANNEL);
    Linebreaker treeStemLinebreak = new Linebreaker(RobotConfig.Magazine.TREE_STEM_LINEBREAK_CHANNEL);

    public void onInitialize() {
        this.leftTreeUpperMotor.setNeutralMode(NeutralMode.Brake);
        this.leftTreeLowerMotor.setNeutralMode(NeutralMode.Brake);
        this.rightTreeUpperMotor.setNeutralMode(NeutralMode.Brake);
        this.rightTreeLowerMotor.setNeutralMode(NeutralMode.Brake);
        this.treeStemLeftMotor.setNeutralMode(NeutralMode.Brake);
        this.treeStemRightMotor.setNeutralMode(NeutralMode.Brake);

        this.leftTreeLowerMotor.setInverted(true);
        this.rightTreeUpperMotor.setInverted(true);
        this.treeStemLeftMotor.setInverted(true);

        this.leftTreeLowerMotor.follow(this.leftTreeUpperMotor);
        this.rightTreeLowerMotor.follow(this.rightTreeUpperMotor);
        this.treeStemRightMotor.follow(this.treeStemLeftMotor);
    }

    public void run(double power) {
        if (power <= 0) {
            this.runRaw(power);
            return;
        }

        this.treeStemLeftMotor.set(ControlMode.PercentOutput, power);
        this.leftTreeUpperMotor.set(ControlMode.PercentOutput, (this.treeStemLinebreak.Broken() && this.leftTreeLinebreak.Broken()) ? 0 : power);
        this.rightTreeUpperMotor.set(ControlMode.PercentOutput, (this.treeStemLinebreak.Broken() && this.rightTreeLinebreak.Broken()) ? ((this.leftTreeLinebreak.Broken()) ? -Math.abs(power) : 0) : power);
    }

    public void runRaw(double power) {
        this.treeStemLeftMotor.set(ControlMode.PercentOutput, power);
        this.leftTreeUpperMotor.set(ControlMode.PercentOutput, power);
        this.rightTreeUpperMotor.set(ControlMode.PercentOutput, power);
    }

    @Override
    public void onDispose() {
        this.runRaw(0);
    }
}

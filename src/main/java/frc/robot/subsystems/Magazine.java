package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.robot.RobotMap;
import frc.robot.sensors.Linebreaker;
import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class Magazine extends DiSubsystem implements IInitializable, IDisposable {
    TalonSRX leftTreeMotor = new TalonSRX(RobotMap.Magazine.LEFT_TREE_MOTOR_CHANNEL);;
    Linebreaker leftTreeLinebreak = new Linebreaker(RobotMap.Magazine.LEFT_TREE_LINEBREAK_CHANNEL);
    
    TalonSRX rightTreeMotor = new TalonSRX(RobotMap.Magazine.RIGHT_TREE_MOTOR_CHANNEL);
    Linebreaker rightTreeLinebreak = new Linebreaker(RobotMap.Magazine.RIGHT_TREE_LINEBREAK_CHANNEL);
    
    TalonSRX treeStemMotor = new TalonSRX(RobotMap.Magazine.TREE_STEM_MOTOR_CHANNEL);
    Linebreaker treeStemLinebreak = new Linebreaker(RobotMap.Magazine.TREE_STEM_LINEBREAK_CHANNEL);

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
}

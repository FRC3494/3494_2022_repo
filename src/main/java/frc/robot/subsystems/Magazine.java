package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.robot.sensors.Linebreaker;
import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class Magazine extends DiSubsystem implements IInitializable, IDisposable {
    @Inject(id = "leftTreeUpperMotor")
    TalonSRX leftTreeUpperMotor;
    @Inject(id = "leftTreeLowerMotor")
    TalonSRX leftTreeLowerMotor;
    @Inject(id = "leftTreeLinebreak")
    Linebreaker leftTreeLinebreak;
    
    @Inject(id = "rightTreeUpperMotor")
    TalonSRX rightTreeUpperMotor;
    @Inject(id = "rightTreeLowerMotor")
    TalonSRX rightTreeLowerMotor;
    @Inject(id = "rightTreeLinebreak")
    Linebreaker rightTreeLinebreak;
    
    @Inject(id = "treeStemLeftMotor")
    TalonSRX treeStemLeftMotor;
    @Inject(id = "treeStemRightMotor")
    TalonSRX treeStemRightMotor;
    @Inject(id = "treeStemLinebreak")
    Linebreaker treeStemLinebreak;

    @Override
    public void Initialize() {
        leftTreeUpperMotor.setNeutralMode(NeutralMode.Brake);
        leftTreeLowerMotor.setNeutralMode(NeutralMode.Brake);
        rightTreeUpperMotor.setNeutralMode(NeutralMode.Brake);
        rightTreeLowerMotor.setNeutralMode(NeutralMode.Brake);
        treeStemLeftMotor.setNeutralMode(NeutralMode.Brake);
        treeStemRightMotor.setNeutralMode(NeutralMode.Brake);

        leftTreeLowerMotor.setInverted(true);
        rightTreeUpperMotor.setInverted(true);
        treeStemLeftMotor.setInverted(true);

        leftTreeLowerMotor.follow(leftTreeUpperMotor);
        rightTreeLowerMotor.follow(rightTreeUpperMotor);
        treeStemRightMotor.follow(treeStemLeftMotor);
    }

    public void Run(double power) {
        if (power <= 0) {
            RunRaw(power);
            return;
        }

        treeStemLeftMotor.set(ControlMode.PercentOutput, power);
        leftTreeUpperMotor.set(ControlMode.PercentOutput, (treeStemLinebreak.Broken() && leftTreeLinebreak.Broken()) ? 0 : power);
        rightTreeUpperMotor.set(ControlMode.PercentOutput, (treeStemLinebreak.Broken() && rightTreeLinebreak.Broken()) ? ((leftTreeLinebreak.Broken()) ? -Math.abs(power) : 0) : power);
    }

    public void RunRaw(double power) {
        treeStemLeftMotor.set(ControlMode.PercentOutput, power);
        leftTreeUpperMotor.set(ControlMode.PercentOutput, power);
        rightTreeUpperMotor.set(ControlMode.PercentOutput, power);
    }

    @Override
    public void Dispose() {
        RunRaw(0);
    }
}

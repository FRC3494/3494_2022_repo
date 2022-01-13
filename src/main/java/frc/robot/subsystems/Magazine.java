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

    public void Run(double power) {
        if (power <= 0) {
            this.RunRaw(power);
            return;
        }

        this.treeStemLeftMotor.set(ControlMode.PercentOutput, power);
        this.leftTreeUpperMotor.set(ControlMode.PercentOutput, (this.treeStemLinebreak.Broken() && this.leftTreeLinebreak.Broken()) ? 0 : power);
        this.rightTreeUpperMotor.set(ControlMode.PercentOutput, (this.treeStemLinebreak.Broken() && this.rightTreeLinebreak.Broken()) ? ((this.leftTreeLinebreak.Broken()) ? -Math.abs(power) : 0) : power);
    }

    public void RunRaw(double power) {
        this.treeStemLeftMotor.set(ControlMode.PercentOutput, power);
        this.leftTreeUpperMotor.set(ControlMode.PercentOutput, power);
        this.rightTreeUpperMotor.set(ControlMode.PercentOutput, power);
    }

    @Override
    public void Dispose() {
        this.RunRaw(0);
    }
}

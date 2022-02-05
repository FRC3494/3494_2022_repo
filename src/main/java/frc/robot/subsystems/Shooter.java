package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.RobotConfig;
import frc.robot.RobotMap;
import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.ITickable;

public class Shooter extends DiSubsystem implements IInitializable, IDisposable, ITickable {
    @Inject
    public Pneumatics pneumatics;

    private CANSparkMax shooterMotor = new CANSparkMax(RobotMap.Shooter.SHOOTER_MOTOR_CHANNEL, MotorType.kBrushless);
    private SparkMaxPIDController shooterPidController;
    private RelativeEncoder shooterMotorEncoder;
    
    private CANSparkMax turretMotor = new CANSparkMax(RobotMap.Shooter.TURRET_MOTOR_CHANNEL, MotorType.kBrushless);

    private DoubleSolenoid hoodMainSolenoid = new DoubleSolenoid(RobotMap.Pneumatics.SHOOTER_PCM, PneumaticsModuleType.CTREPCM, RobotMap.Shooter.HOOD_MAIN_SOLENOID_CHANNEL, RobotMap.Shooter.HOOD_MAIN_SOLENOID_CHANNEL + 1);
    private DoubleSolenoid hoodSecondarySolenoid = new DoubleSolenoid(RobotMap.Pneumatics.SHOOTER_PCM, PneumaticsModuleType.CTREPCM, RobotMap.Shooter.HOOD_SECONDARY_SOLENOID_CHANNEL, RobotMap.Shooter.HOOD_SECONDARY_SOLENOID_CHANNEL + 1);

    private boolean aimbotEnabled = false;

    public void onInitialize() {
        this.shooterMotor.setIdleMode(IdleMode.kBrake);

        this.shooterMotorEncoder = this.shooterMotor.getEncoder();
        this.shooterPidController = this.shooterMotor.getPIDController();

        this.shooterPidController.setP(RobotConfig.Shooter.PIDF.P);
        this.shooterPidController.setI(RobotConfig.Shooter.PIDF.I);
        this.shooterPidController.setD(RobotConfig.Shooter.PIDF.D);
        this.shooterPidController.setFF(RobotConfig.Shooter.PIDF.FF);
    }

    public void run(double rpm) {
        this.shooterPidController.setReference(rpm, ControlType.kVelocity);
    }

    public void runHood(boolean position) {
        this.hoodMainSolenoid.set((position) ? Value.kForward : Value.kReverse);
        this.hoodSecondarySolenoid.set(Value.kReverse);
    }

    public void runTurret(double power) {
        if (this.aimbotEnabled) return;

        this.turretMotor.set(power);
    }

    public void onDispose() {
        this.run(0);
    }

    public double getRPM() {
        return this.shooterMotorEncoder.getVelocity();
    }

    public void enableAimBot() {
        this.aimbotEnabled = true;
    }

    public void disableAimBot() {
        this.aimbotEnabled = false;
    }

    public void onTick() {
        if (this.aimbotEnabled) {
            // aimbot code here
        }
    }
}

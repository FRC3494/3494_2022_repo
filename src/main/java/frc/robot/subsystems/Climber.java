package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.RobotMap;
import com.fizzyapple12.javadi.DiInterfaces.IDisposable;
import com.fizzyapple12.javadi.DiInterfaces.IInitializable;
import com.fizzyapple12.wpilibdi.DiSubsystem;

public class Climber extends DiSubsystem implements IInitializable, IDisposable {
    private CANSparkMax climbMotor = new CANSparkMax(RobotMap.Climber.CLIMB_MOTOR_CHANNEL, MotorType.kBrushless);

    private DoubleSolenoid ratchetReleaseSolenoid = new DoubleSolenoid(RobotMap.Pneumatics.BASE_PCM, PneumaticsModuleType.CTREPCM, RobotMap.Climber.CLIMB_RELEASE_RATCHET_SOLENOID_CHANNEL, RobotMap.Climber.CLIMB_RELEASE_RATCHET_SOLENOID_CHANNEL + 1);
    private DoubleSolenoid armReleaseSolenoid = new DoubleSolenoid(RobotMap.Pneumatics.BASE_PCM, PneumaticsModuleType.CTREPCM, RobotMap.Climber.CLIMB_RELEASE_ARM_SOLENOID_CHANNEL, RobotMap.Climber.CLIMB_RELEASE_ARM_SOLENOID_CHANNEL + 1);

    long t = 0;

    long prevT = 0;

    public void onInitialize() {
        this.climbMotor.setIdleMode(IdleMode.kBrake);

        this.armReleaseSolenoid.set(Value.kForward);

        this.releaseRatchet(true);
    }

    public void run(double power) {
        if (this.armReleaseSolenoid.get() == Value.kForward) return;

        if (power > 0) this.releaseRatchet(false);
        else this.releaseRatchet(true);

        this.runRaw(power);
    }

    public void runRaw(double power) {
        this.climbMotor.set(power);
    }

    public void releaseRatchet(boolean release) {
        this.ratchetReleaseSolenoid.set((release) ? Value.kForward : Value.kReverse);
    }

    public void releaseArm() {
        this.armReleaseSolenoid.set(Value.kReverse);
    }

    public void retractArm() {
        this.armReleaseSolenoid.set(Value.kForward);
    }

    public boolean isRatchetOut() {
        return this.ratchetReleaseSolenoid.get() == Value.kForward;
    }

    public void onDispose() {
        this.run(0);
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Climber");

        builder.setActuator(true);

        builder.addDoubleProperty("Climber Speed", this.climbMotor::get, (double value) -> { });

        builder.addBooleanProperty("Rachet Engage", () -> {
            return this.ratchetReleaseSolenoid.get() == Value.kReverse;
        }, (boolean value) -> { });

        builder.addBooleanProperty("Arm Hold", () -> {
            return this.armReleaseSolenoid.get() == Value.kReverse;
        }, (boolean value) -> { });
    }
}

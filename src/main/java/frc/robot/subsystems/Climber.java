package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.RobotMap;
import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class Climber extends DiSubsystem implements IInitializable, IDisposable {
    private CANSparkMax climbMotor = new CANSparkMax(RobotMap.Climber.CLIMB_MOTOR_CHANNEL, MotorType.kBrushless);

    private DoubleSolenoid releaseSolenoid = new DoubleSolenoid(RobotMap.Pneumatics.BASE_PCM, PneumaticsModuleType.CTREPCM, RobotMap.Climber.CLIMB_RELEASE_SOLENOID_CHANNEL, RobotMap.Climber.CLIMB_RELEASE_SOLENOID_CHANNEL + 1);

    public void onInitialize() {
        this.climbMotor.setIdleMode(IdleMode.kBrake);
    }

    public void run(double power) {
        if (power >= 0) this.release(false);
        else this.release(true);

        this.climbMotor.set(power);
    }

    public void release(boolean release) {
        this.releaseSolenoid.set((release) ? Value.kForward : Value.kReverse);
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
            return this.releaseSolenoid.get() == Value.kReverse;
        }, (boolean value) -> { });
    }
}

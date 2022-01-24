package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Servo;
import frc.robot.RobotConfig;
import frc.robot.RobotMap;
import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class Climber extends DiSubsystem implements IInitializable, IDisposable {
    private CANSparkMax climbMotor = new CANSparkMax(RobotMap.Climber.CLIMB_MOTOR_CHANNEL, MotorType.kBrushless);

    private Servo climberReleaseServo = new Servo(RobotMap.Climber.RELEASE_SERVO_CHANNEL);

    private boolean hasReleased = false;

    public void onInitialize() {
        this.climbMotor.setIdleMode(IdleMode.kBrake);

        this.climberReleaseServo.set(RobotConfig.Climber.HOLDING_POSITION);
    }

    public void run(double power) {
        this.climbMotor.set((this.hasReleased) ? power : 0);
    }

    public void release() {
        this.climberReleaseServo.set(RobotConfig.Climber.RELEASE_POSITION);

        this.hasReleased = true;
    }

    public void onDispose() {
        this.run(0);
    }
}

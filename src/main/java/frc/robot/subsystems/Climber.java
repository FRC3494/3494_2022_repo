package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Servo;
import frc.robot.RobotConfig;
import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class Climber extends DiSubsystem implements IInitializable, IDisposable {
    CANSparkMax leftClimbMotor = new CANSparkMax(RobotConfig.Climber.LEFT_CLIMB_MOTOR_CHANNEL, MotorType.kBrushless);
    CANSparkMax rightClimbMotor = new CANSparkMax(RobotConfig.Climber.RIGHT_CLIMB_MOTOR_CHANNEL, MotorType.kBrushless);

    Servo climberReleaseServo = new Servo(RobotConfig.Climber.RELEASE_SERVO_CHANNEL);

    boolean hasReleased = false;

    public void onInitialize() {
        this.leftClimbMotor.setIdleMode(IdleMode.kBrake);
        this.rightClimbMotor.setIdleMode(IdleMode.kBrake);

        this.leftClimbMotor.setInverted(true);

        this.rightClimbMotor.follow(this.leftClimbMotor);

        this.climberReleaseServo.set(RobotConfig.Climber.HOLDING_POSITION);
    }

    public void run(double power) {
        this.leftClimbMotor.set((this.hasReleased) ? power : 0);
    }

    public void release() {
        this.climberReleaseServo.set(RobotConfig.Climber.RELEASE_POSITION);

        this.hasReleased = true;
    }

    @Override
    public void onDispose() {
        run(0);
    }
}

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.Servo;
import frc.robot.RobotConfig;
import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class Climber extends DiSubsystem implements IInitializable, IDisposable {
    @Inject(id = "leftClimbMotor")
    CANSparkMax leftClimbMotor;
    @Inject(id = "rightClimbMotor")
    CANSparkMax rightClimbMotor;

    @Inject(id = "climberReleaseServo")
    Servo climberReleaseServo;

    boolean hasReleased = false;

    @Override
    public void Initialize() {
        leftClimbMotor.setIdleMode(IdleMode.kBrake);
        rightClimbMotor.setIdleMode(IdleMode.kBrake);

        leftClimbMotor.setInverted(true);

        rightClimbMotor.follow(leftClimbMotor);

        climberReleaseServo.set(RobotConfig.Climber.holdingPosition);
    }

    public void Climb(double power) {
        leftClimbMotor.set((hasReleased) ? power : 0);
    }

    public void ReleaseClimber() {
        climberReleaseServo.set(RobotConfig.Climber.releasePosition);

        hasReleased = true;
    }

    @Override
    public void Dispose() {
        Climb(0);
    }
}

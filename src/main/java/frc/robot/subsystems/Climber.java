package frc.robot.subsystems;

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
        this.leftClimbMotor.setIdleMode(IdleMode.kBrake);
        this.rightClimbMotor.setIdleMode(IdleMode.kBrake);

        this.leftClimbMotor.setInverted(true);

        this.rightClimbMotor.follow(this.leftClimbMotor);

        this.climberReleaseServo.set(RobotConfig.Climber.HoldingPosition);
    }

    public void Run(double power) {
        this.leftClimbMotor.set((this.hasReleased) ? power : 0);
    }

    public void ReleaseClimber() {
        this.climberReleaseServo.set(RobotConfig.Climber.ReleasePosition);

        this.hasReleased = true;
    }

    @Override
    public void Dispose() {
        Run(0);
    }
}

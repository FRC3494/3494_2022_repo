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
    public void initialize() {
        this.leftClimbMotor.setIdleMode(IdleMode.kBrake);
        this.rightClimbMotor.setIdleMode(IdleMode.kBrake);

        this.leftClimbMotor.setInverted(true);

        this.rightClimbMotor.follow(leftClimbMotor);

        this.climberReleaseServo.set(RobotConfig.Climber.HOLDING_POSITION);
    }

    public void Climb(double power) {
        this.leftClimbMotor.set((hasReleased) ? power : 0);
    }

    public void ReleaseClimber() {
        this.climberReleaseServo.set(RobotConfig.Climber.RELEASE_POSITION);

        hasReleased = true;
    }

    @Override
    public void dispose() {
        Climb(0);
    }
}

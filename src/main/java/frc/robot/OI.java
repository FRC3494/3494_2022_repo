package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.ITickable;

public class OI implements IInitializable, IDisposable {
    @Inject(id = "primaryXbox")
    public XboxController primaryXbox;

    @Inject(id = "secondaryXbox")
    public XboxController secondaryXbox;

    @Override
    public void Initialize() {
        
    }

    @Override
    public void Dispose() {
        
    }

    public double GetLeftDriveSpeed() {
        return (primaryXbox.getY(Hand.kLeft) * RobotConfig.Drivetrain.forwardSensitivity) + (primaryXbox.getX(Hand.kRight) * RobotConfig.Drivetrain.turnSensitivity);
    }

    public double GetRightDriveSpeed() {
        return (primaryXbox.getY(Hand.kLeft) * RobotConfig.Drivetrain.forwardSensitivity) - (primaryXbox.getX(Hand.kRight) * RobotConfig.Drivetrain.turnSensitivity);
    }

    public boolean GetNeedOuttake() {
        return secondaryXbox.getAButton();
    }

    public double GetClimberPower() {
        return (secondaryXbox.getYButton()) ? (secondaryXbox.getY(Hand.kLeft) * RobotConfig.Climber.climbSpeed) : 0;
    }

    public boolean GetClimberRelease() {
        return secondaryXbox.getBButton() && secondaryXbox.getYButton();
    }

    public double GetIntakeSpeed() {
        return secondaryXbox.getTriggerAxis(Hand.kLeft) * RobotConfig.Intake.intakeSpeed;
    }

    public double GetShooterPower() {
        return secondaryXbox.getTriggerAxis(Hand.kRight);
    }
}
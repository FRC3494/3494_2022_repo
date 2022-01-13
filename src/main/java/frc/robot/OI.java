package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class OI implements IInitializable, IDisposable {
    @Inject(id = "primaryXbox")
    XboxController primaryXbox;

    @Inject(id = "secondaryXbox")
    XboxController secondaryXbox;

    @Override
    public void Initialize() {
        
    }

    @Override
    public void Dispose() {
        
    }

    public double GetLeftDriveSpeed() {
        return (this.primaryXbox.getLeftY() * RobotConfig.Drivetrain.ForwardSensitivity) + (this.primaryXbox.getRightX() * RobotConfig.Drivetrain.TurnSensitivity);
    }

    public double GetRightDriveSpeed() {
        return (this.primaryXbox.getLeftY() * RobotConfig.Drivetrain.ForwardSensitivity) - (this.primaryXbox.getRightX() * RobotConfig.Drivetrain.TurnSensitivity);
    }

    public boolean GetNeedOuttake() {
        return this.secondaryXbox.getAButton();
    }

    public double GetClimberPower() {
        return (this.secondaryXbox.getYButton()) ? (this.secondaryXbox.getLeftY() * RobotConfig.Climber.ClimbSpeed) : 0;
    }

    public boolean GetClimberRelease() {
        return this.secondaryXbox.getBButton() && this.secondaryXbox.getYButton();
    }

    public double GetIntakeSpeed() {
        return this.secondaryXbox.getLeftTriggerAxis() * RobotConfig.Intake.IntakeSpeed;
    }

    public double GetShooterPower() {
        return this.secondaryXbox.getRightTriggerAxis();
    }

    public boolean StartSinging() {
        return this.secondaryXbox.getXButton(); // Disable this before comp lol
    }
}
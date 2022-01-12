package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
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
    public void initialize() {
        
    }

    @Override
    public void dispose() {
        
    }

    public double GetLeftDriveSpeed() {
        return (primaryXbox.getLeftY() * RobotConfig.Drivetrain.FORWARD_SENSITIVITY) + (primaryXbox.getRightX() * RobotConfig.Drivetrain.TURN_SENSITIVITY);
    }

    public double GetRightDriveSpeed() {
        return (primaryXbox.getLeftY() * RobotConfig.Drivetrain.FORWARD_SENSITIVITY) - (primaryXbox.getRightX() * RobotConfig.Drivetrain.TURN_SENSITIVITY);
    }

    public boolean GetNeedOuttake() {
        return secondaryXbox.getAButton();
    }

    public double GetClimberPower() {
        return (secondaryXbox.getYButton()) ? (secondaryXbox.getLeftY() * RobotConfig.Climber.CLIMB_SPEED) : 0;
    }

    public boolean GetClimberRelease() {
        return secondaryXbox.getBButton() && secondaryXbox.getYButton();
    }

    public double GetIntakeSpeed() {
        return secondaryXbox.getLeftTriggerAxis() * RobotConfig.Intake.INTAKE_SPEED;
    }

    public double GetShooterPower() {
        return secondaryXbox.getRightTriggerAxis();
    }
}
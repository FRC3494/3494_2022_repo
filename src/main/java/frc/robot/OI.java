package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class OI implements IInitializable, IDisposable {
    XboxController primaryXbox = new XboxController(0);

    XboxController secondaryXbox = new XboxController(1);

    public void onInitialize() {
        
    }

    public void onDispose() {
        
    }

    public double GetLeftDriveSpeed() {
        return (this.primaryXbox.getLeftY() * RobotConfig.Drivetrain.FORWARD_SENSITIVITY) + (this.primaryXbox.getRightX() * RobotConfig.Drivetrain.TURN_SENSITIVITY);
    }

    public double GetRightDriveSpeed() {
        return (this.primaryXbox.getLeftY() * RobotConfig.Drivetrain.FORWARD_SENSITIVITY) - (this.primaryXbox.getRightX() * RobotConfig.Drivetrain.TURN_SENSITIVITY);
    }

    public boolean GetNeedOuttake() {
        return this.secondaryXbox.getAButton();
    }

    public double GetClimberPower() {
        return (this.primaryXbox.getYButton()) ? ((this.primaryXbox.getLeftTriggerAxis() - this.primaryXbox.getRightTriggerAxis()) * RobotConfig.Climber.CLIMB_SPEED) : 0;
    }

    public double GetIntakeSpeed() {
        return this.secondaryXbox.getLeftTriggerAxis() * RobotConfig.Intake.INTAKE_SPEED;
    }

    public boolean GetOverrideMagazineStateMachine() {
        return this.secondaryXbox.getYButton();
    }

    public double GetShooterPower() {
        return this.secondaryXbox.getRightTriggerAxis();
    }

    public double GetTurretPower() {
        return this.secondaryXbox.getLeftX() * RobotConfig.Shooter.TURRET_SPEED;
    }

    public boolean StartSinging() {
        return this.primaryXbox.getAButton() && this.primaryXbox.getBButton() && this.primaryXbox.getXButton() && this.primaryXbox.getYButton() && this.secondaryXbox.getAButton() && this.secondaryXbox.getBButton() && this.secondaryXbox.getXButton() && this.secondaryXbox.getYButton(); // Disable this before comp lol
    }
}
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
        return (this.secondaryXbox.getYButton()) ? (this.secondaryXbox.getLeftY() * RobotConfig.Climber.CLIMB_SPEED) : 0;
    }

    public double GetIntakeSpeed() {
        return this.secondaryXbox.getLeftTriggerAxis() * RobotConfig.Intake.INTAKE_SPEED;
    }

    public double GetShooterPower() {
        return this.secondaryXbox.getRightTriggerAxis();
    }

    public double GetTurretPower() {
        return this.secondaryXbox.getLeftX() * RobotConfig.Shooter.TURRET_SPEED;
    }

    public boolean StartSinging() {
        return this.secondaryXbox.getXButton(); // Disable this before comp lol
    }
}
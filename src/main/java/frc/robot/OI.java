package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.utilities.di.DiInterfaces.ITickable;

public class OI implements ITickable {
    XboxController primaryXbox = new XboxController(0);

    XboxController secondaryXbox = new XboxController(1);

    String buttonSequence = "";

    String targetSequence = "uuddlrlrbas";
    boolean pressingPOV = true;

    public void onTick() {
        if (this.buttonSequence.length() > this.targetSequence.length()) this.buttonSequence = "";

        for (int i = 0; i < this.buttonSequence.length(); i++) {
            if (this.buttonSequence.charAt(i) != this.targetSequence.charAt(i)) this.buttonSequence = "";
        }

        if ((this.secondaryXbox.getPOV() > -1 && this.secondaryXbox.getPOV() < 35) || this.secondaryXbox.getPOV() > 325 && !this.pressingPOV) this.buttonSequence += 'u';
        if (this.secondaryXbox.getPOV() > 55 && this.secondaryXbox.getPOV() < 125 && !this.pressingPOV) this.buttonSequence += 'd';
        if (this.secondaryXbox.getPOV() > 145 && this.secondaryXbox.getPOV() < 215 && !this.pressingPOV) this.buttonSequence += 'l';
        if (this.secondaryXbox.getPOV() > 235 && this.secondaryXbox.getPOV() < 305 && !this.pressingPOV) this.buttonSequence += 'r';

        this.pressingPOV = this.secondaryXbox.getPOV() != -1;

        if (this.secondaryXbox.getAButtonPressed()) this.buttonSequence += 'a';
        if (this.secondaryXbox.getBButtonPressed()) this.buttonSequence += 'b';
        if (this.secondaryXbox.getStartButtonPressed()) this.buttonSequence += 's';
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
        boolean equal = this.buttonSequence.equals(this.targetSequence);

        if (equal) this.buttonSequence = "";

        return equal;
        //return this.primaryXbox.getAButton() && this.primaryXbox.getBButton() && this.primaryXbox.getXButton() && this.primaryXbox.getYButton() && this.secondaryXbox.getAButton() && this.secondaryXbox.getBButton() && this.secondaryXbox.getXButton() && this.secondaryXbox.getYButton(); // Disable this before comp lol
    }
}
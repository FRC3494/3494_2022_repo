package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.utilities.di.DiInterfaces.ITickable;

public class OI implements ITickable {
    XboxController primaryXbox = new XboxController(0);

    XboxController secondaryXbox = new XboxController(1);

    String buttonSequence = "";

    String targetSequence = "uuddlrlrbas";
    boolean pressingPOV = false;

    boolean magazineIdle = false;

    boolean switchRPM = false;
    boolean switchRPMPressed = false;

    public void onTick() {
        /*if (this.buttonSequence.length() > this.targetSequence.length()) this.buttonSequence = "";

        for (int i = 0; i < this.buttonSequence.length(); i++) {
            if (this.buttonSequence.charAt(i) != this.targetSequence.charAt(i)) this.buttonSequence = "";
        }

        if (!this.pressingPOV && (this.secondaryXbox.getPOV() > -1 && this.secondaryXbox.getPOV() < 35) || this.secondaryXbox.getPOV() > 325) this.buttonSequence += 'u';
        if (!this.pressingPOV && this.secondaryXbox.getPOV() > 145 && this.secondaryXbox.getPOV() < 215) this.buttonSequence += 'd';
        if (!this.pressingPOV && this.secondaryXbox.getPOV() > 235 && this.secondaryXbox.getPOV() < 305) this.buttonSequence += 'l';
        if (!this.pressingPOV && this.secondaryXbox.getPOV() > 55 && this.secondaryXbox.getPOV() < 125) this.buttonSequence += 'r';

        if (this.secondaryXbox.getAButtonPressed()) this.buttonSequence += 'a';
        if (this.secondaryXbox.getBButtonPressed()) this.buttonSequence += 'b';
        if (this.secondaryXbox.getStartButtonPressed()) this.buttonSequence += 's';*/

        if (this.pressingPOV) {
            if (!this.switchRPMPressed) this.switchRPM = true;
            this.switchRPMPressed = true;
        } else this.switchRPMPressed = false;

        this.pressingPOV = this.secondaryXbox.getPOV() != -1;

        //if (this.secondaryXbox.getXButtonPressed()) magazineIdle = !magazineIdle;

        //System.out.println(buttonSequence);
    }

    public double GetLeftDriveSpeed() { //
        return -(this.primaryXbox.getLeftY() * RobotConfig.Drivetrain.FORWARD_SENSITIVITY) - (this.primaryXbox.getRightX() * RobotConfig.Drivetrain.TURN_SENSITIVITY);
    }

    public double GetRightDriveSpeed() { //
        return -(this.primaryXbox.getLeftY() * RobotConfig.Drivetrain.FORWARD_SENSITIVITY) + (this.primaryXbox.getRightX() * RobotConfig.Drivetrain.TURN_SENSITIVITY);
    }

    public boolean GetNeedOuttake() { //
        return this.secondaryXbox.getStartButton();
        //return this.primaryXbox.getAButton();
    }

    public boolean GetMagazineIdle() { //
        return magazineIdle;
    }

    public double GetClimberPower() { //
        //return (this.primaryXbox.getYButton()) ? ((this.primaryXbox.getLeftTriggerAxis() - this.primaryXbox.getRightTriggerAxis())) : 0;
        return this.primaryXbox.getLeftTriggerAxis() - this.primaryXbox.getRightTriggerAxis();
        //return 0;
    }

    public double GetClimberWithoutRachetPower() { //
        //return (this.primaryXbox.getYButton()) ? (((this.primaryXbox.getLeftBumper() ? 1 : 0) - (this.primaryXbox.getRightBumper() ? 1 : 0))) : 0;
        return (this.primaryXbox.getLeftBumper() ? 0.5 : 0) - (this.primaryXbox.getRightBumper() ? 1 : 0);
        //return 0;
    }

    public boolean GetClimberAutoDeploy() {
        return this.primaryXbox.getYButton();
    }

    public double GetFrontIntakePower() { //
        //return this.secondaryXbox.getRightTriggerAxis();
        //return this.primaryXbox.getLeftTriggerAxis();p
        if (this.secondaryXbox.getYButton() && this.secondaryXbox.getRightBumper()) return 0;
        return (this.secondaryXbox.getXButton()) ? 1 : 0;
    }

    public double GetBackIntakePower() { //
        //return this.secondaryXbox.getRightTriggerAxis();
        //return this.primaryXbox.getLeftTriggerAxis();
        if (this.secondaryXbox.getBButton() && this.secondaryXbox.getRightBumper()) return 0;
        return (this.secondaryXbox.getAButton()) ? 1 : 0;
    }

    public double GetLeftTreeMagazinePower() { //
        //return (this.secondaryXbox.getAButton()) ? 1 : 0;
        if (this.secondaryXbox.getYButton() && this.secondaryXbox.getRightBumper()) return -1;
        return (this.secondaryXbox.getRightBumper()) ? 1 : 0;
    }

    public double GetRightTreeMagazinePower() { //
        if (this.secondaryXbox.getBButton() && this.secondaryXbox.getRightBumper()) return -1;
        return (this.secondaryXbox.getRightBumper()) ? 1 : 0;
        //return (this.secondaryXbox.getAButton()) ? 1 : 0;
    }

    public double GetTreeStemMagazinePower() { //
        //return (this.secondaryXbox.getYButton()) ? 1 : 0;
        return (this.secondaryXbox.getRightTriggerAxis() != 0) ? 1 : 0;
    }

    public boolean QueueBall() {
        return this.secondaryXbox.getYButtonPressed();
    }

    public boolean GetOverrideMagazineStateMachine() {
        //return this.secondaryXbox.getYButton();
        return false;
    }

    public double GetShooterPower() { //
        return this.secondaryXbox.getLeftTriggerAxis();
    }

    public boolean ToggleAimBot() { //
        return this.secondaryXbox.getBackButtonPressed();
    }

    public boolean ToggleShooterHood() {//
        return this.secondaryXbox.getLeftBumperPressed();
    }

    public double GetTurretPower() { //
        return this.secondaryXbox.getLeftX();
    }

    public boolean GetTurretGoToFront() { //
        return this.secondaryXbox.getLeftY() > 0;
    }

    public boolean GetTurretGoToBack() { //
        return this.secondaryXbox.getLeftY() > 0;
    }

    public boolean GetSwitchRPM() {
        if (switchRPM) {
            switchRPM = false;
            return true;
        }

        return false;

        //return this.secondaryXbox.getPOV() != -1;
    }

    public boolean StartSinging() { //
        boolean equal = this.buttonSequence.equals(this.targetSequence);

        if (equal) this.buttonSequence = "";

        return equal;
        //return this.primaryXbox.getAButton() && this.primaryXbox.getBButton() && this.primaryXbox.getXButton() && this.primaryXbox.getYButton() && this.secondaryXbox.getAButton() && this.secondaryXbox.getBButton() && this.secondaryXbox.getXButton() && this.secondaryXbox.getYButton(); // Disable this before comp lol
    }
}
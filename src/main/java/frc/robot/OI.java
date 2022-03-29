package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.utilities.di.DiInterfaces.ITickable;

public class OI implements ITickable {
    XboxController primaryXbox = new XboxController(0);

    XboxController secondaryXbox = new XboxController(1);

    XboxController emergencyXbox = new XboxController(2);

    String buttonSequence = "";

    String targetSequence = "uuddlrlrbas";
    boolean pressingPOV = false;

    boolean magazineIdle = false;

    boolean queueBall = false;
    boolean queueBallPressed = false;

    public void onTick() {
        //System.out.println(primaryXbox.isConnected());
        //System.out.println(secondaryXbox.isConnected());
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

        if (this.secondaryXbox.getLeftTriggerAxis() > 0.25) {
            if (!this.queueBallPressed) this.queueBall = true;
            this.queueBallPressed = true;
        } else this.queueBallPressed = false;

        this.pressingPOV = this.secondaryXbox.getPOV() != -1;

        //if (this.secondaryXbox.getXButtonPressed()) magazineIdle = !magazineIdle;

        //System.out.println(buttonSequence);
    }

    public double GetForwardSpeed() { //
        return this.primaryXbox.getLeftY();
    }

    public double GetTurnSpeed() { //
        return this.primaryXbox.getRightX();
    }

    public boolean GetNeedOuttake() { //
        return this.secondaryXbox.getStartButton() || this.emergencyXbox.getStartButton();
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
        //if (this.secondaryXbox.getYButton() && this.secondaryXbox.getRightBumper()) return 0;
        return (this.secondaryXbox.getRightBumper()) ? 1 : 0;
    }

    public double GetBackIntakePower() { //
        //return this.secondaryXbox.getRightTriggerAxis();
        //return this.primaryXbox.getLeftTriggerAxis();
        //if (this.secondaryXbox.getBButton() && this.secondaryXbox.getRightBumper()) return 0;
        return (this.secondaryXbox.getLeftBumper()) ? 1 : 0;
    }

    public double GetLeftTreeMagazinePower() { //
        //if (!this.emergencyXbox.isConnected()) return 0;
        //return (this.emergencyXbox.getXButton()) ? 1 : 0;
        return (this.primaryXbox.getLeftBumper() && this.primaryXbox.getRightBumper()) ? 1 : 0;
    }

    public double GetRightTreeMagazinePower() { //
        //if (!this.emergencyXbox.isConnected()) return 0;
        //return (this.emergencyXbox.getBButton()) ? 1 : 0;
        return (this.primaryXbox.getLeftBumper() && this.primaryXbox.getRightBumper()) ? 1 : 0;
    }

    public double GetTreeStemMagazinePower() { //
        //if (!this.emergencyXbox.isConnected()) return 0;
        //return (this.emergencyXbox.getYButton()) ? 1 : 0;
        return (this.primaryXbox.getLeftBumper() && this.primaryXbox.getRightBumper()) ? 1 : 0;
    }

    public boolean QueueBall() {
        //return this.secondaryXbox.getYButtonPressed();
        if (queueBall) {
            queueBall = false;
            return true;
        }

        return false;
    }

    public boolean GetOverrideMagazineStateMachine() {
        //if (!this.emergencyXbox.isConnected()) return false;
        return this.primaryXbox.getLeftBumper() && this.primaryXbox.getRightBumper();
        //return this.emergencyXbox.getLeftBumper();
        //return false;
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
        return this.secondaryXbox.getLeftY() < -0.85;
    }

    public boolean GetTurretGoToBack() { //
        return this.secondaryXbox.getLeftY() > 0.85;
    }

    public int SetShooterSetting() {
        if (this.secondaryXbox.getYButton()) return 3;
        if (this.secondaryXbox.getXButton()) return 2;
        if (this.secondaryXbox.getAButton()) return 1;
        if (this.secondaryXbox.getBButton()) return 0;
        return -1;
        /*if (switchRPM) {
            switchRPM = false;
            return true;
        }

        return false;*/

        //return this.secondaryXbox.getPOV() != -1;
    }

    public boolean StartSinging() { //
        boolean equal = this.buttonSequence.equals(this.targetSequence);

        if (equal) this.buttonSequence = "";

        return equal;
        //return this.primaryXbox.getAButton() && this.primaryXbox.getBButton() && this.primaryXbox.getXButton() && this.primaryXbox.getYButton() && this.secondaryXbox.getAButton() && this.secondaryXbox.getBButton() && this.secondaryXbox.getXButton() && this.secondaryXbox.getYButton(); // Disable this before comp lol
    }
}
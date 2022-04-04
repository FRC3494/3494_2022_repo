package frc.robot.commands.teleop;

import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.ITickable;
import frc.robot.utilities.wpilibdi.DiCommand;
import frc.robot.OI;
import frc.robot.RobotConfig;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

public class Drive extends DiCommand implements IInitializable, ITickable, IDisposable {
    @Inject
    OI oi;

    @Inject
    DriverStation.Alliance alliance;

    @Inject
    Drivetrain drivetrain;

    @Inject
    Intake intake;

    @Inject
    Magazine magazine;

    @Inject
    Climber climber;

    @Inject
    Shooter shooter;

    boolean singing = false;
    boolean startedSinging = false;

    int selectedSetting = 0;

    boolean autoClimberDeploy = false;
    boolean climbing = false;
    int autoClimbStage = 0;
    Timer autoClimbTimer = new Timer();
    Timer climbRampTimer = new Timer();

    boolean shootABall = false;
    boolean waitForShooterReady = false;

    public void onInitialize() {
        this.autoClimbTimer.reset();
        this.climbRampTimer.start();
        
        this.drivetrain.stopAutoNav();

        this.shooter.enableTurret(true);
        this.shooter.runTurret(RobotConfig.Shooter.TURRET_FRONT_POSITION);
    }

    public void onTick() {
        //Drivetrain
        if (this.singing) {
            if (!this.startedSinging) {
                this.drivetrain.singTheTheme();
                this.startedSinging = true;
            }

            if (this.drivetrain.isDoneSinging()) {
                this.singing = false;
                this.startedSinging = false;
            }

            return;
        }

        this.drivetrain.arcadeDrive(RobotConfig.Drivetrain.PowerCurve(this.oi.GetForwardSpeed()) * RobotConfig.Drivetrain.FORWARD_SENSITIVITY, RobotConfig.Drivetrain.PowerCurve(this.oi.GetTurnSpeed()) * RobotConfig.Drivetrain.TURN_SENSITIVITY);

        if (this.oi.StartSinging()) this.singing = true;


        int newSetting = this.oi.SetShooterSetting();

        //Shooter
        if (!this.climbing) {
            if (newSetting != -1) {
                this.selectedSetting = newSetting;
                this.shooter.run(RobotConfig.Shooter.RPMS.get(this.selectedSetting));
            } else this.shooter.stop();
        } else this.shooter.stop();

        this.shooter.runTurretRelative(RobotConfig.Shooter.TurretPowerCurve(this.oi.GetTurretPower()) * RobotConfig.Shooter.TURRET_SPEED);
        if (this.oi.GetTurretGoToFront()) this.shooter.runTurret(RobotConfig.Shooter.TURRET_FRONT_POSITION);
        if (this.oi.GetTurretGoToBack()) this.shooter.runTurret(RobotConfig.Shooter.TURRET_BACK_POSITION);

        if (this.oi.ToggleAimBot()) {
            if (this.shooter.aimbotEnabled()) this.shooter.disableAimBot();
            else this.shooter.enableAimBot();
        }



        // Climber
        if (this.autoClimberDeploy) {
            this.intake.run(0.00001f, 0);
    
            if (this.autoClimbTimer.advanceIfElapsed(0.7)) {
                this.climber.releaseArm();
                this.autoClimberDeploy = false;
            }

            return;
        }
        
        if (this.oi.GetClimberAutoDeploy()) {
            if (!this.autoClimberDeploy) {
                this.autoClimberDeploy = true;
                this.climbing = true;
                this.autoClimbTimer.start();
            }
        }

        if (this.climbing) {
            if (this.oi.GetClimberPower() == 0 && this.oi.GetClimberWithoutRachetPower() == 0) {
                this.climbRampTimer.reset();
                this.climber.runRaw(0);
            } else if (this.oi.GetClimberPower() > 0) {
                this.climber.run(RobotConfig.Climber.PowerCurve(this.oi.GetClimberPower() * RobotConfig.Climber.ANALOG_CLIMB_SPEED, this.climbRampTimer.get()));
            } else {
                this.climber.releaseRatchet(true);
                if (climbRampTimer.get() > 0.1) this.climber.run(this.oi.GetClimberPower() * RobotConfig.Climber.ANALOG_CLIMB_SPEED);
            }

            this.intake.run(0.00001f, 0);
            
            return;
        } else {
            this.climber.releaseRatchet(true);
            this.climber.retractArm();
        }



        //Intake + Magazine
        double frontIntakeSpeed = !(magazine.leftFull() || magazine.isEjecting()) ? RobotConfig.Intake.PowerCurve(this.oi.GetFrontIntakePower()) * RobotConfig.Intake.INTAKE_SPEED : 0;
        double backIntakeSpeed = !(magazine.rightFull() || magazine.isEjecting()) ? RobotConfig.Intake.PowerCurve(this.oi.GetBackIntakePower()) * RobotConfig.Intake.INTAKE_SPEED : 0;

        this.intake.run(frontIntakeSpeed, backIntakeSpeed);

        this.magazine.setAutoOperate(!this.oi.GetOverrideMagazineStateMachine());

        if (this.oi.GetOverrideMagazineStateMachine()) {
            double leftMagazineSpeed = this.oi.GetLeftTreeMagazinePower() * RobotConfig.Magazine.INTAKE_SPEED;
            double rightMagazineSpeed = this.oi.GetRightTreeMagazinePower() * RobotConfig.Magazine.INTAKE_SPEED;
            double stemMagazineSpeed = this.oi.GetTreeStemMagazinePower() * RobotConfig.Magazine.INTAKE_SPEED;
    
            //if (this.oi.GetNeedOuttake()) {
                leftMagazineSpeed = RobotConfig.Magazine.OUTTAKE_SPEED;
                rightMagazineSpeed = RobotConfig.Magazine.OUTTAKE_SPEED;
                stemMagazineSpeed = RobotConfig.Magazine.OUTTAKE_SPEED / 4;
            //}
    
            this.magazine.run(leftMagazineSpeed, rightMagazineSpeed, stemMagazineSpeed);
        } else {
            if (this.oi.QueueBall() && newSetting != -1) waitForShooterReady = true;

            if (waitForShooterReady && this.shooter.atRPM()) {
                this.magazine.sendBall(RobotConfig.Shooter.RPMS.get(this.selectedSetting).feedThrough);
                waitForShooterReady = false;
            }

            this.magazine.useFastSpeed(this.oi.GetFrontIntakePower() > 0 || this.oi.GetBackIntakePower() > 0);
        }
    }

    public void onDispose() {
        this.intake.run(0, 0);
        this.magazine.runRaw(0, 0, 0);
        this.climber.run(0);
        this.shooter.stop();
        this.shooter.enableTurret(false);
    }
}

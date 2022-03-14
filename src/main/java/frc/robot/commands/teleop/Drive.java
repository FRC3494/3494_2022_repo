package frc.robot.commands.teleop;

import frc.robot.utilities.DiCommand;
import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.ITickable;
import frc.robot.OI;
import frc.robot.RobotConfig;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.AutoNav;
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

    @Inject
    AutoNav autoNav;

    boolean singing = false;
    boolean startedSinging = false;

    boolean autoClimberDeploy = false;
    int autoClimbStage = 0;
    Timer autoClimbTimer = new Timer();

    int selectedRPM = 0;

    public void onInitialize() {
        drivetrain.setDefaultCommand(this);
        autoClimbTimer.reset();
    }

    public void onTick() {
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

        //this.drivetrain.run(RobotConfig.Drivetrain.PowerCurve(this.oi.GetLeftDriveSpeed()), RobotConfig.Drivetrain.PowerCurve(this.oi.GetRightDriveSpeed()));

        this.drivetrain.run((this.oi.GetLeftDriveSpeed()), (this.oi.GetRightDriveSpeed()));

        double frontIntakeSpeed = (this.oi.GetClimberPower() == 0 && this.oi.GetClimberWithoutRachetPower() == 0) ? RobotConfig.Intake.PowerCurve(this.oi.GetFrontIntakePower()) * RobotConfig.Intake.INTAKE_SPEED : 0.00001f;
        double backIntakeSpeed = RobotConfig.Intake.PowerCurve(this.oi.GetBackIntakePower()) * RobotConfig.Intake.INTAKE_SPEED;

        if (!this.autoClimberDeploy) this.intake.run(frontIntakeSpeed, backIntakeSpeed);

        this.magazine.setAutoOperate(!this.oi.GetOverrideMagazineStateMachine());

        if (this.oi.GetOverrideMagazineStateMachine()) {
            double leftMagazineSpeed = this.oi.GetLeftTreeMagazinePower() * RobotConfig.Magazine.INTAKE_SPEED;
            double rightMagazineSpeed = this.oi.GetRightTreeMagazinePower() * RobotConfig.Magazine.INTAKE_SPEED;
            double stemMagazineSpeed = this.oi.GetTreeStemMagazinePower() * RobotConfig.Magazine.INTAKE_SPEED;
    
            if (this.oi.GetNeedOuttake()) {
                leftMagazineSpeed = RobotConfig.Magazine.OUTTAKE_SPEED;
                rightMagazineSpeed = RobotConfig.Magazine.OUTTAKE_SPEED;
                stemMagazineSpeed = RobotConfig.Magazine.OUTTAKE_SPEED;
            }
    
            this.magazine.run(leftMagazineSpeed, rightMagazineSpeed, stemMagazineSpeed);
        } else {
            if (this.oi.QueueBall()) this.magazine.sendBall();

            this.magazine.useFastSpeed(this.oi.GetFrontIntakePower() > 0 || this.oi.GetBackIntakePower() > 0);
        }
        

        //if (this.oi.GetOverrideMagazineStateMachine()) this.magazine.runRaw(leftMagazineSpeed, rightMagazineSpeed, stemMagazineSpeed);
        //else this.magazine.run(leftMagazineSpeed, rightMagazineSpeed, stemMagazineSpeed, this.oi.GetLeftTreeMagazinePower() > 0);

        this.climber.runRaw(RobotConfig.Climber.PowerCurve(this.oi.GetClimberPower() * RobotConfig.Climber.ANALOG_CLIMB_SPEED + this.oi.GetClimberWithoutRachetPower() * RobotConfig.Climber.BINARY_CLIMB_SPEED));
        
        if (!this.autoClimberDeploy && this.oi.GetClimberAutoDeploy()) {
            this.autoClimbTimer.reset();
            this.autoClimbTimer.start();

            this.autoClimberDeploy = true;
        }

        if (this.autoClimberDeploy) this.runClimberAutoDeploy();
        else this.runClimberManual();

        this.shooter.run(RobotConfig.Shooter.RPMPowerCurve(this.oi.GetShooterPower()) * RobotConfig.Shooter.RPMS.get(this.selectedRPM).Value);
        this.shooter.runTurret(RobotConfig.Shooter.TurretPowerCurve(this.oi.GetTurretPower()) * RobotConfig.Shooter.TURRET_SPEED);

        if (this.oi.ToggleAimBot()) {
            if (this.shooter.aimbotEnabled()) this.shooter.disableAimBot();
            else this.shooter.enableAimBot();
        }

        if (this.oi.GetSwitchRPM()) {
            this.selectedRPM++;
            if (this.selectedRPM >= RobotConfig.Shooter.RPMS.size()) this.selectedRPM = 0;
        }

        if (this.oi.ToggleShooterHood()) this.shooter.setHood(!this.shooter.getHood());

        //if (this.oi.StartSinging()) this.singing = true;
    }

    public void runClimberAutoDeploy() {
        this.climber.release(true);
        this.intake.run(0.00001f, 0);

        switch (autoClimbStage) {
            case 0:
                if (this.autoClimbTimer.advanceIfElapsed(1000)) this.autoClimbStage++; 

                this.climber.runRaw(this.climberReleaseEquation(this.autoClimbTimer.get() * 1000));
            case 1:
                if (this.autoClimbTimer.advanceIfElapsed(3000)) this.autoClimbStage++; 

                this.climber.runRaw(1);
            default:
                this.autoClimberDeploy = false;
        }

    }

    public double climberReleaseEquation(double t) {
        return (1 / 90000) * Math.pow(t - 300, 2) - 1;
    }

    public void runClimberManual() {
        if (this.oi.GetClimberPower() < 0 || this.oi.GetClimberWithoutRachetPower() != 0) this.climber.release(true);
        if (this.oi.GetClimberPower() > 0) this.climber.release(false);
    }

    public void onDispose() {
        this.drivetrain.run(0, 0);
        this.intake.run(0, 0);
        this.magazine.runRaw(0, 0, 0);
        this.climber.run(0);
        this.shooter.run(0);
        this.shooter.runTurret(0);
        this.shooter.disableAimBot();
    }
}

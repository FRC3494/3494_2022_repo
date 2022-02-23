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
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj.DriverStation;

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

    public void onInitialize() {
        drivetrain.setDefaultCommand(this);
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

        this.intake.run(RobotConfig.Intake.PowerCurve(this.oi.GetIntakeSpeed()));

        double magazineSpeed = (this.oi.GetNeedOuttake()) ? RobotConfig.Magazine.OUTTAKE_SPEED : ((this.oi.GetIntakeSpeed() != 0) ? RobotConfig.Magazine.INTAKE_SPEED : ((this.oi.GetMagazineIdle()) ? RobotConfig.Magazine.IDLE_SPEED : 0));
        
        if (this.oi.GetOverrideMagazineStateMachine()) this.magazine.runRaw(magazineSpeed);
        else this.magazine.run(magazineSpeed);

        this.climber.run(RobotConfig.Climber.PowerCurve(this.oi.GetClimberPower()));

        this.shooter.run(RobotConfig.Shooter.RPMPowerCurve(this.oi.GetShooterPower()) * RobotConfig.Shooter.BASE_TARGET_RPM);

        this.shooter.runTurret(RobotConfig.Shooter.TurretPowerCurve(this.oi.GetTurretPower()));

        if (this.oi.StartSinging()) this.singing = true;
    }

    public void onDispose() {
        this.drivetrain.run(0, 0);
        this.intake.run(0);
        this.magazine.runRaw(0);
        this.climber.run(0);
        this.shooter.run(0);
        this.shooter.runTurret(0);
    }
}

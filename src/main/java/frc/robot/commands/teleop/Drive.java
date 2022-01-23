package frc.robot.commands.teleop;

import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.ITickable;
import frc.robot.OI;
import frc.robot.RobotConfig;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Drive extends CommandBase implements ITickable, IDisposable {
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

    public void onTick() {
        if (this.singing) {
            if (!this.startedSinging) {
                this.drivetrain.singTheTheme();
                this.startedSinging = true;
            }
            return;
        }

        this.drivetrain.run(this.PowerCurve(this.oi.GetLeftDriveSpeed()), this.PowerCurve(this.oi.GetRightDriveSpeed()));

        this.intake.run(this.oi.GetIntakeSpeed());
        this.magazine.run((this.oi.GetNeedOuttake()) ? RobotConfig.Magazine.OUTTAKE_SPEED : ((this.oi.GetIntakeSpeed() != 0) ? RobotConfig.Magazine.INTAKE_SPEED : RobotConfig.Magazine.IDLE_SPEED));

        if (this.oi.GetClimberRelease()) this.climber.release();
        this.climber.run(this.oi.GetClimberPower());

        this.shooter.run(this.oi.GetShooterPower() * RobotConfig.Shooter.BASE_TARGET_RPM);

        this.shooter.runTurret(this.oi.GetTurretPower());

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

    public double PowerCurve(double x) {
        if (x > 1) return 1;
        if (x < -1) return -1;

        return Math.pow(x, 3);
    }
}

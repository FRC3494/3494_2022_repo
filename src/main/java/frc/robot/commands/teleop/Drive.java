package frc.robot.commands.teleop;

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
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Drive extends CommandBase implements IInitializable, ITickable, IDisposable {
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

    @Override
    public void Initialize() {
        addRequirements(this.drivetrain);
        addRequirements(this.intake);
        addRequirements(this.magazine);
        addRequirements(this.climber);
        addRequirements(this.shooter);
    }

    @Override
    public void Tick() {
        if (this.singing) {
            if (!this.startedSinging) {
                this.drivetrain.SingTheTheme();
                this.startedSinging = true;
            }
            return;
        }

        this.drivetrain.Tank(this.PowerCurve(this.oi.GetLeftDriveSpeed()), this.PowerCurve(this.oi.GetRightDriveSpeed()));

        this.intake.Run(this.oi.GetIntakeSpeed());
        this.magazine.Run((this.oi.GetNeedOuttake()) ? RobotConfig.Magazine.OuttakeSpeed : ((this.oi.GetIntakeSpeed() != 0) ? RobotConfig.Magazine.IntakeSpeed : RobotConfig.Magazine.IdleSpeed));

        if (this.oi.GetClimberRelease()) this.climber.ReleaseClimber();
        this.climber.Run(this.oi.GetClimberPower());

        this.shooter.Run(this.oi.GetShooterPower() * RobotConfig.Shooter.BaseTargetRPM);

        if (this.oi.StartSinging()) this.singing = true;
    }

    @Override
    public void Dispose() {
        this.drivetrain.Tank(0, 0);
        this.intake.Run(0);
        this.magazine.RunRaw(0);
        this.climber.Run(0);
        this.shooter.Run(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    public double PowerCurve(double x) {
        if (x > 1) return 1;
        if (x < -1) return -1;

        return Math.pow(x, 3);
    }
}

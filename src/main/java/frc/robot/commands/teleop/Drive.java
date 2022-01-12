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
    private OI oi;

    @Inject
    private DriverStation.Alliance alliance;

    @Inject
    private Drivetrain drivetrain;

    @Inject
    private Intake intake;

    @Inject
    private Magazine magazine;

    @Inject
    private Climber climber;

    @Inject
    private Shooter shooter;

    public boolean singing = false;
    public boolean startedSinging = false;

    @Override
    public void Initialize() {
        addRequirements(drivetrain);
        addRequirements(intake);
        addRequirements(magazine);
        addRequirements(climber);
        addRequirements(shooter);
    }

    @Override
    public void Tick() {
        if (singing) {
            if (!startedSinging) {
                drivetrain.SingTheTheme();
                startedSinging = true;
            }
            return;
        }

        drivetrain.Tank(PowerCurve(oi.GetLeftDriveSpeed()), PowerCurve(oi.GetRightDriveSpeed()));

        intake.Run(oi.GetIntakeSpeed());
        magazine.Run((oi.GetNeedOuttake()) ? RobotConfig.Magazine.outtakeSpeed : ((oi.GetIntakeSpeed() != 0) ? RobotConfig.Magazine.intakeSpeed : RobotConfig.Magazine.idleSpeed));

        if (oi.GetClimberRelease()) climber.ReleaseClimber();
        climber.Run(oi.GetClimberPower());

        shooter.Shoot(oi.GetShooterPower() * RobotConfig.Shooter.baseTargetRPM);

        if (oi.StartSinging()) singing = true;
    }

    @Override
    public void Dispose() {
        drivetrain.Tank(0, 0);
        intake.Run(0);
        magazine.RunRaw(0);
        climber.Run(0);
        shooter.Shoot(0);
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

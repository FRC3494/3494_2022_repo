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

    @Override
    public void initialize() {
        this.addRequirements(drivetrain);
        this.addRequirements(intake);
        this.addRequirements(magazine);
        this.addRequirements(climber);
        this.addRequirements(shooter);
    }

    @Override
    public void tick() {
        drivetrain.tankDrive(PowerCurve(oi.GetLeftDriveSpeed()), PowerCurve(oi.GetRightDriveSpeed()));

        intake.run(oi.GetIntakeSpeed());
        magazine.run((oi.GetNeedOuttake()) ? RobotConfig.Magazine.OUTTAKE_SPEED : ((oi.GetIntakeSpeed() != 0) ? RobotConfig.Magazine.INTAKE_SPEED : RobotConfig.Magazine.IDLE_SPEED));

        if (oi.GetClimberRelease()) climber.ReleaseClimber();
        climber.Climb(oi.GetClimberPower());

        shooter.shoot(oi.GetShooterPower() * RobotConfig.Shooter.BASE_TARGET_RPM);
    }

    @Override
    public void dispose() {
        drivetrain.tankDrive(0, 0);
        intake.run(0);
        magazine.runRaw(0);
        climber.Climb(0);
        shooter.shoot(0);
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

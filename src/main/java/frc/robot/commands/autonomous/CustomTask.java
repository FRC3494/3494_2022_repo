package frc.robot.commands.autonomous;

import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.utilities.AutoTask;
import frc.robot.utilities.di.DiContainer.Inject;

public abstract class CustomTask extends AutoTask {
    @Inject
    Drivetrain drivetrain;
    @Inject
    Shooter shooter;
    @Inject
    Intake intake;
    @Inject
    Magazine magazine;
    @Inject
    Climber climber;

    public CustomTask() {

    }

    public abstract void begin();
    public abstract boolean execute();
    public abstract void stop();

    @Override
    public void Begin() {
        begin();
    }

    @Override
    public boolean Execute() {
        return execute();
    }

    @Override
    public void Stop() {
        stop();
    }

    @Override
    public ETA GetETA() {
        return new ETA();
    }
}
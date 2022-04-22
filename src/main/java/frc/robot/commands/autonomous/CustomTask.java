package frc.robot.commands.autonomous;

import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.utilities.AutoTask;
import com.fizzyapple12.javadi.DiContainer.Inject;

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

    public abstract void customBegin();
    public abstract boolean customExecute();
    public abstract void customStop();

    @Override
    public void begin() {
        this.customBegin();
    }

    @Override
    public boolean execute() {
        return this.customExecute();
    }

    @Override
    public void stop() {
        this.customStop();
    }

    @Override
    public ETA getETA() {
        return new ETA();
    }
}
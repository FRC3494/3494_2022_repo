package frc.robot.commands.autonomous;

import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Shooter;
import frc.robot.utilities.AutoTask;
import frc.robot.utilities.di.DiContainer.Inject;

public abstract class CustomTimedTask extends AutoTask {
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
    
    float millis;

    float endTime;

    public CustomTimedTask(float millis) {
        this.millis = millis;
    }

    public abstract void customBegin();
    public abstract void customExecute();
    public abstract void customStop();

    @Override
    public void begin() {
        this.customBegin();

        this.endTime = System.currentTimeMillis() + this.millis;
    }

    @Override
    public boolean execute() {
        this.customExecute();

        return (this.endTime < System.currentTimeMillis());
    }

    @Override
    public void stop() {
        this.customStop();
    }

    @Override
    public ETA getETA() {
        return new ETA(this.endTime - this.millis, System.currentTimeMillis(), this.endTime);
    }
}

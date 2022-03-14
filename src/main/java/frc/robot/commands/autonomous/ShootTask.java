package frc.robot.commands.autonomous;

import frc.robot.subsystems.Shooter;
import frc.robot.utilities.AutoTask;
import frc.robot.utilities.di.DiContainer.Inject;

public class ShootTask extends AutoTask {
    @Inject
    Shooter shooter;

    double rpm;
    boolean hood;

    public ShootTask(double rpm, boolean hood) {
        this.rpm = rpm;
        this.hood = hood;
    }

    @Override
    public void begin() {
        this.shooter.run(this.rpm);
        this.shooter.setHood(this.hood);
    }

    @Override
    public boolean execute() {
        return true;
    }

    @Override
    public void stop() {
        
    }

    @Override
    public ETA getETA() {
        return new ETA();
    }
}

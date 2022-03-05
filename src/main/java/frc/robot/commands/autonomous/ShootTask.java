package frc.robot.commands.autonomous;

import frc.robot.subsystems.Shooter;
import frc.robot.utilities.AutoTask;
import frc.robot.utilities.di.DiContainer.Inject;

public class ShootTask extends AutoTask {
    @Inject
    Shooter shooter;

    double rpm;

    public ShootTask(double rpm) {
        this.rpm = rpm;
    }

    @Override
    public void begin() {
        this.shooter.run(this.rpm);
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

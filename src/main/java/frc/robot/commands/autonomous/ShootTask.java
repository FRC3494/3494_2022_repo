package frc.robot.commands.autonomous;

import frc.robot.subsystems.Shooter;
import frc.robot.utilities.AutoTask;
import frc.robot.utilities.ShooterSetting;
import frc.robot.utilities.di.DiContainer.Inject;

public class ShootTask extends AutoTask {
    @Inject
    Shooter shooter;

    ShooterSetting setting;

    public ShootTask(ShooterSetting setting) {
        this.setting = setting;
    }

    @Override
    public void begin() {
        this.shooter.run(this.setting);
    }

    @Override
    public boolean execute() {
        return this.shooter.atRPM();
    }

    @Override
    public void stop() {
        
    }

    @Override
    public ETA getETA() {
        return new ETA();
    }
}

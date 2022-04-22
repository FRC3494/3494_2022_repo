package frc.robot.commands.autonomous;

import frc.robot.subsystems.Shooter;
import frc.robot.utilities.AutoTask;
import com.fizzyapple12.javadi.DiContainer.Inject;

public class AimbotTask extends AutoTask {
    @Inject
    Shooter shooter;

    boolean enable;

    public AimbotTask(boolean enable) {
        this.enable = enable;
    }

    @Override
    public void begin() {
        if (this.enable) this.shooter.enableAimBot();
        this.shooter.disableAimBot();
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

package frc.robot.commands.autonomous;

import frc.robot.subsystems.Shooter;
import frc.robot.utilities.AutoTask;
import com.fizzyapple12.javadi.DiContainer.Inject;

public class ZeroTurretTask extends AutoTask {
    @Inject
    Shooter shooter;

    boolean doReturn = false;

    public ZeroTurretTask() {
    }

    @Override
    public void begin() {
        this.shooter.enableTurret(true);

        if (this.shooter.isZeroing()) this.shooter.rezero();
    }
 
    @Override
    public boolean execute() {
        return this.shooter.isZeroing();
    }

    @Override
    public void stop() {}

    @Override
    public ETA getETA() {
        return new ETA();
    }
}

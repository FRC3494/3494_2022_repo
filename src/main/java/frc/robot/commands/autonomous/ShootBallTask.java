package frc.robot.commands.autonomous;

import frc.robot.subsystems.Magazine;
import frc.robot.utilities.AutoTask;
import frc.robot.utilities.di.DiContainer.Inject;

public class ShootBallTask extends AutoTask {
    @Inject
    Magazine magazine;

    public ShootBallTask() {
    }

    @Override
    public void begin() {
        this.magazine.sendBall();
    }

    @Override
    public boolean execute() {
        return true;
    }

    @Override
    public void stop() {}

    @Override
    public ETA getETA() {
        return new ETA();
    }
}

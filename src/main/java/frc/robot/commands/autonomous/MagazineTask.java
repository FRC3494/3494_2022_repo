package frc.robot.commands.autonomous;

import frc.robot.subsystems.Magazine;
import frc.robot.utilities.AutoTask;
import frc.robot.utilities.di.DiContainer.Inject;

public class MagazineTask extends AutoTask {
    @Inject
    Magazine magazine;

    double leftPower;
    double rightPower;
    double verticalPower;
    boolean sendShooter;

    public MagazineTask(double leftPower, double rightPower, double verticalPower, boolean sendShooter) {
        this.leftPower = leftPower;
        this.rightPower = rightPower;
        this.verticalPower = verticalPower;
        this.sendShooter = sendShooter;
    }

    @Override
    public void begin() {
        this.magazine.run(this.leftPower, this.rightPower, this.verticalPower, this.sendShooter);
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

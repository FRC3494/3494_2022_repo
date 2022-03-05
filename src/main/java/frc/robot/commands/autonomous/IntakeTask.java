package frc.robot.commands.autonomous;

import frc.robot.subsystems.Magazine;
import frc.robot.subsystems.Intake;
import frc.robot.utilities.AutoTask;
import frc.robot.utilities.di.DiContainer.Inject;

public class IntakeTask extends AutoTask {
    @Inject
    Intake intake;
    @Inject
    Magazine magazine;

    double leftPower;
    double rightPower;

    public IntakeTask(double leftPower, double rightPower) {
        this.leftPower = leftPower;
        this.rightPower = rightPower;
    }

    @Override
    public void begin() {
        this.intake.run(this.leftPower, this.rightPower);
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

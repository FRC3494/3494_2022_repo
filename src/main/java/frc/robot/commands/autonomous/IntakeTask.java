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

    double power;

    public IntakeTask(double power) {
        this.power = power;
    }

    @Override
    public void begin() {
        this.intake.run(this.power);
        this.magazine.run(this.power);
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

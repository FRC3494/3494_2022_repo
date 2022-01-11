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
    public void Begin() {
        intake.Intake(power);
        magazine.Run(power);
    }

    @Override
    public boolean Execute() {
        return true;
    }

    @Override
    public void Stop() {}

    @Override
    public ETA GetETA() {
        return new ETA();
    }
}

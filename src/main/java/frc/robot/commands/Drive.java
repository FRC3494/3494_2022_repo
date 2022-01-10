package frc.robot.commands;

import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.ITickable;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Drive extends CommandBase implements IInitializable, ITickable, IDisposable {
    @Inject()
    private Drivetrain drivetrain;

    @Override
    public void Initialize() {
        addRequirements(drivetrain);
    }

    @Override
    public void Tick() {
    }

    @Override
    public void Dispose() {
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}

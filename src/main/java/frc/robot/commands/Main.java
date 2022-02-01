package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Pneumatics;
import frc.robot.utilities.di.DiContainer.Inject;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class Main extends CommandBase implements IInitializable, IDisposable {
    @Inject
    Pneumatics pneumatics;

    public void onInitialize() {
        //this.pneumatics.enable();
    }

    public void onDispose() {
        //this.pneumatics.disable();
    }
}

package frc.robot.opmodes.teleop;

import java.lang.reflect.InvocationTargetException;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.OI;
import frc.robot.commands.teleop.Drive;
import frc.robot.utilities.DiOpMode;

public class Teleop extends DiOpMode {
    @Override
    public void Install() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        this.Container.BindInstance(new XboxController(0)).WithId("primaryXbox");
        this.Container.BindInstance(new XboxController(1)).WithId("secondaryXbox");
        
        this.Container.Bind(OI.class);

        this.Container.Bind(Drive.class);
    }
}

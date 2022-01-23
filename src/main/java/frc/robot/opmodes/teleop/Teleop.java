package frc.robot.opmodes.teleop;

import java.lang.reflect.InvocationTargetException;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.OI;
import frc.robot.commands.teleop.Drive;
import frc.robot.utilities.DiOpMode;

public class Teleop extends DiOpMode {
    @Override
    public void install() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        this.Container.bindInstance(new XboxController(0)).withId("primaryXbox");
        this.Container.bindInstance(new XboxController(1)).withId("secondaryXbox");
        
        this.Container.bind(OI.class);

        this.Container.bind(Drive.class);
    }
}

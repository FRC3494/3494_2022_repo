package frc.robot.opmodes.teleop;

import java.lang.reflect.InvocationTargetException;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.OI;
import frc.robot.commands.teleop.Drive;
import frc.robot.utilities.DiOpMode;

public class Teleop extends DiOpMode {
    @Override
    public void install() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        this.m_container.bindInstance(new XboxController(0)).withId("primaryXbox");
        this.m_container.bindInstance(new XboxController(1)).withId("secondaryXbox");
        
        this.m_container.bind(OI.class);

        this.m_container.bind(Drive.class);
    }
}

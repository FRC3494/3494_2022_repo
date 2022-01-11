package frc.robot.opmodes.teleop;

import java.lang.reflect.InvocationTargetException;

import frc.robot.OI;
import frc.robot.commands.teleop.Drive;
import frc.robot.utilities.DiOpMode;

public class Teleop extends DiOpMode {
    @Override
    public void Install() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Container.Bind(OI.class);

        Container.Bind(Drive.class);
    }
}

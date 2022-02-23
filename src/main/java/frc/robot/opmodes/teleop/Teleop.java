package frc.robot.opmodes.teleop;

import java.lang.reflect.InvocationTargetException;

import frc.robot.OI;
import frc.robot.commands.teleop.Drive;
import frc.robot.utilities.DiOpMode;

public class Teleop extends DiOpMode {
    @Override
    public void install() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        this.Container.bind(OI.class).asSingle();

        this.Container.bind(Drive.class).asSingle();
    }
}

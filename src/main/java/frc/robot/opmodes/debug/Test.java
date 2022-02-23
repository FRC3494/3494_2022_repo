package frc.robot.opmodes.debug;

import java.lang.reflect.InvocationTargetException;

/*import frc.robot.commands.debug.TestClimber;
import frc.robot.commands.debug.TestDrivetrain;
import frc.robot.commands.debug.TestIntake;
import frc.robot.commands.debug.TestMagazine;
import frc.robot.commands.debug.TestShooter;*/
import frc.robot.utilities.DiOpMode;
import frc.robot.utilities.TestProctor;

public class Test extends DiOpMode {
    @Override
    public void install() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        this.Container.bind(TestProctor.class).asSingle();

        /*this.Container.bind(TestDrivetrain.class).asSingle();
        this.Container.bind(TestIntake.class).asSingle();
        this.Container.bind(TestMagazine.class).asSingle();
        this.Container.bind(TestShooter.class).asSingle();
        this.Container.bind(TestClimber.class).asSingle();*/
    }
}

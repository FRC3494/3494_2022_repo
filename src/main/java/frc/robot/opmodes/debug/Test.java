package frc.robot.opmodes.debug;

import java.lang.reflect.InvocationTargetException;

import com.fizzyapple12.wpilibdi.DiOpMode;
import com.fizzyapple12.wpilibdi.DiTestProctor;

public class Test extends DiOpMode {
    @Override
    public void install() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        this.Container.bind(DiTestProctor.class).asSingle();

        /*this.Container.bind(TestDrivetrain.class).asSingle();
        this.Container.bind(TestIntake.class).asSingle();
        this.Container.bind(TestMagazine.class).asSingle();
        this.Container.bind(TestShooter.class).asSingle();
        this.Container.bind(TestClimber.class).asSingle();*/
    }
}

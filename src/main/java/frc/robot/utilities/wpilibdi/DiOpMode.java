package frc.robot.utilities.wpilibdi;

import java.lang.reflect.InvocationTargetException;

public abstract class DiOpMode {
    public WPILibDiContainer Container = new WPILibDiContainer();
    
    public abstract void install() throws IllegalAccessException, InstantiationException, InvocationTargetException;
}

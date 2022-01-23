package frc.robot.utilities;

import java.lang.reflect.InvocationTargetException;

import frc.robot.utilities.di.DiContainer;

public abstract class DiOpMode {
    public DiContainer Container = new DiContainer();
    
    public abstract void install() throws IllegalAccessException, InstantiationException, InvocationTargetException;
}

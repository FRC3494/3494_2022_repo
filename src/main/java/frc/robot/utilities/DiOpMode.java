package frc.robot.utilities;

import java.lang.reflect.InvocationTargetException;

import frc.robot.utilities.di.DiContainer;

public abstract class DiOpMode {
    protected DiContainer Container = new DiContainer();
    
    public abstract void Install() throws IllegalAccessException, InstantiationException, InvocationTargetException;
}

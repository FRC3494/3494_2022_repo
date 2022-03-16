package frc.robot.utilities.wpilibdi;

import java.lang.reflect.InvocationTargetException;
import java.util.WeakHashMap;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.utilities.di.DiContainer;
import frc.robot.utilities.di.DiInterfaces;

public class WPILibDiContainer extends DiContainer {

    /**
    * Initializes all objects in the object pool
    * Doesn't check if initialization has already happened, please only call this once
    *
    * @see DiContext
    */
    public void onInject() {
        for (Object objectInstance : this.objectPool.values()) {
            if (objectInstance instanceof DiInterfaces.IInitializable && !(objectInstance instanceof DiCommand) && !(objectInstance instanceof DiSubsystem)) {
                ((DiInterfaces.IInitializable) objectInstance).onInitialize();
            }
        }
    }

    /**
    * Ticks all objects in the object pool
    *
    * @see DiContext
    */
    public void onTick() {
        for (Object objectInstance : this.objectPool.values()) {
            if (objectInstance instanceof DiInterfaces.ITickable && !(objectInstance instanceof DiCommand) && !(objectInstance instanceof DiSubsystem)) {
                ((DiInterfaces.ITickable) objectInstance).onTick();
            }
        }
    }

    /**
    * Disposes all objects in the object pool
    *
    * @see DiContext
    */
    public void onDispose() {
        for (Object objectInstance : this.objectPool.values()) {
            if (objectInstance instanceof DiInterfaces.IDisposable && !(objectInstance instanceof DiCommand) && !(objectInstance instanceof DiSubsystem)) {
                ((DiInterfaces.IDisposable) objectInstance).onDispose();
                
            }
            if (objectInstance instanceof DiCommand) CommandScheduler.getInstance().cancel((DiCommand) objectInstance);
        }

        CommandScheduler.getInstance().run();

        for (Object objectInstance : this.objectPool.values()) {
            if (objectInstance instanceof DiSubsystem) CommandScheduler.getInstance().unregisterSubsystem((DiSubsystem) objectInstance);
        }

        CommandScheduler.getInstance().run();

        this.objectPool.clear();
        this.objectPool = new WeakHashMap<>();

        System.gc();
    }
    
    /**
    * Creates a new set of rules which registers raw classes with the Conteiners
    *
    * @param inClasses The classes to bind
    * @return A new RuleBuilder 
    * @throws DiExceptions.RuleBuilderException Caused by having an improper configuration prior to trying to set resolution
    * @see DiRule
    */
    public WPILibDiRuleBuilder bind(Class<?>... inClasses) {
        WPILibDiRuleBuilder diRuleBuilder = new WPILibDiRuleBuilder(this);

        diRuleBuilder.bind(inClasses);

        return diRuleBuilder;
    }

    /**
    * Creates a new rule which registers an instance to be resolved by the Container
    *
    * @param instance The instance to add to the Container
    * @return A new RuleBuilder 
    * @throws DiExceptions.RuleBuilderException Caused by having an improper configuration prior to trying to set resolution
    * @see DiRule
    */
    public WPILibDiRuleBuilder bindInstance(Object instance) {
        WPILibDiRuleBuilder diRuleBuilder = new WPILibDiRuleBuilder(this);

        diRuleBuilder.bindInstance(instance);

        return diRuleBuilder;
    }

    /**
    * Creates a new set of rules which registers multiple instances to be resolved by the Container
    *
    * @param instances The instances to add to the Container
    * @return A new RuleBuilder 
    * @throws DiExceptions.RuleBuilderException Caused by having an improper configuration prior to trying to set resolution
    * @see DiRule
    */
    public WPILibDiRuleBuilder bindInstances(Object... instances) {
        WPILibDiRuleBuilder diRuleBuilder = new WPILibDiRuleBuilder(this);

        diRuleBuilder.bindInstances(instances);

        return diRuleBuilder;
    }

    /**
    * Creates a new rule which registers a class' interfaces to resolve to one single class 
    *
    * @param inClass The class to get interfaces from and bind
    * @return A new RuleBuilder 
    * @throws IllegalAccessException Can be caused by the reflection, out of my control
    * @throws InvocationTargetException Can be caused by the reflection, out of my control
    * @throws InstantiateException Can be caused if the Container fails to instantiate a class
    * @see DiRule
    */
    public WPILibDiRuleBuilder bindInterfacesTo(Class<?> inClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        WPILibDiRuleBuilder diRuleBuilder = new WPILibDiRuleBuilder(this);

        diRuleBuilder.bindInterfacesTo(inClass);

        return diRuleBuilder;
    }

    /**
    * Creates a new rule which registers a class and its interfaces to resolve to one single class 
    *
    * @param inClass The class to get interfaces from and bind
    * @return A new RuleBuilder 
    * @throws IllegalAccessException Can be caused by the reflection, out of my control
    * @throws InvocationTargetException Can be caused by the reflection, out of my control
    * @throws InstantiateException Can be caused if the Container fails to instantiate a class
    * @see DiRule
    */
    public WPILibDiRuleBuilder bindInterfacesAndSelfTo(Class<?> inClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        WPILibDiRuleBuilder diRuleBuilder = new WPILibDiRuleBuilder(this);

        diRuleBuilder.bindInterfacesAndSelfTo(inClass);

        return diRuleBuilder;
    }

    //wpilib specific

    public WPILibDiRuleBuilder bindCommand(Class<?> commandClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        WPILibDiRuleBuilder diRuleBuilder = new WPILibDiRuleBuilder(this);

        diRuleBuilder.bindCommand(commandClass);

        return diRuleBuilder;
    }

    public WPILibDiRuleBuilder bindSubsystem(Class<?> subsystemClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        WPILibDiRuleBuilder diRuleBuilder = new WPILibDiRuleBuilder(this);

        diRuleBuilder.bindSubsystem(subsystemClass);

        return diRuleBuilder;
    }
}

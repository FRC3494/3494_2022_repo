package frc.robot.utilities;

import java.lang.reflect.Field;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.IInjected;
import frc.robot.utilities.di.DiInterfaces.ITickable;

public abstract class DiCommand extends CommandBase implements IInjected {
    protected boolean isFinished = false;

    private boolean isInitializable = false;
    private boolean isTickable = false;
    private boolean isDisposable = false;

    private boolean needsInitialization = true;
    private boolean waitingForInject = true;
    private boolean waitingForInitialize = true;

    public void onInject() {
        if (!needsInitialization) return;

        waitingForInject = false;

        if (!waitingForInitialize) {
            internalInitialize();

            needsInitialization = false;
        }
    }

    @Override
    public void initialize() {
        if (!needsInitialization) return;

        waitingForInitialize = false;

        if (!waitingForInject) {
            internalInitialize();

            needsInitialization = false;
        }
    }

    private void internalInitialize() {
        if (this instanceof IInitializable) isInitializable = true;
        if (this instanceof ITickable) isTickable = true;
        if (this instanceof IDisposable) isDisposable = true;

        for (Field field : this.getClass().getFields()) {
            if (!field.getType().isAssignableFrom(Subsystem.class)) continue;

            try {
                addRequirements((Subsystem) field.get(this));
            } catch (Exception e) {
                isInitializable = false;
                isTickable = false;
                isDisposable = false;
                
                isFinished = true;
                
                e.printStackTrace();
            }
        }

        if (isInitializable) ((IInitializable) this).onInitialize();
    }

    @Override
    public void execute() {
        if (needsInitialization) {
            if (!waitingForInject && !waitingForInitialize) {
                internalInitialize();
    
                needsInitialization = false;
            }

            return;
        } 

        if (this.isTickable) ((ITickable) this).onTick();
    }

    @Override
    public void end(boolean interrupted) {
        if (isDisposable) ((IDisposable) this).onDispose();
    }
  
    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
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
        System.out.println("commandy init1");
        if (!this.needsInitialization) return;

        this.waitingForInject = false;

        if (!this.waitingForInitialize) {
            this.internalInitialize();

            this.needsInitialization = false;
        }
    }

    @Override
    public void initialize() {
        System.out.println("commandy init2");
        if (!this.needsInitialization) return;

        this.waitingForInitialize = false;

        if (!this.waitingForInject) {
            this.internalInitialize();

            this.needsInitialization = false;
        }
    }

    private void internalInitialize() {
        System.out.println("commandy init!");

        if (this instanceof IInitializable) this.isInitializable = true;
        if (this instanceof ITickable) this.isTickable = true;
        if (this instanceof IDisposable) this.isDisposable = true;

        for (Field field : this.getClass().getFields()) {
            if (!field.getType().isAssignableFrom(Subsystem.class)) continue;

            try {
                this.addRequirements((Subsystem) field.get(this));
            } catch (Exception e) {
                this.isInitializable = false;
                this.isTickable = false;
                this.isDisposable = false;
                
                this.isFinished = true;
                
                e.printStackTrace();
            }
        }

        if (this.isInitializable) ((IInitializable) this).onInitialize();
    }

    @Override
    public void execute() {
        System.out.println("commandy ticky");
        
        if (this.needsInitialization) {
            if (!this.waitingForInject && !this.waitingForInitialize) {
                this.internalInitialize();
    
                this.needsInitialization = false;
            }

            return;
        } 

        if (this.isTickable) ((ITickable) this).onTick();
    }

    @Override
    public void end(boolean interrupted) {
        if (this.isDisposable) ((IDisposable) this).onDispose();
    }
  
    @Override
    public boolean isFinished() {
        return this.isFinished;
    }
}
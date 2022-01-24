package frc.robot.utilities;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.di.DiInterfaces.IInjected;
import frc.robot.utilities.di.DiInterfaces.ITickable;

public abstract class DiSubsystem extends SubsystemBase implements IInjected {
    protected boolean isFinished = false;

    private boolean isTickable = false;

    private boolean needsInitialization = true;

    public void onInject() {
        if (!this.needsInitialization) return;

        this.internalInitialize();
    }

    private void internalInitialize() {
        if (this instanceof IInitializable) ((IInitializable) this).onInitialize();
        if (this instanceof ITickable) this.isTickable = true;
    }

    @Override
    public void periodic() {
        if (this.needsInitialization) return;

        if (this.isTickable) ((ITickable) this).onTick();
    }
}
package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class Pneumatics extends DiSubsystem implements IInitializable, IDisposable {
    private Compressor compressor = new Compressor(PneumaticsModuleType.CTREPCM);

    public void onInitialize() {
        this.compressor.enableDigital();
    }

    public void onDispose() {
        this.compressor.disable();
    }
}

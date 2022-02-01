package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.RobotMap;
import frc.robot.utilities.DiSubsystem;
import frc.robot.utilities.di.DiInterfaces.IInitializable;

public class Pneumatics extends DiSubsystem implements IInitializable {
    private Compressor compressor = new Compressor(RobotMap.Pneumatics.BASE_PCM, PneumaticsModuleType.CTREPCM);

    public void onInitialize() {
        
    }

    public void enable() {
        this.compressor.enableDigital();
    }

    public void disable() {
        this.compressor.disable();
    }
}

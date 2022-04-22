package frc.robot.subsystems;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.RobotMap;
import com.fizzyapple12.wpilibdi.DiSubsystem;

public class Pneumatics extends DiSubsystem {
    private Compressor baseCompressor = new Compressor(RobotMap.Pneumatics.BASE_PCM, PneumaticsModuleType.CTREPCM);

    @SuppressWarnings("unused")
    private Compressor shooterCompressor = new Compressor(RobotMap.Pneumatics.SHOOTER_PCM, PneumaticsModuleType.CTREPCM);

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Pneumatics");

        builder.addBooleanProperty("Compressing", () -> {
            return this.baseCompressor.getPressureSwitchValue();
        }, (boolean value) -> { });
    }
}

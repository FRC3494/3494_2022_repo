package frc.robot.subsystems;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import frc.robot.RobotMap;
import frc.robot.utilities.DiSubsystem;

public class Pneumatics extends DiSubsystem {
    private Compressor compressor = new Compressor(RobotMap.Pneumatics.BASE_PCM, PneumaticsModuleType.CTREPCM);

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Pneumatics");

        builder.addBooleanProperty("Compressing", () -> {
            return this.compressor.getPressureSwitchValue();
        }, (boolean value) -> { });
    }
}

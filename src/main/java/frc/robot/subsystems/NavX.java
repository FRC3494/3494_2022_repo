package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.util.sendable.SendableBuilder;
import frc.robot.utilities.DiSubsystem;

public class NavX extends DiSubsystem {
    private AHRS ahrs = new AHRS();

    public double getYaw() {
        return this.ahrs.getFusedHeading();
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Gyro");
        builder.addDoubleProperty("Value", this::getYaw, null);
    }
}
package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.util.sendable.SendableBuilder;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.wpilibdi.DiSubsystem;

public class NavX extends DiSubsystem implements IInitializable {
    private AHRS ahrs = new AHRS();

    private double pitchOffset = 0;

    public void onInitialize() {
        //this.ahrs.calibrate();
        this.pitchOffset = this.ahrs.getRoll();
    }

    public double getYaw() {
        return this.ahrs.getFusedHeading();
    }

    public double getPitch() {
        //System.out.println(-this.ahrs.getRoll() + this.pitchOffset);
        return -this.ahrs.getRoll() + this.pitchOffset;
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("Gyro");
        builder.addDoubleProperty("Value", this::getYaw, null);
    }
}
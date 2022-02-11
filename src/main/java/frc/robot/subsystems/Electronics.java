package frc.robot.subsystems;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.PowerDistribution;
import frc.robot.utilities.DiSubsystem;

public class Electronics extends DiSubsystem {
    private PowerDistribution powerDistributionBoard = new PowerDistribution();

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("PDP");

        builder.addDoubleProperty("Voltage", this.powerDistributionBoard::getVoltage, (double value) -> { });
        builder.addDoubleProperty("Total Current", this.powerDistributionBoard::getTotalCurrent, (double value) -> { });

        builder.addDoubleProperty("Ch0", () -> { return this.powerDistributionBoard.getCurrent(0); }, (double value) -> { });
        builder.addDoubleProperty("Ch1", () -> { return this.powerDistributionBoard.getCurrent(1); }, (double value) -> { });
        builder.addDoubleProperty("Ch2", () -> { return this.powerDistributionBoard.getCurrent(2); }, (double value) -> { });
        builder.addDoubleProperty("Ch3", () -> { return this.powerDistributionBoard.getCurrent(3); }, (double value) -> { });
        builder.addDoubleProperty("Ch4", () -> { return this.powerDistributionBoard.getCurrent(4); }, (double value) -> { });
        builder.addDoubleProperty("Ch5", () -> { return this.powerDistributionBoard.getCurrent(5); }, (double value) -> { });
        builder.addDoubleProperty("Ch6", () -> { return this.powerDistributionBoard.getCurrent(6); }, (double value) -> { });
        builder.addDoubleProperty("Ch7", () -> { return this.powerDistributionBoard.getCurrent(7); }, (double value) -> { });
        builder.addDoubleProperty("Ch8", () -> { return this.powerDistributionBoard.getCurrent(8); }, (double value) -> { });
        builder.addDoubleProperty("Ch9", () -> { return this.powerDistributionBoard.getCurrent(9); }, (double value) -> { });
        builder.addDoubleProperty("Ch10", () -> { return this.powerDistributionBoard.getCurrent(10); }, (double value) -> { });
        builder.addDoubleProperty("Ch11", () -> { return this.powerDistributionBoard.getCurrent(11); }, (double value) -> { });
        builder.addDoubleProperty("Ch12", () -> { return this.powerDistributionBoard.getCurrent(12); }, (double value) -> { });
        builder.addDoubleProperty("Ch13", () -> { return this.powerDistributionBoard.getCurrent(13); }, (double value) -> { });
        builder.addDoubleProperty("Ch14", () -> { return this.powerDistributionBoard.getCurrent(14); }, (double value) -> { });
        builder.addDoubleProperty("Ch15", () -> { return this.powerDistributionBoard.getCurrent(15); }, (double value) -> { });

        /* for (int i = 0; i < this.powerDistributionBoard.getNumChannels(); i++) {
            builder.addDoubleProperty("Ch" + i, () -> { return this.powerDistributionBoard.getCurrent(i); }, (double value) -> { });
        } // cant do this because static references :wheeze: */
    }
}
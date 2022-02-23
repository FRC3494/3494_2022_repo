package frc.robot.subsystems;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import frc.robot.utilities.DiSubsystem;

public class Electronics extends DiSubsystem {
    private PowerDistribution powerDistributionBoard = new PowerDistribution();

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType(BuiltInWidgets.kPowerDistribution.getWidgetName());

        builder.addDoubleProperty("Voltage", this.powerDistributionBoard::getVoltage, (double value) -> { });
        builder.addDoubleProperty("TotalCurrent", this.powerDistributionBoard::getTotalCurrent, (double value) -> { });

        builder.addDoubleProperty("Chan0", () -> { return this.powerDistributionBoard.getCurrent(0); }, (double value) -> { });
        builder.addDoubleProperty("Chan1", () -> { return this.powerDistributionBoard.getCurrent(1); }, (double value) -> { });
        builder.addDoubleProperty("Chan2", () -> { return this.powerDistributionBoard.getCurrent(2); }, (double value) -> { });
        builder.addDoubleProperty("Chan3", () -> { return this.powerDistributionBoard.getCurrent(3); }, (double value) -> { });
        builder.addDoubleProperty("Chan4", () -> { return this.powerDistributionBoard.getCurrent(4); }, (double value) -> { });
        builder.addDoubleProperty("Chan5", () -> { return this.powerDistributionBoard.getCurrent(5); }, (double value) -> { });
        builder.addDoubleProperty("Chan6", () -> { return this.powerDistributionBoard.getCurrent(6); }, (double value) -> { });
        builder.addDoubleProperty("Chan7", () -> { return this.powerDistributionBoard.getCurrent(7); }, (double value) -> { });
        builder.addDoubleProperty("Chan8", () -> { return this.powerDistributionBoard.getCurrent(8); }, (double value) -> { });
        builder.addDoubleProperty("Chan9", () -> { return this.powerDistributionBoard.getCurrent(9); }, (double value) -> { });
        builder.addDoubleProperty("Chan10", () -> { return this.powerDistributionBoard.getCurrent(10); }, (double value) -> { });
        builder.addDoubleProperty("Chan11", () -> { return this.powerDistributionBoard.getCurrent(11); }, (double value) -> { });
        builder.addDoubleProperty("Chan12", () -> { return this.powerDistributionBoard.getCurrent(12); }, (double value) -> { });
        builder.addDoubleProperty("Chan13", () -> { return this.powerDistributionBoard.getCurrent(13); }, (double value) -> { });
        builder.addDoubleProperty("Chan14", () -> { return this.powerDistributionBoard.getCurrent(14); }, (double value) -> { });
        builder.addDoubleProperty("Chan15", () -> { return this.powerDistributionBoard.getCurrent(15); }, (double value) -> { });

        /* for (int i = 0; i < this.powerDistributionBoard.getNumChannels(); i++) {
            builder.addDoubleProperty("Ch" + i, () -> { return this.powerDistributionBoard.getCurrent(i); }, (double value) -> { });
        } // cant do this because static references :wheeze: */
    }
}
package frc.robot.utilities;

public class ShooterSetting {
    public String name;
    public double rpm;
    public boolean hood;
    public boolean feedThrough;
    public static ShooterSetting Off = new ShooterSetting("Off", 0, false, false);

    public ShooterSetting(String name, double rpm, boolean hood, boolean feedThrough) {
        this.name = name;
        this.rpm = rpm;
        this.hood = hood;
        this.feedThrough = feedThrough;

    }
}

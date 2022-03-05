package frc.robot.sensors;

import edu.wpi.first.wpilibj.DigitalInput;

public class Linebreaker {
    DigitalInput digitalInput;
    boolean inverted;

    public Linebreaker(int port) {
        this.digitalInput = new DigitalInput(port);
        this.inverted = false;
    }

    public Linebreaker(int port, boolean inverted) {
        this.digitalInput = new DigitalInput(port);
        this.inverted = inverted;
    }

    public boolean Broken() {
        return (inverted) ? this.digitalInput.get() : !this.digitalInput.get();
    }
}
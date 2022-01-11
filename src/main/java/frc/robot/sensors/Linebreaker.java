package frc.robot.sensors;

import edu.wpi.first.wpilibj.DigitalInput;

public class Linebreaker {
    private DigitalInput digitalInput;

    public Linebreaker(int port) {
        this.digitalInput = new DigitalInput(port);
    }

    public boolean Broken() {
        return !this.digitalInput.get();
    }
}
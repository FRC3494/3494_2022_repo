package frc.robot.sensors;

import edu.wpi.first.wpilibj.DigitalInput;

public class Linebreaker {
    private DigitalInput m_digitalInput;

    public Linebreaker(int port) {
        this.m_digitalInput = new DigitalInput(port);
    }

    public boolean Broken() {
        return !this.m_digitalInput.get();
    }
}
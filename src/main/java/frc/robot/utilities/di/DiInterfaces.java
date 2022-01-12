package frc.robot.utilities.di;

public class DiInterfaces {
    public interface IInitializable {
        void initialize();
    }
    public interface ITickable {
        void tick();
    }
    public interface IDisposable {
        void dispose();
    }
}


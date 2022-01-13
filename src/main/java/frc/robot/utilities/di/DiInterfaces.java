package frc.robot.utilities.di;

public class DiInterfaces {
    public interface IInitializable {
        /**
        * Defines a point for a Container to initialize of a class
        *
        * @see DiContainer
        */
        void Initialize();
    }
    public interface ITickable {
        /**
        * Defines a point for a Container to tick a class
        *
        * @see DiContainer
        */
        void Tick();
    }
    public interface IDisposable {
        /**
        * Defines a point for a Container to dispose of a class
        *
        * @see DiContainer
        */
        void Dispose();
    }
}


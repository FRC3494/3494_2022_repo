package frc.robot.utilities.di;

public class DiInterfaces {
    public interface IInjected {
        /**
        * Defines a point for a Container to call code as soon as Injection is finished
        *
        * @see DiContainer
        */
        void Inject();
    }
    public interface IInitializable {
        /**
        * Defines a point for a Container to initialize of a class
        *
        * @see DiContainer
        */
        void onInject();
    }
    public interface ITickable {
        /**
        * Defines a point for a Container to tick a class
        *
        * @see DiContainer
        */
        void onTick();
    }
    public interface IDisposable {
        /**
        * Defines a point for a Container to dispose of a class
        *
        * @see DiContainer
        */
        void onDispose();
    }
}


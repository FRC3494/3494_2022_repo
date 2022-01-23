package frc.robot;

public final class RobotConfig {
    public static class Drivetrain {
        public static final double FORWARD_SENSITIVITY = 0.7;
        public static final double TURN_SENSITIVITY = 0.7;
    }

    public static class Shooter {
        public static final double BASE_TARGET_RPM = 2000;

        public static final double TURRET_SPEED = 0.7;
    }

    public static class Intake {
        public static final double INTAKE_SPEED = 0.7;

        public static final double FRONT_DEPLOY_ANGLE = 180;
        public static final double BACK_DEPLOY_ANGLE = -180;
    }

    public static class Magazine {
        public static final double IDLE_SPEED = 0.35;
        public static final double INTAKE_SPEED = 0.7;
        public static final double OUTTAKE_SPEED = -0.7;
    }

    public static class Climber {
        public static final double CLIMB_SPEED = 0.5;

        public static final double HOLDING_POSITION = 0;
        public static final double RELEASE_POSITION = 1;
    }
}

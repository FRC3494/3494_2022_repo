package frc.robot;

public final class RobotConfig {
    public static class Drivetrain {
        public static double FORWARD_SENSITIVITY = 0.7;
        public static double TURN_SENSITIVITY = 0.7;

        public static double PowerCurve(double x) {
            if (x > 1) return 1;
            if (x < -1) return -1;

            return Math.pow(x, 3);
        }
    }

    public static class Shooter {
        public static double BASE_TARGET_RPM = 2000;

        public static double TURRET_SPEED = 0.7;

        public static class PIDF {
            public static float P = 0.0005f;
            public static float I = 0;
            public static float D = 0;
            public static float FF = 0;
        }

        public static double RPMPowerCurve(double x) {
            if (x > 1) return 1;
            if (x < -1) return -1;

            return x;
        }

        public static double TurretPowerCurve(double x) {
            if (x > 1) return 1;
            if (x < -1) return -1;

            return Math.pow(x, 5);
        }
    }

    public static class Intake {
        public static double INTAKE_SPEED = 0.7;

        public static double FRONT_DEPLOY_ANGLE = 180;
        public static double BACK_DEPLOY_ANGLE = -180;

        public static double PowerCurve(double x) {
            if (x > 1) return 1;
            if (x < -1) return -1;

            return x;
        }
    }

    public static class Magazine {
        public static double IDLE_SPEED = 0.35;
        public static double INTAKE_SPEED = 0.7;
        public static double OUTTAKE_SPEED = -0.7;
    }

    public static class Climber {
        public static double CLIMB_SPEED = 0.5;

        public static double PowerCurve(double x) {
            if (x > 1) return 1;
            if (x < -1) return -1;

            return x;
        }
    }
}

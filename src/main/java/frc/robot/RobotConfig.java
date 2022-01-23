package frc.robot;

public final class RobotConfig {
    public static class Drivetrain {
        public static final int LEFT_LEADER_CHANNEL = 0;
        public static final int LEFT_FOLLOWER_CHANNEL = 1;

        public static final int RIGHT_LEADER_CHANNEL = 2;
        public static final int RIGHT_FOLLOWER_CHANNEL = 3;

        public static final double FORWARD_SENSITIVITY = 0.7;
        public static final double TURN_SENSITIVITY = 0.7;
    }

    public static class Shooter {
        public static final int SHOOTER_MOTOR_CHANNEL = 4;

        public static final double BASE_TARGET_RPM = 2000;
    }

    public static class Intake {
        public static final int FRONT_INTAKE_MOTOR_CHANNEL = 5;
        public static final int BACK_INTAKE_MOTOR_CHANNEL = 7;

        public static final double INTAKE_SPEED = 0.7;
    }

    public static class Magazine {
        public static final int LEFT_TREE_UPPER_MOTOR_CHANNEL = 8;
        public static final int LEFT_TREE_LOWER_MOTOR_CHANNEL = 9;
        public static final int LEFT_TREE_LINEBREAK_CHANNEL = 0;

        public static final int RIGHT_TREE_UPPER_MOTOR_CHANNEL = 10;
        public static final int RIGHT_TREE_LOWER_MOTOR_CHANNEL = 11;
        public static final int RIGHT_TREE_LINEBREAK_CHANNEL = 1;

        public static final int TREE_STEM_LEFT_MOTOR_CHANNEL = 12;
        public static final int TREE_STEM_RIGHT_MOTOR_CHANNEL = 13;
        public static final int TREE_STEM_LINEBREAK_CHANNEL = 2;

        public static final double IDLE_SPEED = 0.35;
        public static final double INTAKE_SPEED = 0.7;
        public static final double OUTTAKE_SPEED = -0.7;
    }

    public static class Climber {
        public static final int LEFT_CLIMB_MOTOR_CHANNEL = 14;
        public static final int RIGHT_CLIMB_MOTOR_CHANNEL = 15;

        public static final int RELEASE_SERVO_CHANNEL = 3;

        public static final double CLIMB_SPEED = 0.5;

        public static final double HOLDING_POSITION = 0;
        public static final double RELEASE_POSITION = 1;
    }
}

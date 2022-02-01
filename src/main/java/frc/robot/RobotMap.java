package frc.robot;

public class RobotMap {
    public static class Drivetrain {
        public static final int LEFT_DOM_CHANNEL = 0;
        public static final int LEFT_SUB_CHANNEL = 1;

        public static final int RIGHT_DOM_CHANNEL = 2;
        public static final int RIGHT_SUB_CHANNEL = 3;
    }

    public static class Shooter {
        public static final int SHOOTER_MOTOR_CHANNEL = 4;
        public static final int TURRET_MOTOR_CHANNEL = 6;
        
        public static final int HOOD_MAIN_SOLENOID_CHANNEL = 0;
        public static final int HOOD_SECONDARY_SOLENOID_CHANNEL = 2;
    }

    public static class Intake {
        public static final int FRONT_INTAKE_MOTOR_CHANNEL = 7;
        public static final int BACK_INTAKE_MOTOR_CHANNEL = 8;

        public static final int FRONT_INTAKE_DEPLOY_SOLENOID_CHANNEL = 0;
        public static final int BACK_INTAKE_DEPLOY_SOLENOID_CHANNEL = 2;
    }

    public static class Magazine {
        public static final int LEFT_TREE_MOTOR_CHANNEL = 9;
        public static final int LEFT_TREE_LINEBREAK_CHANNEL = 0;

        public static final int RIGHT_TREE_MOTOR_CHANNEL = 10;
        public static final int RIGHT_TREE_LINEBREAK_CHANNEL = 1;

        public static final int TREE_STEM_MOTOR_CHANNEL = 11;
        public static final int TREE_STEM_LINEBREAK_CHANNEL = 2;
    }

    public static class Climber {
        public static final int CLIMB_MOTOR_CHANNEL = 14;

        public static final int CLIMB_RELEASE_SOLENOID_CHANNEL = 4;
    }

    public static class Pneumatics {
        public static final int BASE_PCM = 0;
        public static final int SHOOTER_PCM = 1;
    }
}

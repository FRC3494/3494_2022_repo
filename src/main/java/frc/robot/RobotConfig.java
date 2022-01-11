package frc.robot;

public final class RobotConfig {
    public static class Drivetrain {
        public static final int leftMaster = 0;
        public static final int leftSlave = 1;

        public static final int rightMaster = 2;
        public static final int rightSlave = 3;

        public static final double forwardSensitivity = 0.7;
        public static final double turnSensitivity = 0.7;
    }

    public static class Shooter {
        public static final int shooterMotor = 4;

        public static final double baseTargetRPM = 2000;
    }

    public static class Intake {
        public static final int frontIntakeMotor = 5;
        public static final int frontIntakeInnerMotor = 6;
        public static final int backIntakeMotor = 7;

        public static final double intakeSpeed = 0.7;
    }

    public static class Magazine {
        public static final int leftTreeUpperMotor = 8;
        public static final int leftTreeLowerMotor = 9;
        public static final int leftTreeLinebreak = 0;

        public static final int rightTreeUpperMotor = 10;
        public static final int rightTreeLowerMotor = 11;
        public static final int rightTreeLinebreak = 1;

        public static final int treeStemLeftMotor = 12;
        public static final int treeStemRightMotor = 13;
        public static final int treeStemLinebreak = 2;

        public static final double idleSpeed = 0.35;
        public static final double intakeSpeed = 0.7;
        public static final double outtakeSpeed = -0.7;
    }

    public static class Climber {
        public static final int leftClimbMotor = 14;
        public static final int rightClimbMotor = 15;

        public static final int climberReleaseServo = 3;

        public static final double climbSpeed = 0.5;

        public static final double holdingPosition = 0;
        public static final double releasePosition = 1;
    }
}

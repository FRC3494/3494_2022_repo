package frc.robot;

public final class RobotConfig {
    public static class Drivetrain {
        public static final int LeftMasterChannel = 0;
        public static final int LeftSlaveChannel = 1;

        public static final int RightMasterChannel = 2;
        public static final int RightSlaveChannel = 3;

        public static final double ForwardSensitivity = 0.7;
        public static final double TurnSensitivity = 0.7;
    }

    public static class Shooter {
        public static final int ShooterMotorChannel = 4;

        public static final double BaseTargetRPM = 2000;
    }

    public static class Intake {
        public static final int FrontIntakeMotorChannel = 5;
        public static final int FrontIntakeInnerMotorChannel = 6;
        public static final int BackIntakeMotorChannel = 7;

        public static final double IntakeSpeed = 0.7;
    }

    public static class Magazine {
        public static final int LeftTreeUpperMotorChannel = 8;
        public static final int LeftTreeLowerMotorChannel = 9;
        public static final int LeftTreeLinebreakChannel = 0;

        public static final int RightTreeUpperMotorChannel = 10;
        public static final int RightTreeLowerMotorChannel = 11;
        public static final int RightTreeLinebreakChannel = 1;

        public static final int TreeStemLeftMotorChannel = 12;
        public static final int TreeStemRightMotorChannel = 13;
        public static final int TreeStemLinebreakChannel = 2;

        public static final double IdleSpeed = 0.35;
        public static final double IntakeSpeed = 0.7;
        public static final double OuttakeSpeed = -0.7;
    }

    public static class Climber {
        public static final int LeftClimbMotorChannel = 14;
        public static final int RightClimbMotorChannel = 15;

        public static final int ClimberReleaseServoChannel = 3;

        public static final double ClimbSpeed = 0.5;

        public static final double HoldingPosition = 0;
        public static final double ReleasePosition = 1;
    }
}

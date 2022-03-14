package frc.robot;

import java.util.ArrayList;
import java.util.List;

import frc.robot.utilities.Pair;

import frc.robot.utilities.AutoConfigurable;

public final class RobotConfig extends AutoConfigurable {
    public static class Drivetrain {
        public static double FORWARD_SENSITIVITY = 0.8; //0.5
        public static double TURN_SENSITIVITY = 0.8; //0.5

        public static double PowerCurve(double x) {
            if (x > 1) return 1;
            if (x < -1) return -1;

            return Math.pow(x, 3);
        }
    }

    public static class Shooter {
        public static List<Pair<String, Double>> RPMS = new ArrayList<>() { {
            add(new Pair<String, Double>("Close Low", 1200.0));
            add(new Pair<String, Double>("Close High", 2500.0));
            add(new Pair<String, Double>("Middle High", 3000.0)); //3250
            add(new Pair<String, Double>("Far High", 3250.0)); //3250
            //add(new Pair<String, Double>("Full Power", 5000.0));
            //new Pair<>("Auto Shot", -1);
        } };

        public static double TURRET_SPEED = 0.4; //0.7

        public static class Aimbot {
            public static double CameraHeightMeters = 38.5; // From floor to camera PLEASE CHANGE
            public static double HubHeightMeters = 50;  //From floor to reflective tape PLEASE CHANGE
            public static double CameraPitchRadians = 0;
            public static String CameraName = "USB_Camera";
            public static double hubCenterConstant = 100;  
            public static int TurretMotorPort = 2;
            public static double maxHoodAngle = 24;
            public static double minHoodAngle = 5;
        }

        public static class PIDF { //Tu= 0.242
            public static float P = 0.00042f;//0.0006f;
            public static float I = 0.000001f;//0.000001;//0.0005f;//0.121f;
            public static float D = 0.00036f;//0.03025f;
            public static float FF = 0.00021f;
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
        public static double INTAKE_SPEED = 0.8;

        public static double FRONT_DEPLOY_ANGLE = 180;
        public static double BACK_DEPLOY_ANGLE = -180;

        public static double PowerCurve(double x) {
            if (x > 1) return 1;
            if (x < -1) return -1;

            return x;
        }
    }

    public static class Magazine {
        public static double IDLE_SPEED = 0.4;
        public static double INTAKE_SPEED = 0.65;
        public static double OUTTAKE_SPEED = -0.75;
        public static double LAUNCH_SPEED = 0.8;

        public static double SECONDS_TO_EJECT_HORIZONTAL = 2;
        public static double SECONDS_TO_SEND_BALL = 2;

        public static double SECONDS_RELOAD_RUN_UP = 0.15;
        public static double SECONDS_RELOAD_RUN_DOWN = 0.2;
    }

    public static class Climber {
        public static double BINARY_CLIMB_SPEED = 1.0;
        public static double ANALOG_CLIMB_SPEED = 0.8;

        public static double PowerCurve(double x) {
            if (x > 1) return 1;
            if (x < -1) return -1;

            return x;
        }
    }
}

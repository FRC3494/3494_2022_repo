package frc.robot.subsystems.Vision;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.RotatedRect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.HttpCamera;
import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
import frc.robot.RobotConfig;
import frc.robot.utilities.di.DiInterfaces.IDisposable;
import frc.robot.utilities.di.DiInterfaces.IInitializable;
import frc.robot.utilities.wpilibdi.DiSubsystem;

public class ComputerVision extends DiSubsystem implements IInitializable, IDisposable  {
    Thread cvThread;

    HttpCamera targetingCamera;
    CvSink targetingCameraSink;

    public static class TargetingCameraProperties {
        public static double Pitch = 0;
        public static double Yaw = 0;
    }

    @Override
    public void onInitialize() {
        this.targetingCamera = new HttpCamera("Targeting Camera", RobotConfig.ComputerVision.TARGETING_CAMERA_URL);
        this.targetingCamera.setConnectionStrategy(ConnectionStrategy.kKeepOpen);

        this.targetingCameraSink = new CvSink("Targeting Camera CV");
        this.targetingCameraSink.setSource(this.targetingCamera);
        
        this.cvThread = new Thread(() -> {
            Mat inMat = new Mat();
            Mat hsvMat = new Mat();
            Mat filteredMat = new Mat();
            //List<MatOfPoint> targetPoints = new ArrayList<>();
            //Mat heirarchicalMat = new Mat();
            MatOfPoint2f targetPoints;
            RotatedRect targetRect;

            while (!Thread.interrupted()) {
                long frameTime = this.targetingCameraSink.grabFrame(inMat);
                if (frameTime == 0) continue;

                Imgproc.cvtColor(inMat, hsvMat, Imgproc.COLOR_BGR2HSV);

                Core.inRange(hsvMat, RobotConfig.ComputerVision.MIN_HSV_RANGE, RobotConfig.ComputerVision.MAX_HSV_RANGE, filteredMat);
                
                //Imgproc.findContours(filteredMat, targetPoints, heirarchicalMat, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
                targetPoints = new MatOfPoint2f(filteredMat);
                targetRect = Imgproc.minAreaRect(targetPoints);

                TargetingCameraProperties.Yaw = Math.toRadians(targetRect.center.x * RobotConfig.ComputerVision.HORIZONTAL_FOV - (RobotConfig.ComputerVision.HORIZONTAL_FOV / 2));
                TargetingCameraProperties.Pitch = Math.toRadians(targetRect.center.y * RobotConfig.ComputerVision.VERTICAL_FOV - (RobotConfig.ComputerVision.VERTICAL_FOV / 2));
            }
        });
        this.cvThread.setDaemon(true);
        this.cvThread.start();
    }

    @Override
    public void onDispose() {
        this.targetingCamera.close();
    }
}

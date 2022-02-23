package frc.robot.subsystems.Vision;
import frc.robot.subsystems.Vision.Point;
public class Line {
    public double m;
    public double b;

    public Line(double x1, double y1, double x2, double y2){
        m = (y1-y2)/(x1-x2);
        b = y1-m*x1;
    }
    public static Point solve(Line l1, Line l2){
        
        return new Point(1.0, 1.0);
    }

}

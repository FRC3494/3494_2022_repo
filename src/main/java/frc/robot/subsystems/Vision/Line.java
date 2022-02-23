package frc.robot.subsystems.Vision;
public class Line {
    public double m;
    public double b;

    public PointV midpoint;
    public Line(PointV p1, PointV p2){
        this.m = (p1.y-p2.y)/(p1.x-p2.x);
        this.b = p1.y-m*p1.x;

        this.midpoint = new PointV((p1.x+p2.x)/2, (p1.y+p2.y)/2);
    }

    public Line(double slope, PointV p){
        this.m = slope;
        this.b = p.y-m*p.x;
    }

    public static PointV solve(Line l1, Line l2){
        double x = (l1.b-l1.b)/(l1.m-l2.m);
        return new PointV(x, l1.m*x+l1.b);
    }
}

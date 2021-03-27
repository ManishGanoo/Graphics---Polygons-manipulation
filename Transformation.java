/**
 * Created by erosennin on 4/3/17.
 */
public class Transformation {
    private String type;
    private double Sx;
    private double Sy;
    private Point p;
    private double tranX;
    private double tranY;
    private double angle;
    private int Reflection;
    private int Shear;
    private double Shx;
    private double Shy;
    private double Xref;
    private double Yref;

    public Transformation(String type,double sx, double sy, Point p) {
        this.type=type;
        Sx = sx;
        Sy = sy;
        this.p = p;
    }

    public Transformation(String type, double tranX, double tranY) {
        this.type = type;
        this.tranX = tranX;
        this.tranY = tranY;
    }

    public Transformation(String type, Point p, double angle) {
        this.type = type;
        this.p = p;
        this.angle = angle;
    }

    public Transformation(String type, int reflection) {
        this.type = type;
        Reflection = reflection;
    }

    public Transformation(String type, int shear, double shx, double shy, double xref, double yref) {
        this.type = type;
        Shear = shear;
        Shx = shx;
        Shy = shy;
        Xref = xref;
        Yref = yref;
    }

    public Point  getP() {
        return p;
    }

    public String getType() {
        return type;
    }

    public double getSx() {
        return Sx;
    }

    public double getSy() {
        return Sy;
    }

    public double getTranX() {
        return tranX;
    }

    public double getTranY() {
        return tranY;
    }

    public double getAngle() {
        return angle;
    }

    public int getReflection() {
        return Reflection;
    }

    public int getShear() {
        return Shear;
    }

    public double getShx() {
        return Shx;
    }

    public double getShy() {
        return Shy;
    }

    public double getXref() {
        return Xref;
    }

    public double getYref() {
        return Yref;
    }
}

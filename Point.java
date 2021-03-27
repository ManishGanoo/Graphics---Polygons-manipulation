/**
 * Created by erosennin on 2/22/17.
 */
public class Point {
    private double x;
    private double y;

    public Point(){}

    public Point(double x,double y){
        this.x=x;
        this.y=y;
    }


    public int getiX() {
        return (int)x;
    }

    public int getiY() {
        return (int)y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setLocation(double x,double y){
        this.x=x;
        this.y=y;
    }
}

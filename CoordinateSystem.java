/**
 * Created by erosennin on 11/4/2016.
 */

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class CoordinateSystem extends JPanel{
    private int height;
    private int width;
    private static int mid_height;
    private static int mid_width;
    private String coordinate;
    private Point center;//center point
    private static int scale_markX;//division length
    private static int scale_markY;
    private static boolean gridon=true;
    private static boolean gravityon=false;
    private static int power=0;

    public static boolean isGravityon() {
        return gravityon;
    }

    public static void setGravityon() {
        gravityon = !gravityon;
    }

    public void setWidth(double width){
        this.width=(int)width;
        mid_width=this.width/2;
    }

    public int getMid_width(){
        return mid_width;
    }

    public int getScale_markX(){
        return scale_markX;
    }

    public void setHeight(double height){
        this.height=(int)height;
        mid_height=this.height/2;
    }

    public int getMid_height(){
        return mid_height;
    }

    public static void setPower(int value){
        power=value;
        if (power>7){
            JOptionPane.showMessageDialog(new JFrame(),"Scale Factor too large,not enough processing power","Scale Error",JOptionPane.ERROR_MESSAGE);
            power=0;
        }
    }

    public static void setPower(){
        power++;
        if (power>7){
            JOptionPane.showMessageDialog(new JFrame(),"Scale Factor too large,not enough processing power","Scale Error",JOptionPane.ERROR_MESSAGE);
            power=0;
        }
    }

    public static double translateX(double x){
        double diff=x-mid_width;
        double pixel=diff/scale_markX;
        if (isGravityon()){
            pixel=Math.round(pixel);
        }
        return pixel*Math.pow(10,power);
    }

    public static double translateY(double y){
        double diff=y-mid_height;
        double pixel=diff/scale_markY;
        if (isGravityon()){
            pixel=Math.round(pixel);
        }
        return (-1*pixel)*Math.pow(10,power);
    }

    public static int BtranslateX(double x){
        double pixel=(x*scale_markX)/Math.pow(10,power);
        double diff=pixel+mid_width;

        return (int)Math.round(diff);
    }

    public static int BtranslateY(double y){
        double pixel=(y*scale_markY*-1)/Math.pow(10,power);
        double diff=pixel+mid_height;
        return (int)Math.round(diff);
    }

    public static void setGridon(){
        gridon=!gridon;
    }

    public static boolean getGridon(){
        return gridon;
    }
    //drawing coordinate system
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);

        center = new Point((double)mid_width, (double)mid_height);
        scale_markX = height/20;
        scale_markY = height/20;

        //drawing axis
        g.drawString("0,0", mid_width, mid_height);
        g.drawLine(mid_width, mid_height, mid_width, mid_height);//center point
        g.drawLine((mid_width - (scale_markX * 10)), mid_height, mid_width + (scale_markX * 10), mid_height);//x-axis
        g.drawLine(mid_width, (mid_height - scale_markX * 10), mid_width, (mid_height + (scale_markX * 10)));//y-axis

        //drawing scale
        int z=scale_markX*10;
        if (getGridon()){
            for (int i = 1; i <= 10; i++) {
                g.setColor(Color.BLUE);

                //+x-axis
                g.drawLine(((int)center.getX() + (i * scale_markX)), (mid_height - z), ((int)center.getX() + (i * scale_markX)), (mid_height + z));//grid


                //-x-axis
                g.drawLine(((int)center.getX() - (i * scale_markX)), (mid_height - z), ((int)center.getX() - (i * scale_markX)), (mid_height + z));//grid



                //-ve y-axis
                g.drawLine((mid_width - z), ((int)center.getY() + (i * scale_markY)), mid_width + (z), ((int)center.getY() + (i * scale_markY)));//grid


                //+y-axis
                g.drawLine((mid_width - z), ((int)center.getY() - (i * scale_markY)), mid_width + (z), ((int)center.getY() - (i * scale_markY)));//grid
            }
        }
        for (int i = 1; i <= 10; i++) {
                g.setColor(Color.WHITE);

                //+x-axis
                if (i == 10) {
                    coordinate = "" + i + "X10^" + power;//coordinate to display
                } else {
                    coordinate = "" + i + "";//coordinate to display
                }
                g.drawString(coordinate, (((int)center.getX() + (i * scale_markX))), (int)center.getY());

                //-x-axis
                coordinate = "-" + i + "";//coordinate to display
                g.drawString(coordinate, (((int)center.getX() - (i * scale_markX)) + 5), (int)center.getY());


                //-ve y-axis
                coordinate = "-" + i + "";//coordinate to display
                g.drawString(coordinate, (int)center.getX() + 5, (((int)center.getY() + (i * scale_markY))));

                //+y-axis
                if (i == 10) {
                    coordinate = "" + i + "X10^" + power;//coordinate to display
                } else {
                    coordinate = "" + i + "";//coordinate to display
                }
                g.drawString(coordinate, (int)center.getX() + 5, (((int)center.getY() - (i * scale_markY))+10));
        }
    }

    public static void AutoScale(ArrayList<Point> vertex){
        ArrayList<Point> tran=new ArrayList<>();
        for (int i=0;i<vertex.size();i++){
            double x=vertex.get(i).getiX();
            double y=vertex.get(i).getiY();
            tran.add(new Point(x,y));
        }
        for (int i=0;i<tran.size();i++){
            double x=tran.get(i).getX();
            double y=tran.get(i).getY();
            double PosRange=10*Math.pow(10,power);
            double NegRange=-1*PosRange;
            while ((x<NegRange)||(x>PosRange)){
                setPower();
                PosRange=10*Math.pow(10,power);
                NegRange=-1*PosRange;
            }
            while ((y<NegRange)||(y>PosRange)){
                setPower();
                PosRange=10*Math.pow(10,power);
                NegRange=-1*PosRange;
            }
        }
    }
}

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.*;

/**
 *
 * @author manish
 */
public class DrawPoly extends Shape{
    private int x1,x2,y1,y2;
    int x1Temp;
    int y1Temp;
    int x2Temp;
    int y2Temp;

    public void paintComponent(Graphics g,ArrayList<Point> polygon){
        super.paintComponent(g);
        int dx,dy,change;
        int stepx, stepy;
        //drawing the polygon and filling it

        Point p,p0,p1;
        g.setColor(Color.RED);
        for(int i=0;i<polygon.size();i++){
            p = polygon.get(i);
            p0 = polygon.get(0);


            if(i==polygon.size()-1){
                x1=CoordinateSystem.BtranslateX(p0.getX());
                x2=CoordinateSystem.BtranslateX(p.getX());
                y1=CoordinateSystem.BtranslateY(p0.getY());
                y2=CoordinateSystem.BtranslateY(p.getY());

                dx = Math.abs(x2 - x1);
                dy = Math.abs(y2 - y1);
                change = dx - dy;

                x1Temp=x2;y1Temp=y2;
                x2Temp=x1;y2Temp=y1;

            }
            else{
                p1 = polygon.get(i+1);

                x1=CoordinateSystem.BtranslateX(p.getX());
                x2=CoordinateSystem.BtranslateX(p1.getX());
                y1=CoordinateSystem.BtranslateY(p.getY());
                y2=CoordinateSystem.BtranslateY(p1.getY());


                dx = Math.abs(x2 - x1);
                dy = Math.abs(y2 - y1);
                change = dx - dy;

                x1Temp=x1;y1Temp=y1;
                x2Temp=x2;y2Temp=y2;
            }

            if (x1Temp <x2Temp){
                stepx = 1;
            }
            else {
                stepx = -1;
            }
            if (y1Temp < y2Temp) {
                stepy = 1;
            }
            else{
                stepy = -1;
            }

            while ((x1Temp != x2Temp) || (y1Temp != y2Temp)) {
                int param = 2 * change;
                if (param > -dy) {
                    change = change - dy;
                    x1Temp = x1Temp + stepx;
                }
                if (param < dx) {
                    change = change + dx;
                    y1Temp = y1Temp + stepy;
                }
                g.drawLine(x1Temp,y1Temp, x1Temp, y1Temp);
            }
        }
    }
}
import java.awt.*;
import java.util.ArrayList;

public class Regular_Polygon extends Shape{
    private int numSides;
    private double x,y,radius;
    private static ArrayList<Point> vertex;

    public void setNum_sides(int num){
        this.numSides=num;
    }

    public void paintComponent(Graphics g, Color c){
        super.paintComponent(g);
        g.setColor(c);
        Graphics2D ga=(Graphics2D)g;
        ga.setStroke(new BasicStroke(2));

        int x1 = p.getiX();
        int x2 = p2.getiX();
        int y1 = p.getiY();
        int y2 = p2.getiY();
        int dx,dy,change;
        int stepx, stepy;



        int x1Temp;
        int y1Temp;
        int x2Temp;
        int y2Temp;


        //drawing polygon by Bresenham algorithm
        //  Graphics2D ga=(Graphics2D)g;
        //number of sides of polygon
        int num_of_sides=numSides;
        int []X=new int[num_of_sides];
        int []Y=new int[num_of_sides];

        //calculating the radius
        x=Math.pow((x2-x1),2);
        y=Math.pow((y2-y1),2);
        radius=Math.sqrt(x+y);

        //getting the coordinate of the polygon
        double theta;
        vertex=new ArrayList<>();
        for(int i=0;i<num_of_sides;i++){
            double xv,yv;
            theta=2.0*Math.PI*i/num_of_sides;
            xv=(x1+radius*Math.cos(theta));
            yv=(y1+radius*Math.sin(theta));
            vertex.add(new Point(CoordinateSystem.translateX(xv),CoordinateSystem.translateY(yv)));
            X[i]=(int)xv;
            Y[i]=(int)yv;
        }
        for (Point x:vertex){
            String coordinates=String.format("%.2f , %.2f",(float)x.getX(),(float)x.getY());
            g.drawString(coordinates,CoordinateSystem.BtranslateX(x.getX()),CoordinateSystem.BtranslateY(x.getY()));
        }

        //drawing the polygon and filling it
        for(int i=0;i<num_of_sides;i++){
            if(i==num_of_sides-1){
                dx = Math.abs(X[i] - X[0]);
                dy = Math.abs(Y[i] - Y[0]);
                change = dx - dy;

                x1Temp=X[i];y1Temp=Y[i];
                x2Temp=X[0];y2Temp=Y[0];

            }
            else{


                dx = Math.abs(X[i+1] - X[i]);
                dy = Math.abs(Y[i+1] - Y[i]);
                change = dx - dy;

                x1Temp=X[i];y1Temp=Y[i];
                x2Temp=X[i+1];y2Temp=Y[i+1];
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

                int p = 2 * change;

                if (p > -dy) {
                    change = change - dy;
                    x1Temp = x1Temp + stepx;
                }
                if (p < dx) {
                    change = change + dx;
                    y1Temp = y1Temp + stepy;
                }


                ga.drawLine(x1Temp,y1Temp, x1Temp, y1Temp);

            }
        }
        CoordinateSystem.AutoScale(vertex);
        Index.refresh();
    }

    public static ArrayList<Point> getVertex(){
        return vertex;
    }
}

import java.awt.*;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Irregular_Polygon extends JPanel{
    //inialising the array
    private  int max_sides=100;
    private  double [] X = new double[max_sides];
    private  double [] Y = new double[max_sides];
    private static ArrayList<Point> IrregVertex=new ArrayList<>();
    private static int cnt;
    int dx,dy,change;
    int stepx, stepy;



    int x1Temp;
    int y1Temp;
    int x2Temp;
    int y2Temp;


    public Irregular_Polygon(){
        cnt = 0;
    }

    public  void setXY(Point p){
        //getting the values of x and y
        //increment the count to know the number of sides
        if(p.getX() !=0){
            X[cnt] = p.getX();
            Y[cnt] = p.getY();
            cnt++;
        }
    }

    public void paint(Graphics g){
        Graphics2D ga = (Graphics2D)g;
        ga.setColor(Color.RED);

        IrregVertex.clear();
        for(int i=0;i<=cnt;i++){
            if(X[i]!=0){
                IrregVertex.add(new Point(X[i],Y[i]));
            }
        }
        for (Point x:IrregVertex){
            String coordinates=String.format("%.2f , %.2f",(float)x.getX(),(float)x.getY());
            g.drawString(coordinates,CoordinateSystem.BtranslateX(x.getX()),CoordinateSystem.BtranslateY(x.getY()));
        }


        for(int i=0;i<cnt;i++){
            if(i==cnt-1){
                dx = Math.abs(CoordinateSystem.BtranslateX(X[i]) - CoordinateSystem.BtranslateX(X[0]));
                dy = Math.abs(CoordinateSystem.BtranslateY(Y[i]) - CoordinateSystem.BtranslateY(Y[0]));
                change = dx - dy;

                x1Temp=CoordinateSystem.BtranslateX(X[i]);
                y1Temp=CoordinateSystem.BtranslateY(Y[i]);
                x2Temp=CoordinateSystem.BtranslateX(X[0]);
                y2Temp=CoordinateSystem.BtranslateY(Y[0]);

            }
            else{


                dx = Math.abs(CoordinateSystem.BtranslateX(X[i+1]) - CoordinateSystem.BtranslateX(X[i]));
                dy = Math.abs(CoordinateSystem.BtranslateY(Y[i+1]) - CoordinateSystem.BtranslateY(Y[i]));
                change = dx - dy;

                x1Temp=CoordinateSystem.BtranslateX(X[i]);
                y1Temp=CoordinateSystem.BtranslateY(Y[i]);
                x2Temp=CoordinateSystem.BtranslateX(X[i+1]);
                y2Temp=CoordinateSystem.BtranslateY(Y[i+1]);
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

                ga.drawLine(x1Temp,y1Temp, x1Temp,y1Temp);

            }

        }

        // CoordinateSystem.AutoScale(IrregVertex);
        // Index.refresh();

        //close the current path and move back to the start
        //polygon.closePath();
        //ga.setPaint(Color.black);
        //ga.draw(polygon);
        //in order to delete the polygon
        //cnt = 0;
    }
    public static ArrayList<Point> getVertex(){
        return IrregVertex;
    }

    public  void initializeCnt(){
        cnt=0;
        IrregVertex=new ArrayList<>();
        X = new double[max_sides];
        Y = new double[max_sides];
    }

}
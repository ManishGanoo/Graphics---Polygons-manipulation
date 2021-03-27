/**
 * Created by erosennin on 11/4/2016.
 */

import javax.swing.*;
import java.awt.*;

public class Shape extends JPanel {
    protected Point p;
    protected Point p2;
    protected int numSides;

    public Shape(){
        p=new Point();
        p2=new Point();
    }

    public void setXY1(int x,int y){
        p.setLocation(x,y);
    }

    public void setXY2(int x,int y){
        p2.setLocation(x,y);
    }

    public void setNum_sides(int num){
        numSides=num;
    }

    public void paintComponent(Graphics g,Color c){
        super.paintComponent(g);
        g.setColor(Color.WHITE);
    }
}
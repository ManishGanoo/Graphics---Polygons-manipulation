import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Scanline extends JPanel {

    static boolean ScanLineFill = false;

    private static  ArrayList<Point> polygon=new ArrayList<>(); //hold polygon vertices
    private static boolean isPolyClosed = false;   // finished drawing polygon?

    public Scanline(){
    }
    private class HandlerClass implements MouseListener,MouseMotionListener{
        public void mouseClicked(MouseEvent e) {
            if(SwingUtilities.isLeftMouseButton(e)){
                polygon.add(new Point(e.getX(), e.getY()));
            }
        }
        public void mouseReleased(MouseEvent e) {
            if(SwingUtilities.isRightMouseButton(e)){
                repaint();

                isPolyClosed = true;
                polygon.add(polygon.get(0));
                repaint();

            }
        }
        public void mousePressed(MouseEvent e) {}
        public void mouseEntered(MouseEvent e){}
        public void mouseExited(MouseEvent e){}
        public void mouseDragged(MouseEvent e){}
        public void mouseMoved(MouseEvent e){}
    }


    public  void clearP(){
        polygon.clear();
        isPolyClosed = false;
        this.repaint();
        ScanLineFill=false;
    }

    public void paint(Graphics g,ArrayList<Point> vertex,Color color) {
        super.paint(g);
        polygon.clear();
        for (Point x:vertex){
            polygon.add(new Point(CoordinateSystem.BtranslateX(x.getX()),CoordinateSystem.BtranslateY(x.getY())));
        }

        try{
            polygon.add(polygon.get(0));
        }
        catch(Exception e){}

        Graphics2D ga=(Graphics2D)g;
        ga.setStroke(new BasicStroke(2));
        ga.setPaint(color);
        ScanlineFill(ga);

    }


    public static Edge[] createEdges()    {
        Edge[] sortedEdges=null;
        try{
            sortedEdges = new Edge[polygon.size()-1];
            for (int i = 0; i < polygon.size() - 1; i++)
            {
                //if (polygon.elementAt(i).y == polygon.elementAt(i+1).y) continue;
                if (polygon.get(i).getiY() < polygon.get(i+1).getiY())
                    sortedEdges[i] = new Edge(polygon.get(i), polygon.get(i+1));
                else
                    sortedEdges[i] = new Edge(polygon.get(i+1), polygon.get(i));
            }
        }
        catch(Exception e){}
        return sortedEdges;
    }


    public void ScanlineFill(Graphics2D ga){
        // create edges array from polygon vertice vector
        // make sure that first vertice of an edge is the smaller one
        Edge[] sortedEdges = Scanline.createEdges();

        if (sortedEdges==null){
            return;
        }

        // sort all edges by y coordinate, smallest one first, lousy bubblesort
        Edge tmp;

        for (int i = 0; i < sortedEdges.length - 1; i++)
            for (int j = 0; j < sortedEdges.length - 1; j++)
            {
                if (sortedEdges[j].p1.getiY() > sortedEdges[j+1].p1.getiY())
                {
                    // swap both edges
                    tmp = sortedEdges[j];
                    sortedEdges[j] = sortedEdges[j+1];
                    sortedEdges[j+1] = tmp;
                }
            }

        // find biggest y-coord of all vertices
        int scanlineEnd = 0;
        for (int i = 0; i < sortedEdges.length; i++)
        {
            if (scanlineEnd < sortedEdges[i].p2.getiY())
                scanlineEnd = sortedEdges[i].p2.getiY();
        }


        // scanline starts at smallest y coordinate
        int scanline = sortedEdges[0].p1.getiY();

        // this list holds all cutpoints from current scanline with the polygon
        ArrayList<Integer> list = new ArrayList<Integer>();

        // move scanline step by step down to biggest one
        for (scanline = sortedEdges[0].p1.getiY(); scanline <= scanlineEnd; scanline++)
        {
            //System.out.println("ScanLine: " + scanline); // DEBUG

            list.clear();

            // loop all edges to see which are cut by the scanline
            for (int i = 0; i < sortedEdges.length; i++)
            {

                // here the scanline intersects the smaller vertice
                if (scanline == sortedEdges[i].p1.getiY())
                {
                    if (scanline == sortedEdges[i].p2.getiY())
                    {
                        // the current edge is horizontal, so we add both vertices
                        sortedEdges[i].deactivate();
                        list.add((int)sortedEdges[i].curX);
                    }
                    else
                    {
                        sortedEdges[i].activate();
                        // we don't insert it in the list cause this vertice is also
                        // the (bigger) vertice of another edge and already handled
                    }
                }

                // here the scanline intersects the bigger vertice
                if (scanline == sortedEdges[i].p2.getiY())
                {
                    sortedEdges[i].deactivate();
                    list.add((int)sortedEdges[i].curX);
                }

                // here the scanline intersects the edge, so calc intersection point
                if (scanline > sortedEdges[i].p1.getiY() && scanline < sortedEdges[i].p2.getiY())
                {
                    sortedEdges[i].update();
                    list.add((int)sortedEdges[i].curX);
                }

            }

            // now we have to sort our list with our x-coordinates, ascendend
            int swaptmp;
            for (int i = 0; i < list.size(); i++)
                for (int j = 0; j < list.size() - 1; j++)
                {
                    if (list.get(j) > list.get(j+1))
                    {
                        swaptmp = list.get(j);
                        list.set(j, list.get(j+1));
                        list.set(j+1, swaptmp);
                    }

                }

            //ga.setColor(Color.MAGENTA);

            //System.out.println(list);

            // so draw all line segments on current scanline
            for (int i = 0; i < list.size(); i+=2){
                ga.drawLine(list.get(i), scanline, list.get(i+1), scanline);
            }

        }
        //Selection.setScanFill(false);

    }

    public static boolean isPolygonClosed() {
        return isPolyClosed;
    }
}
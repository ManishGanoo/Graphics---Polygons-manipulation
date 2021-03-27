/**
 * Created by erosennin on 9/29/2016.
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

public class PointInput extends JPanel {
    private static int fillX = 0;
    private static int fillY = 0;
    private static ArrayList<Integer> Xfill = new ArrayList<Integer>();
    private static ArrayList<Integer> Yfill = new ArrayList<Integer>();
    private static Color fillColor = Color.green;
    private static Color startColor = Color.red;
    private Point xy_moved = new Point();
    private Irregular_Polygon irregular_polygon = new Irregular_Polygon();
    private CoordinateSystem coordinateSystem = new CoordinateSystem();
    private String xyshown = "";
    private int px = 0;
    private int py = 0;
    private Shape regular_polygon = new Regular_Polygon();
    private LinkedList<Point> stpoint = new LinkedList<>();
    private LinkedList<Point> edpoint = new LinkedList<>();
    private boolean draw = true;
    private boolean test = false;
    int x;
    int y;
    private static Queue<Transformation> transformationQueue = new PriorityQueue<>();
    private static ArrayList<Point> vertex = new ArrayList<>();
    private static ArrayList<Point> tVertex = new ArrayList<>();
    private static boolean FloodFill = false;
    private static boolean BoundaryFill = false;
    private static boolean mandelbrot = false;
    private static boolean KochLine = false;
    private static int kochValue=0;
    int x1,y1,x5,y5;
    int[] kx1;
    int[] ky1;
    int[] kx2;
    int[] ky2;
    private BufferedImage I;
    private final int MAX_ITER = 100;     //color
    private final double ZOOM = 300;
    private double zx, zy, cX, cY, tmp;
    private DrawPoly drawpoly=new DrawPoly();
    private static ArrayList<Point> mandel=new ArrayList();
    private static ArrayList<Color> mandelco=new ArrayList();
    static ArrayList notmandelbrotColor = new ArrayList();
    static ArrayList notYmandelbrot = new ArrayList();
    static ArrayList notXmandelbrot = new ArrayList();

    public PointInput() {
        HandlerClass handler = new HandlerClass();

        this.addMouseListener(handler);
        this.addMouseMotionListener(handler);
    }

    public void Mandelbrotf(int []xt,int []yt,int size){
        System.out.println(size);
        Polygon p=new Polygon(xt,yt,size);

//        I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);



        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {

                //I.setRGB(x, y,255);

                //              I.setRGB(x, y,Color.black.getRGB());

                if (p.contains(x, y)){
                    notXmandelbrot.add(x);
                    notYmandelbrot.add(y);
                    zx = zy = 0;
                    cX = (x - 400) / ZOOM;
                    cY = (y - 300) / ZOOM;
                    int iter = MAX_ITER;
                    while (zx * zx + zy * zy < 4 && iter > 0) {
                        tmp = zx * zx - zy * zy + cX;
                        zy = 2.0 * zx * zy + cY;
                        zx = tmp;
                        iter--;
                    }
                    if(iter>0){

                        Color pxcolor = Color.blue;
                        mandelco.add(pxcolor);
                        mandel.add(new Point(x,y));
                    }

                    //                I.setRGB(x, y, iter | (iter << 8));

                }
            }
        }


    }

    private void drawSnow (Graphics g, int lev, int x1, int y1, int x5, int y5){
        int deltaX, deltaY, x2, y2, x3, y3, x4, y4;

        if (lev == 0){

            g.drawLine(x1, y1, x5, y5);
        }
        else{
            deltaX = x5 - x1;
            deltaY = y5 - y1;

            x2 = (x1 + deltaX / 3);
            y2 = y1 + deltaY / 3;

            x3 = (int) ((0.5 * (x1+x5)) - (Math.sqrt(3) * (y1-y5)/6));
            y3 = (int) (0.5 * (y1+y5) - Math.sqrt(3) * (x5-x1)/6);

            x4 = x1 + (2 * deltaX) /3;
            y4 = y1 + 2 * deltaY /3;

            drawSnow (g,lev-1, x1, y1, x2, y2);
            drawSnow (g,lev-1, x2, y2, x3, y3);
            drawSnow (g,lev-1, x3, y3, x4, y4);
            drawSnow (g,lev-1, x4, y4, x5, y5);
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D ga = (Graphics2D) g;
        //if(mandelbrot){
        //    g.drawImage(I,0,0,null);
        //}
        ga.setStroke(new BasicStroke(2));
        this.setBackground(Color.black);

        Dimension size = getSize();

        Runnable graph = new Runnable() {
            @Override
            public void run() {
                coordinateSystem.setWidth(size.getWidth());
                coordinateSystem.setHeight(size.getHeight());
                coordinateSystem.paintComponent(g);//drawing coordinate system
            }
        };


        Runnable regular = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < edpoint.size(); i++) {
                    g.setColor(Color.RED);
                    regular_polygon.setXY1(coordinateSystem.BtranslateX(stpoint.get(i).getX()), coordinateSystem.BtranslateY(stpoint.get(i).getY()));
                    regular_polygon.setXY2(coordinateSystem.BtranslateX(edpoint.get(i).getX()), coordinateSystem.BtranslateY(edpoint.get(i).getY()));
                    regular_polygon.setNum_sides(Selection.getNum_Sides());
                    regular_polygon.paintComponent(g, Color.RED);
                }
            }
        };


        Runnable irregular = new Runnable() {
            @Override
            public void run() {
                irregular_polygon.paint(g);
            }
        };

        graph.run();

        if (Selection.getBrush() == "regular") {
            if(KochLine){
                ga.setPaint(Color.RED);
                for(int i=0;i<kx1.length;i++){
                    drawSnow(ga,kochValue,kx1[i],ky1[i],kx2[i],ky2[i]);
                }
            }
            else {
                regular.run();
            }
        }
        if (Selection.getBrush() == "irregular") {
            if (KochLine) {
                ga.setPaint(Color.RED);
                for (int i = 0; i < kx1.length; i++) {
                    drawSnow(ga, kochValue, kx1[i], ky1[i], kx2[i], ky2[i]);
                }
            } else {
                irregular.run();
            }
        }

        Runnable transformation=new Runnable() {
            @Override
            public void run() {
                if(Selection.isTransformed()){
                    Polygon polygon=new Polygon();
                    g.setColor(Color.WHITE);

                    for(int j=0;j<vertex.size();j++){
                        double Xt,Yt;
                        Xt=CoordinateSystem.BtranslateX(vertex.get(j).getX());
                        Yt=CoordinateSystem.BtranslateY(vertex.get(j).getY());
                        polygon.addPoint((int)Xt,(int)Yt);
                    }
                    g.drawPolygon(polygon);
                    //drawpoly.paintComponent(g,vertex);

                    for (Point x:vertex){
                        String coordinates=String.format("%.2f , %.2f",(float)x.getX(),(float)x.getY());
                        g.drawString(coordinates,CoordinateSystem.BtranslateX(x.getX()),CoordinateSystem.BtranslateY(x.getY()));
                    }

                    CoordinateSystem.AutoScale(vertex);
                    Index.refresh();
                }
            }
        };

        transformation.run();

        if(mandelbrot){
            for(int i=0;i<notXmandelbrot.size();i++){
                g.setColor(Color.black);
                g.fillRect((int)notXmandelbrot.get(i), (int) notYmandelbrot.get(i), 1, 1);
            }

            for(int i=0;i<mandel.size();i++){
                g.setColor(mandelco.get(i));
                g.fillRect((int)mandel.get(i).getX(),(int) mandel.get(i).getY(), 1, 1);
            }

        }

        if ((BoundaryFill) || (FloodFill)) {
            for (int i = 0; i < Xfill.size(); i++) {
                fillColor = Selection.getColor();
                ga.setPaint(fillColor);
                ga.fillRect(Xfill.get(i), Yfill.get(i), 1, 1);
            }
        }

        if (Selection.isScanFill()){
            Scanline scanline=new Scanline();
            Scanline scanline1=new Scanline();
            ArrayList<Point> fillVertex=new ArrayList<>();
            ArrayList<Point> fillVertex2=new ArrayList<>();

            if (Selection.getBrush()=="regular"){
                fillVertex=Regular_Polygon.getVertex();
            }
            else if (Selection.getBrush()=="irregular"){
                fillVertex=irregular_polygon.getVertex();
            }
            if (Selection.isTransformed()){
                fillVertex2=vertex;
                scanline1.paint(g,fillVertex2,Selection.getColor2());
            }
            scanline.paint(g,fillVertex,Selection.getColor());
        }
    }

    private class HandlerClass implements MouseListener, MouseMotionListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (Selection.isDrawing()) {
                if (Selection.getBrush() == "irregular") {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        setIrregular_polygon(e.getX(), e.getY());
                    }
                }
            }

            if (e.getClickCount() == 3) {
                ClearAll();
            }
            if (e.getClickCount() == 2) {
                if (Selection.ismandelbrot()) {
                    mandelbrot = true;

                    int PolySize = 0;
                    if (Selection.getBrush() == "irregular") {

                        PolySize = Irregular_Polygon.getVertex().size();
                        kx1 = new int[PolySize];
                        ky1 = new int[PolySize];

                        for (int i = 0; i < PolySize; i++) {
                            kx1[i] = coordinateSystem.BtranslateX(Irregular_Polygon.getVertex().get(i).getX());
                            ky1[i] = coordinateSystem.BtranslateY(Irregular_Polygon.getVertex().get(i).getY());

                        }
                    }
                    if (Selection.getBrush() == "regular") {
                        PolySize = Regular_Polygon.getVertex().size();
                        kx1 = new int[PolySize];
                        ky1 = new int[PolySize];

                        for (int i = 0; i < PolySize; i++) {

                            kx1[i] = coordinateSystem.BtranslateX(Regular_Polygon.getVertex().get(i).getX());
                            ky1[i] = coordinateSystem.BtranslateY(Regular_Polygon.getVertex().get(i).getY());
                            System.out.println(kx1[i]);
                        }
                    }
                    Mandelbrotf(kx1, ky1, PolySize);
                    repaint();
                }
                Polygon Pt = null ;
                int[] xcheck = null;    int[] xtcheck = null;
                int[] ycheck = null;	int[] ytcheck = null;
                int PolySize = 0;
                if (Selection.getBrush() == "regular") {
                    PolySize = Regular_Polygon.getVertex().size();
                    xcheck = new int[PolySize];
                    ycheck = new int[PolySize];
                    for (int i = 0; i < PolySize; i++) {
                        xcheck[i] = Regular_Polygon.getVertex().get(i).getiX();
                        ycheck[i] = Regular_Polygon.getVertex().get(i).getiY();
                    }
                }
                if (Selection.getBrush() == "irregular") {
                    PolySize = Irregular_Polygon.getVertex().size();
                    xcheck = new int[PolySize];
                    ycheck = new int[PolySize];
                    for (int i = 0; i < PolySize; i++) {
                        xcheck[i] = Irregular_Polygon.getVertex().get(i).getiX();
                        ycheck[i] = Irregular_Polygon.getVertex().get(i).getiY();
                    }
                }
                if(Selection.isTransformed()){
                    PolySize=vertex.size();
                    xtcheck = new int[PolySize];
                    ytcheck = new int[PolySize];
                    System.out.println(PolySize);
                    for (int i = 0; i < PolySize; i++) {
                        //System.out.println(vertex.get(i).getX());
                        xtcheck[i] = vertex.get(i).getiX();
                        ytcheck[i] = vertex.get(i).getiY();
                    }

                    Pt = new Polygon(xtcheck, ytcheck, PolySize);

                }
                Polygon P = new Polygon(xcheck, ycheck, PolySize);
                if (Selection.isBoundFill()) {
                    BoundaryFill = true;
                    fillX = (int) coordinateSystem.translateX(e.getX());
                    fillY = (int) coordinateSystem.translateY(e.getY());

                    if(Selection.isTransformed()){
                        if (P.contains(fillX, fillY)||Pt.contains(fillX,fillY)) {
                            fillX = e.getX();
                            fillY = e.getY();


                            BoundaryFill(fillX, fillY, startColor, fillColor);

                            // if(Pt.contains(fillX, fillY)){
                            // 	startColor=Color.white;
                            //     BoundaryFill(fillX, fillY, startColor, fillColor);
                            //     }

                            // boundaryFillReg.checkPoly(250, 150);
                        }
                    }
                    else {
                        if (P.contains(fillX, fillY)) {
                            fillX = e.getX();
                            fillY = e.getY();

                            BoundaryFill(fillX, fillY, startColor, fillColor);
                        }
                    }
                }else if (Selection.isFloodFill()) {
                        FloodFill = true;
                        fillX = e.getX();
                        fillY = e.getY();
                        FloodFill(fillX, fillY, fillColor);

                }

            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (Selection.isDrawing()) {
                stpoint = new LinkedList<>();
                edpoint = new LinkedList<>();
                Point p = new Point();
                p.setLocation(e.getX(), e.getY());
                p = checkBonds(p);
                p.setLocation(coordinateSystem.translateX(p.getX()), coordinateSystem.translateY(p.getY()));
                stpoint.add(p);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (Selection.isDrawing()) {
                Point p = new Point();
                p.setLocation(e.getX(), e.getY());
                p = checkBonds(p);
                if ((Selection.getBrush() == "regular") && (draw)) {
                    p.setLocation(coordinateSystem.translateX(p.getX()), coordinateSystem.translateY(p.getY()));
                    edpoint.add(p);
                    repaint();
                }

                if ((SwingUtilities.isRightMouseButton(e))) {
                    if (Selection.iskochline()) {
                        KochLine = true;

                        int PolySize;
                        kochValue = Selection.getkochvalue();
                        if (Selection.getBrush() == "irregular") {

                            PolySize = Irregular_Polygon.getVertex().size();
                            kx1 = new int[PolySize];
                            ky1 = new int[PolySize];
                            kx2 = new int[PolySize];
                            ky2 = new int[PolySize];
                            for (int i = 0; i < PolySize; i++) {
                                if (i == PolySize - 1) {

                                    kx1[i] = coordinateSystem.BtranslateX(Irregular_Polygon.getVertex().get(0).getX());
                                    ky1[i] = coordinateSystem.BtranslateY(Irregular_Polygon.getVertex().get(0).getY());
                                    kx2[i] = coordinateSystem.BtranslateX(Irregular_Polygon.getVertex().get(i).getX());
                                    ky2[i] = coordinateSystem.BtranslateY(Irregular_Polygon.getVertex().get(i).getY());

                                } else {
                                    kx1[i] = coordinateSystem.BtranslateX(Irregular_Polygon.getVertex().get(i).getX());
                                    ky1[i] = coordinateSystem.BtranslateY(Irregular_Polygon.getVertex().get(i).getY());
                                    kx2[i] = coordinateSystem.BtranslateX(Irregular_Polygon.getVertex().get(i + 1).getX());
                                    ky2[i] = coordinateSystem.BtranslateY(Irregular_Polygon.getVertex().get(i + 1).getY());
                                }
                            }
                        }
                        if (Selection.getBrush() == "regular") {
                            PolySize = Regular_Polygon.getVertex().size();
                            kx1 = new int[PolySize];
                            ky1 = new int[PolySize];
                            kx2 = new int[PolySize];
                            ky2 = new int[PolySize];
                            for (int i = 0; i < PolySize; i++) {
                                if (i == PolySize - 1) {

                                    kx1[i] = coordinateSystem.BtranslateX(Regular_Polygon.getVertex().get(0).getX());
                                    ky1[i] = coordinateSystem.BtranslateY(Regular_Polygon.getVertex().get(0).getY());
                                    kx2[i] = coordinateSystem.BtranslateX(Regular_Polygon.getVertex().get(i).getX());
                                    ky2[i] = coordinateSystem.BtranslateY(Regular_Polygon.getVertex().get(i).getY());

                                } else {
                                    kx1[i] = coordinateSystem.BtranslateX(Regular_Polygon.getVertex().get(i).getX());
                                    ky1[i] = coordinateSystem.BtranslateY(Regular_Polygon.getVertex().get(i).getY());
                                    kx2[i] = coordinateSystem.BtranslateX(Regular_Polygon.getVertex().get(i + 1).getX());
                                    ky2[i] = coordinateSystem.BtranslateY(Regular_Polygon.getVertex().get(i + 1).getY());
                                }
                            }
                        }

                    }

                    repaint();
                }
            }
        }


        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (Selection.isDrawing()){
                xy_moved.setLocation(e.getX(),e.getY());
                Mapping(xy_moved.getX(),xy_moved.getY());
            }
        }
    }

    public void ClearAll(){
        stpoint = new LinkedList<>();
        edpoint = new LinkedList<>();
        vertex = new ArrayList<>();
        irregular_polygon.initializeCnt();
        Selection.setFirst(false);
        Selection.setBoundFill(false);
        Selection.setFloodFill(false);
        Selection.setTransformed(false);
        Selection.setScanFill(false);
        Selection.setDrawing(true);
        Selection.setkochline(false);
        Selection.setmandelbrot(false);
        //CoordinateSystem.setPower(0);
        clearfill();
        clearFractal();
        repaint();
    }

    public void BoundaryFill(int X, int Y, Color bColor, Color fColor) {
        try{
            int w = getWidth();
            int h = getHeight();

            BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gd = bi.createGraphics();
            this.paint(gd);

            Stack<Point> points = new Stack<>();
            points.add(new Point(X, Y));
            while (!points.isEmpty()) {
                Point currentPoint = points.pop();
                int x = currentPoint.getiX();
                int y = currentPoint.getiY();

                int current = bi.getRGB(x, y);

                if ((current != bColor.getRGB()) && (current != fColor.getRGB())) {
                    StackManipulation(bi, x, y, fColor, points);
                }
            }
        }
        catch(Exception e){}


    }

    public void FloodFill(int X, int Y, Color fColor) {
        try{
            int w = this.getWidth();
            int h = this.getHeight();
            BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = bi.createGraphics();
            this.paint(g2);
            int initialFill = bi.getRGB(X, Y);

            Stack<Point> points = new Stack<>();
            points.add(new Point(X, Y));

            while (!points.isEmpty()) {
                Point currentPoint = points.pop();
                int x = currentPoint.getiX();
                int y = currentPoint.getiY();

                int current = bi.getRGB(x, y);
                if (current == Color.BLACK.getRGB() || current == initialFill || current == Color.BLUE.getRGB() || current == Color.WHITE.getRGB()) {

                    StackManipulation(bi, x, y, fColor, points);
                }
            }
        }
        catch(Exception e){}
    }

    public void StackManipulation(BufferedImage bi, int x, int y, Color fColor, Stack<Point> points) {
        bi.setRGB(x, y, fColor.getRGB());

        Xfill.add(x);
        Yfill.add(y);
        repaint();
        points.push(new Point(x + 1, y));
        points.push(new Point(x - 1, y));
        points.push(new Point(x, y + 1));
        points.push(new Point(x, y - 1));
        points.push(new Point(x + 1, y + 1));
        points.push(new Point(x - 1, y - 1));
        points.push(new Point(x + 1, y - 1));
        points.push(new Point(x - 1, y + 1));
    }

    public static void clearfill() {
        Xfill.clear();
        Yfill.clear();
    }

    public void Mapping(double x, double y) {
        float trans_X, trans_Y;

        trans_X = (float) coordinateSystem.translateX(x);
        trans_Y = (float) coordinateSystem.translateY(y);
        if (((trans_X >= -10) && (trans_X <= 10)) && ((trans_Y >= -10) && (trans_Y <= 10))) {
            xyshown = String.format("%.1f,  %.1f", trans_X, trans_Y);
            Menu.changeText(xyshown);

        }
    }

    public Point checkBonds(Point p) {
        double left = 0 + (coordinateSystem.getMid_width() - (coordinateSystem.getScale_markX() * 10));
        double right = coordinateSystem.getMid_width() + (coordinateSystem.getScale_markX() * 10);
        double top = 0 + (coordinateSystem.getMid_height() - (coordinateSystem.getScale_markX() * 10));
        double bottom = coordinateSystem.getMid_height() + (coordinateSystem.getScale_markX() * 10);
        boolean[] validation = new boolean[4];
        if (p.getX() < left) {
            p.setX(left);
            validation[0] = true;
        }
        if (p.getX() > right) {
            p.setX(right);
            validation[1] = true;
        }
        if (p.getY() < top) {
            p.setY(top);
            validation[2] = true;
        }
        if (p.getY() > bottom) {
            p.setY(bottom);
            validation[3] = true;
        }
        for (int i = 0; i < 4; i++) {
            if (validation[i] == true) {
                draw = false;
                break;
            } else {
                draw = true;
            }
        }
        for (int i = 0; i < 4; i++) {
            validation[i] = false;
        }
        return p;
    }

    public void setIrregular_polygon(int x, int y) {
        Point p=new Point(x,y);

        p=checkBonds(p);

        p.setX(CoordinateSystem.translateX(p.getX()));
        p.setY(CoordinateSystem.translateY(p.getY()));

        irregular_polygon.setXY(p);
    }

    public static void setVertex(ArrayList<Point> points) {
        vertex=points;
        Selection.setTransformed(true);
    }

    public static void clearFractal() {
        KochLine = false;
        mandelbrot = false;
        mandel.clear();
        notXmandelbrot.clear();
        notYmandelbrot.clear();

    }
}
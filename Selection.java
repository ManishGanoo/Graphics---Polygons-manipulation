import java.awt.*;

/**
 * Created by erosennin on 3/7/17.
 */
public abstract class Selection {
    private static int Num_Sides;
    private static String brush="irregular";
    private static Color color=Color.RED;
    private static Color color2=Color.CYAN;
    private static boolean boundFill=false;
    private static boolean FloodFill=false;
    public static boolean ScanFill=false;
    private static boolean drawing=true;
    private static boolean first=false;
    private static String sTransformation="new";
    private static boolean kochline=false;
    private static boolean mandelbrot=false;
    private static boolean transformed=false;
    private static int kochvalue=0;

    public static String getBrush() {
        return brush;
    }

    public static void setBrush(String brush) {
        Selection.brush = brush;
    }

    public static int getNum_Sides() {
        return Num_Sides;
    }

    public static void setNum_Sides(int num_Sides) {
        Num_Sides = num_Sides;
    }

    public static Color getColor() {
        return color;
    }

    public static void setColor2(Color color) {
        Selection.color2 = color;
    }

    public static Color getColor2() {
        return color2;
    }

    public static void setColor(Color color) {
        Selection.color = color;
    }

    public static boolean ismandelbrot() {
        return mandelbrot;
    }

    public static void setmandelbrot(boolean value) {
        mandelbrot = value;
    }

    public static boolean iskochline() {
        return kochline;
    }

    public static void setkochline(boolean value) {
        kochline = value;
    }

    public static void setkochvalue(int value){
        kochvalue=value;
    }

    public static int getkochvalue(){
        return kochvalue;
    }


    public static boolean isBoundFill() {
        return boundFill;
    }

    public static void setBoundFill(boolean value) {
        boundFill = value;
    }

    public static boolean isFloodFill() {
        return FloodFill;
    }

    public static void setFloodFill(boolean value) {
        FloodFill = value;
    }

    public static boolean isDrawing() {
        return drawing;
    }

    public static void setDrawing(boolean value) {
        drawing = value;
    }

    public static void setsTransformation(String sType){
        sTransformation=sType;
    }

    public static String getsTransformation(){
        return sTransformation;
    }

    public static void setFirst(boolean value){
        first=value;
    }

    public static boolean isFirst(){
        return first;
    }

    public static void setTransformed(boolean value){
        transformed=value;
    }

    public static boolean isTransformed(){
        return transformed;
    }

    public static void setScanFill(boolean value){
        ScanFill=value;
    }

    public static boolean isScanFill(){
        return ScanFill;
    }

}

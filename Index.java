import javax.swing.*;
import java.awt.*;

/**
 * Created by erosennin on 2/21/17.
 */
public class Index extends JPanel{
    public static PointInput pointInput=new PointInput();
    public static Menu menu=new Menu();
    public static MenuBar menuBar=new MenuBar();


    public Index(){

        BorderLayout layout=new BorderLayout();
        setLayout(layout);

        add(menu,BorderLayout.WEST);
        add(menuBar,BorderLayout.NORTH);
        add(pointInput);
    }

    public static void refresh(){
        pointInput.repaint();
    }

    public static void reset(){
        pointInput.ClearAll();
        Menu.changeStatus();
    }
}

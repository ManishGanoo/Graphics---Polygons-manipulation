/**
 * Created by erosennin on 9/20/2016.
 */
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.MenuBar;


public class MainPtI {
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    UIManager.put("nimbusBase", new ColorUIResource(37, 37, 37));
                    UIManager.put("textForeground", new ColorUIResource(0, 4, 138));
                    UIManager.put("defaultFont", new Font(Font.MONOSPACED, Font.BOLD, 16));
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        Dimension ScreenSize=Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame=new JFrame("Draw");


        frame.setPreferredSize(ScreenSize);

        Index index=new Index();

        frame.add(index);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
    }
}

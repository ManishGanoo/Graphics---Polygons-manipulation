import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by erosennin on 3/19/17.
 */
public class MenuBar extends JMenuBar {
    private JMenuBar bar;
    private JToggleButton grid;
    private JToggleButton gravity;
    private JButton scale;
    private JButton btnExit;
    private JButton btnClear;

    public MenuBar(){
        setVisible(true);
        setBackground(Color.RED);

        bar=new JMenuBar();
        bar.setBackground(Color.CYAN);
        bar.addNotify();

        grid=new JToggleButton("GRID");
        grid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CoordinateSystem.setGridon();
                Index.refresh();
            }
        });

        gravity=new JToggleButton("GRAVITY");
        gravity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CoordinateSystem.setGravityon();
                Index.refresh();
            }
        });

        scale=new JButton("SCALE");
        scale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int num;
                Icon icon=new ImageIcon(getClass().getResource("y.png"));
                try {
                    String xinput = String.valueOf(JOptionPane.showInputDialog(null, "Enter number: ", "Change Scale", JOptionPane.YES_NO_OPTION,
                            icon, null, "0"));
                    num = Integer.parseInt(xinput);
                    CoordinateSystem.setPower(num);
                    Index.refresh();
                }catch (Throwable throwable){}

            }
        });

        btnExit=new JButton("EXIT");
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        btnClear = new JButton("CLEAR");
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Index.reset();
            }
        });

        bar.add(grid);
        bar.add(gravity);
        bar.add(scale);
        bar.add(btnClear);
        bar.add(Box.createHorizontalGlue());
        bar.add(btnExit);
        add(bar);
    }
}

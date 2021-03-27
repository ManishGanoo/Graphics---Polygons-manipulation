import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

/**
 * Created by erosennin on 2/21/17.
 */
public class Menu extends JPanel{
    private JRadioButton Regular_P;
    private JRadioButton ireg;
    private ButtonGroup gp;

    private JPanel tabPanel;
    private JTabbedPane tabbedPane;

    private JLabel statusBar=new JLabel();

    private static JList list;
    private static DefaultListModel model;
    private static JScrollPane scrollPane;

    private JButton btnRemove;
    private JButton btnGo;

    private Dimension ScreenSize=Toolkit.getDefaultToolkit().getScreenSize();
    private static Dimension size=new Dimension(60,30);

    private static ArrayList<Point> vertex=new ArrayList<>();
    private static ArrayList<Point> NewVertex=new ArrayList<>();

    private Strans strans=new Strans();

    private Timer timer;

    private TransFormation_Matrix transFormation_matrix=new TransFormation_Matrix();

    private static ArrayList<Transformation> lstTransformation=new ArrayList<>();

    private static JPanel status;
    private static JLabel msg;
    private InformationPanel informationPanel=new InformationPanel();

    public Menu(){
        this.setPreferredSize(new Dimension((int)(ScreenSize.getWidth()*0.30),ScreenSize.height));
        this.setBackground(Color.DARK_GRAY);
        setLayout(new FlowLayout(FlowLayout.CENTER,20,20));

        Regular_P=new JRadioButton("REGULAR");
        ireg=new JRadioButton("IRREGULAR");
        add(Regular_P);
        add(ireg);
        ireg.setSelected(true);
        gp=new ButtonGroup();
        gp.add(Regular_P);
        gp.add(ireg);

        add(strans);

        tabPanel =new JPanel();
        tabPanel.setPreferredSize(new Dimension(600,(int)(ScreenSize.height*0.35)));
        tabPanel.setBackground(Color.DARK_GRAY);
        add(tabPanel);

        tabbedPane=new JTabbedPane();
        tabbedPane.setBackground(Color.black);
        tabbedPane.setForeground(Color.RED);

        tabbedPane.addTab("FILLING",new FillingPanel());
        tabbedPane.addTab("TRANSLATION",new TranslationPanel());
        tabbedPane.addTab("ROTATION",new RotationPanel());
        tabbedPane.addTab("SCALING",new ScalingPanel());
        tabbedPane.addTab("REFLECTION",new ReflectionPanel());
        tabbedPane.addTab("SHEAR",new ShearPanel());
        tabbedPane.addTab("FRACTALS",new FractalsPanel());
        tabbedPane.addTab("VERTEX",informationPanel);

        tabPanel.add(tabbedPane);

        model=new DefaultListModel();
        list=new JList(model);
        scrollPane=new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(300,(int)(ScreenSize.height*0.25)));
        add(scrollPane);

        list.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()==2){
                    list.clearSelection();
                    int index=list.getSelectedIndex();
                    list.removeSelectionInterval(index,index);
                    if (index!=-1){
                        model.remove(index);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        btnRemove=new JButton("REMOVE");
        add(btnRemove);
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index=list.getSelectedIndex();
                if (index!=-1){
                    model.remove(index);
                    lstTransformation.remove(index);
                }
            }
        });

        btnGo=new JButton("GO");
        add(btnGo);

        btnGo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setTransFormation(lstTransformation);
                model.clear();
            }
        });

        status=new JPanel();
        status.setBackground(Color.WHITE);
        status.setPreferredSize(new Dimension(300,(int)(ScreenSize.getHeight()*0.1)));
        msg=new JLabel("HELLO");
        status.add(msg);
        add(status);

        setColor(this);

        Handler handler=new Handler();
        Regular_P.addActionListener(handler);
        ireg.addActionListener(handler);
    }

    public static void changeText(String text){
        msg.setText(text);
    }

    public static void changeStatus(){
        if (!Selection.isDrawing()){
            status.setBackground(Color.RED);
            msg.setForeground(Color.WHITE);
            changeText("FILLING");
        }
        else {
            status.setBackground(Color.WHITE);
            msg.setForeground(Color.BLACK);
        }
    }

    private class Handler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand()=="REGULAR"){
                String brush="regular";
                Selection.setBrush(brush);
                Selection.setTransformed(false);
                Selection.setFirst(false);
                int num=0;
                String xinput="3";
                try {
                    Icon icon = new ImageIcon(getClass().getResource("s.png"));
                    xinput = String.valueOf(JOptionPane.showInputDialog(null, "Enter number of sides: ", "Sides", JOptionPane.PLAIN_MESSAGE, icon, null, "3"));
                    num = Integer.parseInt(xinput);
                    Selection.setNum_Sides(num);
                }catch (Throwable throwable){}
            }
            if (e.getActionCommand()=="IRREGULAR"){
                String brush="irregular";
                Selection.setBrush(brush);
                Selection.setTransformed(false);
                Selection.setFirst(false);
            }
        }
    }

    public class Strans extends JPanel{
        private JRadioButton rbNew;
        private JRadioButton rbContinuous;
        private ButtonGroup g;
        private GridBagConstraints constraints=new GridBagConstraints();
        private GridBagLayout layout=new GridBagLayout();

        public Strans(){
            setBackground(Color.DARK_GRAY);

            rbNew=new JRadioButton("NEW");
            rbContinuous=new JRadioButton("Continuous");
            g=new ButtonGroup();
            g.add(rbContinuous);
            g.add(rbNew);

            constraints.gridx=0;
            constraints.gridy=0;
            layout.setConstraints(rbNew,constraints);
            add(rbNew);

            constraints.gridx=1;
            layout.setConstraints(rbContinuous,constraints);
            add(rbContinuous);

            rbNew.setSelected(true);
            setColor(this);

            Handler handler=new Handler();
            rbContinuous.addActionListener(handler);
            rbNew.addActionListener(handler);
        }

        private class Handler implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rbNew.isSelected()){
                    Selection.setsTransformation("new");
                }
                else {
                    Selection.setsTransformation("continuous");
                }
            }
        }

    }

    public class InformationPanel extends JPanel{
        private  JLabel lblVertex1;
        private  JLabel lblVertex2;
        private GridBagLayout layout=new GridBagLayout();
        private GridBagConstraints constraints=new GridBagConstraints();
        public InformationPanel(){
            setLayout(layout);

            lblVertex1=new JLabel("<html>Hello<br>World</html>");
            constraints.gridx=0;
            constraints.gridy=0;
            layout.setConstraints(lblVertex1,constraints);
            add(lblVertex1);

            lblVertex2=new JLabel("<html>Please Draw a Polygon<br> and<br> set a transformation</html>");
            constraints.gridx=1;
            constraints.insets=new Insets(0,20,0,10);
            layout.setConstraints(lblVertex2,constraints);
            add(lblVertex2);
            setColor(this);
        }

        public void changeText1(StringBuilder text){
            lblVertex1.setText(text.toString());
        }

        public void changeText2(StringBuilder text){
            lblVertex2.setText(text.toString());
        }
    }

    public class FractalsPanel extends JPanel{
        private JPanel pFractals;
        private JRadioButton rbMandelbrot;
        private JRadioButton rbKochLine;
        private JButton fillbtnSet;
        private JButton fillbtnClear;
        private ButtonGroup gp3;
        private GridBagConstraints constraints=new GridBagConstraints();
        private GridBagLayout layout=new GridBagLayout();

        public FractalsPanel(){
            constraints.insets=new Insets(0,0,20,0);
            pFractals=new JPanel();
            pFractals.setLayout(layout);
            gp3=new ButtonGroup();

            rbMandelbrot=new JRadioButton("MANDELBROT");
            rbKochLine=new JRadioButton("KOCHLINE");
            gp3.add(rbMandelbrot);
            rbMandelbrot.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Selection.setDrawing(false);
                    Selection.setmandelbrot(true);

                }
            });
            gp3.add(rbKochLine);
            rbKochLine.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    String levelStr = JOptionPane.showInputDialog("Enter the depth of recursion");
                    Selection.setkochvalue(Integer.parseInt(levelStr));
                    Selection.setkochline(true);
                }
            });



            constraints.gridy=0;
            constraints.gridx=0;
            layout.setConstraints(rbMandelbrot,constraints);
            pFractals.add(rbMandelbrot);

            constraints.gridx=1;
            layout.setConstraints(rbKochLine,constraints);
            pFractals.add(rbKochLine);

            fillbtnClear=new JButton("CLEAR");
            fillbtnSet=new JButton("SET");

            fillbtnSet.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Selection.setDrawing(false);
                    //PointInput.fill(null);
                }
            });

            fillbtnClear.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Selection.setDrawing(true);
                    Selection.setkochline(false);
                    PointInput.clearFractal();
                    Selection.setmandelbrot(false);
                }
            });

            constraints.gridx=2;
            constraints.gridy=3;
            layout.setConstraints(fillbtnSet,constraints);
            pFractals.add(fillbtnSet);

            constraints.gridx=3;
            layout.setConstraints(fillbtnClear,constraints);
            pFractals.add(fillbtnClear);
            add(pFractals);

        }
    }

    public class FillingPanel extends JPanel{
        private JPanel pFill;
        private JRadioButton rbBound;
        private JRadioButton rbFlood;
        private JRadioButton rbScan;
        private JButton fillbtnColor;
        private JButton fillbtnSet;
        private JButton fillbtnClear;
        private ButtonGroup gp2;
        private GridBagConstraints constraints=new GridBagConstraints();
        private GridBagLayout layout=new GridBagLayout();
        private boolean scanfill=false;

        public FillingPanel(){
            constraints.insets=new Insets(0,0,20,0);

            pFill=new JPanel();
            pFill.setLayout(layout);

            gp2=new ButtonGroup();

            rbBound=new JRadioButton("BOUNDARY FILL");
            rbFlood=new JRadioButton("FLOOD FILL");
            rbScan=new JRadioButton("SCAN LINE");
            gp2.add(rbBound);
            rbBound.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Selection.setBoundFill(true);
                }
            });
            rbFlood.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Selection.setFloodFill(true);
                }
            });
            rbScan.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    scanfill=true;
                }
            });
            gp2.add(rbFlood);
            gp2.add(rbScan);


            constraints.gridy=0;
            constraints.gridx=0;

            layout.setConstraints(rbBound,constraints);
            pFill.add(rbBound);

            constraints.gridx=1;
            layout.setConstraints(rbFlood,constraints);
            pFill.add(rbFlood);

            constraints.gridx=2;
            layout.setConstraints(rbScan,constraints);
            pFill.add(rbScan);


            fillbtnColor=new JButton("COLOR");
            constraints.gridx=1;
            constraints.gridy=1;
            layout.setConstraints(fillbtnColor,constraints);
            pFill.add(fillbtnColor);

            fillbtnColor.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Color ini=Color.black;
                    Color color=JColorChooser.showDialog(null,"Pick your color ",ini);
                    Selection.setColor(color);
                }
            });

            fillbtnClear=new JButton("CLEAR");
            fillbtnSet=new JButton("SET");

            fillbtnSet.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Selection.setDrawing(false);
                    changeStatus();
                    if (scanfill){
                        Selection.setScanFill(true);
                        Color ini=Color.black;
                        Color color=JColorChooser.showDialog(null,"Pick your color for transformed polygon ",ini);
                        Selection.setColor2(color);
                        Index.refresh();
                    }
                }
            });

            fillbtnClear.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Selection.setDrawing(true);
                    changeStatus();
                    Selection.setBoundFill(false);
                    Selection.setFloodFill(false);
                    Selection.setScanFill(false);
                    PointInput.clearfill();
                }
            });

            constraints.gridx=2;
            constraints.gridy=3;
            layout.setConstraints(fillbtnSet,constraints);
            pFill.add(fillbtnSet);

            constraints.gridx=3;
            layout.setConstraints(fillbtnClear,constraints);
            pFill.add(fillbtnClear);

            add(pFill);
        }
    }

    public class TranslationPanel extends JPanel{
        private GridBagConstraints constraints=new GridBagConstraints();
        private GridBagLayout layout=new GridBagLayout();

        private JLabel lblX;
        private JLabel lblY;

        private JTextField txtX;
        private JTextField txtY;

        private JButton btnGO;
        private JButton btnADD;


        public TranslationPanel(){
            setLayout(layout);

            constraints.insets=new Insets(20,20,10,0);

            lblX=new JLabel("X");
            constraints.gridx=0;
            constraints.gridy=0;
            layout.setConstraints(lblX,constraints);
            add(lblX);

            txtX=new JTextField();
            constraints.gridx=1;
            layout.setConstraints(txtX,constraints);
            add(txtX);

            constraints.insets=new Insets(0,20,10,0);

            lblY=new JLabel("Y");
            constraints.gridx=0;
            constraints.gridy=1;
            layout.setConstraints(lblY,constraints);
            add(lblY);

            txtY=new JTextField();
            constraints.gridx=1;
            layout.setConstraints(txtY,constraints);
            add(txtY);

            btnADD=new JButton("ADD");
            constraints.gridx=2;
            constraints.gridy=2;
            layout.setConstraints(btnADD,constraints);
            add(btnADD);

            btnADD.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int tranX=0;
                    int tranY=0;
                    try{
                        tranX=Integer.parseInt(txtX.getText());
                        tranY=Integer.parseInt(txtY.getText());
                    }
                    catch(Exception exp){}

                    model.addElement("TRANSLATION of X: "+tranX+" units and Y: "+tranY+" units");
                    Transformation transformation=new Transformation("translation",tranX,tranY);
                    lstTransformation.add(transformation);
                }
            });

            btnGO=new JButton("GO");

            btnGO.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    double tranX=0;
                    double tranY=0;
                    try{
                        tranX=Double.parseDouble(txtX.getText());
                        tranY=Double.parseDouble(txtY.getText());
                    }
                    catch(Exception exp){}
                    Transformation transformation=new Transformation("translation",tranX,tranY);
                    NewVertex=transFormation_matrix.getNewVertex();
                    setAnimation(transformation);
                }
            });

            constraints.gridx=3;
            layout.setConstraints(btnGO,constraints);
            add(btnGO);

            setTxtSize(this);

        }
    }

    public class RotationPanel extends JPanel{
        private JRadioButton rbFixed;
        private JRadioButton rbOri;
        private ButtonGroup gp;
        private JLabel lblAngle;
        private JLabel lblRotationPt;
        private JTextField txtAngle;
        private JTextField txtX;
        private JTextField txtY;
        private JButton btnGo;
        private JButton btnAdd;
        private JRadioButton rbCl;
        private JRadioButton rbAnt;
        private GridBagConstraints constraints=new GridBagConstraints();
        private GridBagLayout layout=new GridBagLayout();

        public RotationPanel(){
            setLayout(layout);

            constraints.insets=new Insets(2,20,10,0);

            rbOri=new JRadioButton("ORIGIN");
            rbFixed=new JRadioButton("FIXED POINT");
            gp=new ButtonGroup();
            gp.add(rbFixed);
            gp.add(rbOri);
            rbOri.setSelected(true);

            constraints.gridx=0;
            constraints.gridy=0;
            layout.setConstraints(rbOri,constraints);
            add(rbOri);

            constraints.gridx=1;
            layout.setConstraints(rbFixed,constraints);
            add(rbFixed);

            constraints.insets=new Insets(0,20,5,0);

            lblAngle=new JLabel("ANGLE");
            constraints.gridx=0;
            constraints.gridy=1;
            layout.setConstraints(lblAngle,constraints);
            add(lblAngle);

            txtAngle=new JTextField(5);
            constraints.gridx=1;
            layout.setConstraints(txtAngle,constraints);
            add(txtAngle);

            ButtonGroup gp3=new ButtonGroup();

            rbCl=new JRadioButton("CLOCKWISE");
            rbAnt=new JRadioButton("ANTICLOCKWISE");
            rbCl.setSelected(true);
            gp3.add(rbCl);
            gp3.add(rbAnt);

            constraints.gridx=0;
            constraints.gridy=2;
            layout.setConstraints(rbCl,constraints);
            add(rbCl);

            constraints.gridx=1;
            layout.setConstraints(rbAnt,constraints);
            add(rbAnt);

            lblRotationPt=new JLabel("Rotation Point");
            constraints.gridx=0;
            constraints.gridy=3;
            layout.setConstraints(lblRotationPt,constraints);
            add(lblRotationPt);

            txtX=new JTextField(5);
            txtX.setEnabled(false);
            constraints.gridx=1;
            layout.setConstraints(txtX,constraints);
            add(txtX);

            txtY=new JTextField(5);
            txtY.setEnabled(false);
            constraints.gridx=2;
            layout.setConstraints(txtY,constraints);
            add(txtY);

            constraints.insets=new Insets(0,0,0,10);


            btnAdd=new JButton("ADD");
            constraints.gridx=2;
            constraints.gridy=4;
            layout.setConstraints(btnAdd,constraints);
            add(btnAdd);

            btnGo=new JButton("GO");
            constraints.gridx=3;
            layout.setConstraints(btnGo,constraints);
            add(btnGo);

            btnAdd.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int angle=0;
                    int x=0;
                    int y=0;
                    try{
                        angle=Integer.parseInt(txtAngle.getText());
                        x=Integer.parseInt(txtX.getText());
                        y=Integer.parseInt(txtY.getText());
                    }
                    catch(Exception ex){}
                    model.addElement("ROTATION of "+angle+" degree Rotation pt of: "+x+","+y);
                    if (rbCl.isSelected()){
                        angle=-1*angle;
                    }
                    Transformation transformation=null;

                    if (rbOri.isSelected()){
                        transformation=new Transformation("orotation",new Point(x,y),angle);
                    }
                    else if (rbFixed.isSelected()){
                        transformation=new Transformation("frotation",new Point(x,y),angle);
                    }
                    lstTransformation.add(transformation);
                }
            });

            btnGo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    double angle=0;
                    double x=0;
                    double y=0;
                    try{
                        angle=Double.parseDouble(txtAngle.getText());
                        x=Double.parseDouble(txtX.getText());
                        y=Double.parseDouble(txtY.getText());
                    }
                    catch (Exception exp){}
                    if (rbCl.isSelected()){
                        angle=-1*angle;
                    }
                    Transformation transformation=null;

                    if (rbOri.isSelected()){
                        transformation=new Transformation("orotation",new Point(x,y),angle);
                    }
                    else if (rbFixed.isSelected()){
                        transformation=new Transformation("frotation",new Point(x,y),angle);
                    }
                    NewVertex=transFormation_matrix.getNewVertex();
                    setAnimation(transformation);
                }
            });



            Handler handler=new Handler();
            rbFixed.addActionListener(handler);
            rbOri.addActionListener(handler);

            setTxtSize(this);
        }

        private class Handler implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rbFixed.isSelected()){
                    txtX.setEnabled(true);
                    txtY.setEnabled(true);
                    txtX.setText("");
                    txtY.setText("");
                }
                else {
                    txtX.setEnabled(false);
                    txtY.setEnabled(false);
                    txtX.setText("");
                    txtY.setText("");
                }
            }
        }

    }

    public class ScalingPanel extends JPanel{
        private JRadioButton rbFixed;
        private JRadioButton rbOri;
        private ButtonGroup gp;
        private JLabel lblSx;
        private JLabel lblSy;
        private JLabel lblFixed;
        private JTextField txtSx;
        private JTextField txtSy;
        private JTextField txtX;
        private JTextField txtY;
        private JButton btnGo;
        private JButton btnAdd;
        private GridBagConstraints constraints=new GridBagConstraints();
        private GridBagLayout layout=new GridBagLayout();

        public ScalingPanel(){
            setLayout(layout);

            constraints.insets=new Insets(-10,20,10,0);

            rbOri=new JRadioButton("ORIGIN");
            rbFixed=new JRadioButton("FIXED POINT");
            gp=new ButtonGroup();
            gp.add(rbFixed);
            gp.add(rbOri);
            rbOri.setSelected(true);

            constraints.gridx=0;
            constraints.gridy=0;
            layout.setConstraints(rbOri,constraints);
            add(rbOri);

            constraints.gridx=1;
            layout.setConstraints(rbFixed,constraints);
            add(rbFixed);

            constraints.insets=new Insets(0,20,5,0);

            lblSx=new JLabel("Scaling factor X:");
            constraints.gridx=0;
            constraints.gridy=1;
            layout.setConstraints(lblSx,constraints);
            add(lblSx);

            txtSx=new JTextField(5);
            constraints.gridx=1;
            layout.setConstraints(txtSx,constraints);
            add(txtSx);

            lblSy=new JLabel("Scaling factor y: ");
            constraints.gridx=2;
            layout.setConstraints(lblSy,constraints);
            add(lblSy);

            txtSy=new JTextField(5);
            constraints.gridx=3;
            layout.setConstraints(txtSy,constraints);
            add(txtSy);

            lblFixed=new JLabel("Fixed point");
            constraints.gridx=0;
            constraints.gridy=2;
            layout.setConstraints(lblFixed,constraints);
            add(lblFixed);

            txtX=new JTextField(5);
            txtX.setEnabled(false);
            constraints.gridx=1;
            layout.setConstraints(txtX,constraints);
            add(txtX);

            txtY=new JTextField(5);
            txtY.setEnabled(false);
            constraints.gridx=2;
            layout.setConstraints(txtY,constraints);
            add(txtY);

            constraints.insets=new Insets(0,0,0,10);

            btnAdd=new JButton("ADD");
            constraints.gridx=2;
            constraints.gridy=4;
            layout.setConstraints(btnAdd,constraints);
            add(btnAdd);

            btnGo=new JButton("GO");
            constraints.gridx=3;
            layout.setConstraints(btnGo,constraints);
            add(btnGo);

            btnAdd.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    double sx=0;
                    double sy=0;
                    int x=0;
                    int y=0;
                    try{
                        sx=Double.parseDouble(txtSx.getText());
                        if (sx==0){
                            sx=1;
                        }
                        sy=Double.parseDouble(txtSy.getText());
                        if (sy==0){
                            sy=1;
                        }
                        x=Integer.parseInt(txtX.getText());
                        y=Integer.parseInt(txtY.getText());
                    }
                    catch(Exception ex){}
                    model.addElement("Scaling of Sx:"+sx+" and Sy: "+sy+" with pt : "+x+","+y);
                    Transformation transformation=null;

                    if (rbOri.isSelected()){
                        transformation=new Transformation("oscaling",sx,sy,new Point(x,y));
                    }
                    else if(rbFixed.isSelected()){
                        transformation=new Transformation("fscaling",sx,sy,new Point(x,y));
                    }
                    lstTransformation.add(transformation);
                }
            });

            btnGo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    double sx=1;
                    double sy=1;
                    double x=0;
                    double y=0;
                    try{
                        sx=Double.parseDouble(txtSx.getText());
                        if (sx==0){
                            sx=1;
                        }
                        sy=Double.parseDouble(txtSy.getText());
                        if (sy==0){
                            sy=1;
                        }
                        x=Double.parseDouble(txtX.getText());
                        y=Double.parseDouble(txtY.getText());
                    }
                    catch(Exception ex){}

                    Transformation transformation=null;

                    if (rbOri.isSelected()){
                        transformation=new Transformation("oscaling",sx,sy,new Point(x,y));
                    }
                    else if(rbFixed.isSelected()){
                        transformation=new Transformation("fscaling",sx,sy,new Point(x,y));
                    }
                    NewVertex=transFormation_matrix.getNewVertex();
                    setAnimation(transformation);
                }
            });



            Handler handler=new Handler();
            rbFixed.addActionListener(handler);
            rbOri.addActionListener(handler);

            setTxtSize(this);
        }

        private class Handler implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rbFixed.isSelected()){
                    txtX.setEnabled(true);
                    txtY.setEnabled(true);
                    txtX.setText("");
                    txtY.setText("");
                }
                else {
                    txtX.setEnabled(false);
                    txtY.setEnabled(false);
                    txtX.setText("");
                    txtY.setText("");
                }
            }
        }

    }

    public class ReflectionPanel extends JPanel{
        private JRadioButton rbXaxis;
        private JRadioButton rbYaxis;
        private JRadioButton rbXYaxis;
        private JRadioButton rbYXaxis;
        private JRadioButton rbY_Xaxis;
        private ButtonGroup g;
        private JButton btnGo;
        private JButton btnAdd;

        private GridBagConstraints constraints=new GridBagConstraints();
        private GridBagLayout layout=new GridBagLayout();

        public ReflectionPanel(){
            setLayout(layout);

            constraints.insets=new Insets(10,20,10,0);

            constraints.gridy=0;

            rbXaxis=new JRadioButton("X-axis");
            rbYaxis=new JRadioButton("Y-axis");
            rbXYaxis=new JRadioButton("X-Y axis");
            rbYXaxis=new JRadioButton("Y=X");
            rbY_Xaxis=new JRadioButton("Y=-X");

            g=new ButtonGroup();

            g.add(rbXaxis);
            g.add(rbYaxis);
            g.add(rbXYaxis);
            g.add(rbYXaxis);
            g.add(rbY_Xaxis);


            constraints.gridx=0;
            layout.setConstraints(rbXaxis,constraints);
            add(rbXaxis);

            constraints.gridx=1;
            layout.setConstraints(rbYaxis,constraints);
            add(rbYaxis);

            constraints.gridx=2;
            //constraints.gridy=1;
            layout.setConstraints(rbXYaxis,constraints);
            add(rbXYaxis);

            constraints.gridx=0;
            constraints.gridy=2;
            layout.setConstraints(rbYXaxis,constraints);
            add(rbYXaxis);

            constraints.gridx=1;
            layout.setConstraints(rbY_Xaxis,constraints);
            add(rbY_Xaxis);

            btnAdd=new JButton("ADD");
            constraints.gridx=2;
            constraints.gridy=3;
            layout.setConstraints(btnAdd,constraints);
            add(btnAdd);

            btnGo=new JButton("GO");
            constraints.gridx=3;
            layout.setConstraints(btnGo,constraints);
            add(btnGo);

            btnGo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Transformation transformation;
                    int choice=4;
                    if (rbXaxis.isSelected()){
                        choice=1;
                    }
                    else if (rbYaxis.isSelected()){
                        choice=2;
                    }
                    else if (rbXYaxis.isSelected()){
                        choice=3;
                    }
                    else if (rbYXaxis.isSelected()){
                        choice=4;
                    }
                    else if (rbY_Xaxis.isSelected()){
                        choice=5;
                    }

                    transformation=new Transformation("reflection",choice);
                    NewVertex=transFormation_matrix.getNewVertex();
                    setTransformation(transformation);
                }
            });

            btnAdd.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Transformation transformation;
                    String msg="";
                    int choice=0;
                    if (rbXaxis.isSelected()){
                        choice=1;
                        msg="X-axis";
                    }
                    else if (rbYaxis.isSelected()){
                        choice=2;
                        msg="Y-axis";
                    }
                    else if (rbXYaxis.isSelected()){
                        choice=3;
                        msg="XY-axis";
                    }
                    else if (rbYXaxis.isSelected()){
                        choice=4;
                        msg="Y=X";
                    }
                    else if (rbY_Xaxis.isSelected()){
                        choice=5;
                        msg="Y=-X";
                    }

                    transformation=new Transformation("reflection",choice);
                    model.addElement("Reflection in the "+msg);
                    lstTransformation.add(transformation);
                }
            });

            setColor(this);

        }


    }

    public class ShearPanel extends JPanel {
        private JRadioButton rbX;
        private JRadioButton rbY;
        private ButtonGroup g;
        private JLabel lblShx;
        private JLabel lblShy;
        private JTextField txtShx;
        private JTextField txtShy;

        private JRadioButton rbXref;
        private JRadioButton rbYref;
        private JRadioButton rbOri;
        private ButtonGroup g2;

        private JTextField txtXref;
        private JTextField txtYref;

        private JButton btnGo;
        private JButton btnAdd;

        private GridBagConstraints constraints=new GridBagConstraints();
        private GridBagLayout layout=new GridBagLayout();

        public ShearPanel(){

            constraints.insets=new Insets(-7,0,0,0);
            rbX=new JRadioButton("X-axis");
            rbY=new JRadioButton("Y-axis");
            g=new ButtonGroup();
            g.add(rbX);
            g.add(rbY);

            setLayout(layout);

            constraints.insets=new Insets(15,20,5,0);

            constraints.gridy=0;
            constraints.gridx=0;
            layout.setConstraints(rbX,constraints);
            add(rbX);

            constraints.gridx=1;
            layout.setConstraints(rbY,constraints);
            add(rbY);

            constraints.insets=new Insets(0,0,-5,0);


            lblShx=new JLabel("Shx");
            lblShy=new JLabel("Shy");
            txtShx=new JTextField(5);
            txtShy=new JTextField(5);
            txtShx.setEnabled(false);
            txtShy.setEnabled(false);

            constraints.gridx=0;
            constraints.gridy=1;
            layout.setConstraints(lblShx,constraints);
            add(lblShx);

            constraints.insets=new Insets(0,0,5,0);

            constraints.gridx=1;
            layout.setConstraints(txtShx,constraints);
            add(txtShx);

            constraints.gridx=2;
            layout.setConstraints(lblShy,constraints);
            add(lblShy);

            constraints.gridx=3;
            layout.setConstraints(txtShy,constraints);
            add(txtShy);

            txtShx.setText("0");
            txtShy.setText("0");



            rbXref=new JRadioButton("Xref");
            constraints.gridx=0;
            constraints.gridy=2;
            layout.setConstraints(rbXref,constraints);
            add(rbXref);

            rbYref=new JRadioButton("Yref");
            constraints.gridx=1;
            layout.setConstraints(rbYref,constraints);
            add(rbYref);

            rbOri=new JRadioButton("ORIGIN");
            constraints.gridx=2;
            layout.setConstraints(rbOri,constraints);
            add(rbOri);

            g2=new ButtonGroup();
            g2.add(rbXref);
            g2.add(rbYref);
            g2.add(rbOri);

            rbOri.setSelected(true);

            txtXref=new JTextField();
            txtXref.setEnabled(false);
            constraints.gridx=0;
            constraints.gridy=3;
            layout.setConstraints(txtXref,constraints);
            add(txtXref);

            txtYref=new JTextField();
            txtYref.setEnabled(false);
            constraints.gridx=1;
            layout.setConstraints(txtYref,constraints);
            add(txtYref);
            constraints.insets=new Insets(-20,0,-15,0);


            btnAdd=new JButton("ADD");
            btnGo=new JButton("GO");

            constraints.gridx=2;
            constraints.gridy=4;
            layout.setConstraints(btnAdd,constraints);
            add(btnAdd);

            constraints.gridx=3;
            layout.setConstraints(btnGo,constraints);
            add(btnGo);

            btnGo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    double Shx=2;
                    double Shy=2;
                    int choice=0;
                    double xref=0;
                    double yref=0;

                    try{
                        Shx=Double.parseDouble(txtShx.getText());
                        Shy=Double.parseDouble(txtShy.getText());
                        xref=Double.parseDouble(txtXref.getText());
                        yref=Double.parseDouble(txtYref.getText());
                    }
                    catch(Exception exp){}

                    if (rbOri.isSelected()){
                        if (rbX.isSelected()){
                            choice=1;
                        }
                        else if (rbY.isSelected()){
                            choice=2;
                        }
                    }

                    if (rbXref.isSelected()){
                        choice=3;
                    }
                    else if (rbYref.isSelected()){
                        choice=4;
                    }
                    /*Transformation transformation=new Transformation("shear",choice,Shx,Shy,xref,yref);
                    NewVertex=transFormation_matrix.getNewVertex();
                    setTransformation(transformation);*/


                    Transformation transformation=new Transformation("shear",choice,Shx,Shy,xref,yref);
                    NewVertex=transFormation_matrix.getNewVertex();
                    setAnimation(transformation);
                }
            });

            btnAdd.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    double Shx=0;
                    double Shy=0;
                    int choice=0;
                    double xref=0;
                    double yref=0;

                    try{
                        Shx=Double.parseDouble(txtShx.getText());
                        Shy=Double.parseDouble(txtShy.getText());
                        xref=Double.parseDouble(txtXref.getText());
                        yref=Double.parseDouble(txtYref.getText());
                    }
                    catch(Exception exp){}

                    if (rbOri.isSelected()){
                        if (rbX.isSelected()){
                            choice=1;
                        }
                        else if (rbY.isSelected()){
                            choice=2;
                        }
                    }

                    if (rbXref.isSelected()){
                        choice=3;
                    }
                    else if (rbYref.isSelected()){
                        choice=4;
                    }

                    Transformation transformation=new Transformation("shear",choice,Shx,Shy,xref,yref);
                    model.addElement("Shear with Shx: "+Shx+" and Shy: "+Shy);
                    lstTransformation.add(transformation);
                }
            });

            Handler handler=new Handler();
            rbX.addActionListener(handler);
            rbY.addActionListener(handler);
            rbYref.addActionListener(handler);
            rbXref.addActionListener(handler);
            rbOri.addActionListener(handler);

        //setColor(this);
        setTxtSize(this);
        }

        private class Handler implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand()=="X-axis"){
                    txtShx.setEnabled(true);
                    txtShy.setEnabled(false);
                }
                else if (e.getActionCommand()=="Y-axis"){
                    txtShx.setEnabled(false);
                    txtShy.setEnabled(true);
                }
                else if (e.getActionCommand()=="XY-axis"){
                    txtShx.setEnabled(true);
                    txtShy.setEnabled(true);
                }

                if (e.getActionCommand()=="Xref"){
                    txtXref.setEnabled(false);
                    txtYref.setEnabled(true);
                }
                else if (e.getActionCommand()=="Yref"){
                    txtXref.setEnabled(true);
                    txtYref.setEnabled(false);
                }
                else if (e.getActionCommand()=="ORIGIN"){
                    txtXref.setEnabled(false);
                    txtYref.setEnabled(false);
                }
            }
        }
    }

    public static void setColor(JComponent panel){
        for(Component control : panel.getComponents())
        {
            if(control instanceof JButton)
            {
                JButton ctrl = (JButton) control;
                ctrl.setForeground(new Color(0x000000));
            }
            else if (control instanceof JLabel){
                JLabel lbl=(JLabel) control;
                lbl.setForeground(new Color(0, 0, 0));
            }
            else if (control instanceof JTextField){
                JTextField txt=(JTextField) control;
                txt.setForeground(new Color(0, 0, 0));
                txt.setFont(new Font("Tahoma",Font.ITALIC,14));
            }
            else if (control instanceof JCheckBox){
                JCheckBox cb=(JCheckBox) control;
                cb.setBackground(new Color(0, 0, 0));
                cb.setForeground(new Color(0, 0, 0));
                cb.setFont(new Font("Tahoma",Font.BOLD,16));
            }
            else if (control instanceof JRadioButton){
                JRadioButton rb=(JRadioButton) control;
                rb.setBackground(new Color(0, 0, 0));
                rb.setForeground(new Color(0, 0, 0));
                rb.setFont(new Font("Tahoma",Font.BOLD,16));
            }
        }
    }

    public static void setTxtSize(JPanel panel){
        for(Component control : panel.getComponents())
        {
            if (control instanceof JTextField){
                JTextField txt=(JTextField) control;
                txt.setPreferredSize(size);
                txt.setMaximumSize(size);
                txt.setMinimumSize(size);
            }
        }
    }

    public void setTransformation(Transformation transformation){
        if (Selection.getBrush()=="regular"){
            vertex=Regular_Polygon.getVertex();
        }
        else {
            vertex=Irregular_Polygon.getVertex();
        }

        try{
            if (!Selection.isFirst()){
                NewVertex=vertex;
                Selection.setFirst(true);
            }
        }
        catch(Exception e){}


        if (Selection.getsTransformation().equals("new")){
            transFormation_matrix=new TransFormation_Matrix();
            if (Selection.getBrush()=="regular"){
                vertex=Regular_Polygon.getVertex();
            }
            else {
                vertex=Irregular_Polygon.getVertex();
            }
            vertex=transFormation_matrix.getNewVertex(vertex,transformation);
            PointInput.setVertex(vertex);
            setText();
            Index.refresh();
        }
        if (Selection.getsTransformation().equals("continuous")){
            vertex=transFormation_matrix.getNewVertex(NewVertex,transformation);
            PointInput.setVertex(vertex);
            setText();
            Index.refresh();
        }
    }

    public void setTransFormation(ArrayList<Transformation> lstTransformation){
        if (Selection.getBrush()=="regular"){
            vertex=Regular_Polygon.getVertex();
        }
        else {
            vertex=Irregular_Polygon.getVertex();
        }


        double[][] Matrix=transFormation_matrix.BuildMatrix(lstTransformation);

        vertex=transFormation_matrix.getNewVertex(vertex,Matrix);
        PointInput.setVertex(vertex);
        setText();
        Index.refresh();
    }

    public void setText(){
        StringBuilder stringBuilder1=new StringBuilder();
        StringBuilder stringBuilder2=new StringBuilder();

        try{
            if (Selection.getBrush()=="regular"){
                vertex=Regular_Polygon.getVertex();
            }
            else {
                vertex=Irregular_Polygon.getVertex();
            }

            stringBuilder1.append("<html><strong>Original polygon:</strong><br>");
            int i=1;
            for (Point x:vertex){
                String str=String.format("<br><i>V%d X:</i> %.2f <i>Y: </i>%.2f<br>",i,(float)x.getX(),(float)x.getY());
                stringBuilder1.append(str);
                i++;
            }
            stringBuilder1.append("</html>");

            stringBuilder2.append("<html><strong>Transformed polygon: </strong><br>");
            int j=1;
            for (Point x:transFormation_matrix.getNewVertex()){
                String str=String.format("<br><i>V %d X:</i> %.2f <i>Y:</i> %.2f<br>",j,(float)x.getX(),(float)x.getY());
                stringBuilder2.append(str);
                j++;
            }
            stringBuilder2.append("</html>");

            informationPanel.changeText1(stringBuilder1);
            informationPanel.changeText2(stringBuilder2);
        }
        catch(Exception e){}


    }

    public void setAnimation(Transformation transformation){
        InitialiseTimer();
        timer.scheduleAtFixedRate(new Animation(transformation),0,500);
    }

    public void InitialiseTimer(){
        timer=new Timer();
    }

    public void stopTimer(){
        timer.cancel();
    }

    public class Animation extends TimerTask{
        private Transformation transformation=null;
        private double interval;
        private double interval2;
        private double angle;
        private double Sx;
        private double Sy;
        private double Tx;
        private double Ty;
        private double Shx;
        private double Shy;

        public Animation(Transformation transformation){
            this.transformation=transformation;


            switch (transformation.getType()){
                case "orotation":
                case "frotation":{
                    interval=transformation.getAngle()/10;
                    angle=transformation.getAngle()/10;
                }break;
                case "translation":{
                    if (transformation.getTranX()==0){
                        interval2=transformation.getTranY()/10;
                        Ty=transformation.getTranY()/10;
                    }
                    else if (transformation.getTranY()==0){
                        interval=transformation.getTranX()/10;
                        Tx=transformation.getTranX()/10;
                    }
                    else {
                        interval=transformation.getTranX()/10;
                        interval2=transformation.getTranY()/10;
                        Tx=transformation.getTranX()/10;
                        Ty=transformation.getTranY()/10;
                    }
                }break;
                case "oscaling":
                case "fscaling":{
                    interval=transformation.getSx()/10;
                    interval2=transformation.getSy()/10;
//                    Sx=transformation.getSx()/10;
//                    Sy=transformation.getSy()/10;
                    Sx=1;
                    Sy=1;
                }break;
                case "shear":{
                    interval=transformation.getShx()/10;
                    interval2=transformation.getShy()/10;
                    Shy=transformation.getShx()/10;
                    Shx=transformation.getShy()/10;
                }break;
            }
        }

        @Override
        public void run(){
            switch (transformation.getType()){
                case "orotation":
                case "frotation":{
                    Transformation transformation1=new Transformation(transformation.getType(),transformation.getP(),angle);
                    setTransformation(transformation1);
                    angle=angle+interval;
                    if (Math.abs(angle)>Math.abs(transformation.getAngle())){
                        stopTimer();
                    }
                }break;
                case "translation":{
                    if (transformation.getTranX()==0){
                        Transformation transformation1=new Transformation(transformation.getType(),transformation.getTranX(),Ty);
                        setTransformation(transformation1);
                        Ty=Ty+interval2;
                        if (Math.abs(Ty)>Math.abs(transformation.getTranY())){
                            stopTimer();
                        }
                    }
                    else if (transformation.getTranY()==0){
                        Transformation transformation1=new Transformation(transformation.getType(),Tx,transformation.getTranY());
                        setTransformation(transformation1);
                        Tx=Tx+interval;
                        if (Math.abs(Tx)>transformation.getTranX()){
                            stopTimer();
                        }
                    }
                    else {
                        Transformation transformation1=new Transformation(transformation.getType(),Tx,Ty);
                        setTransformation(transformation1);
                        Tx=Tx+interval;
                        Ty=Ty+interval2;
                        if ((Math.abs(Tx)>transformation.getTranX())&&(Math.abs(Ty)>Math.abs(transformation.getTranY()))){
                            stopTimer();
                        }
                    }
                }break;
                case "oscaling":
                case "fscaling":{
                    Transformation transformation1=new Transformation(transformation.getType(),Sx,Sy,transformation.getP());
                    setTransformation(transformation1);
                    if ((transformation.getSx()<1)&&(transformation.getSy()<1)){
                        Sx=Sx-interval;
                        Sy=Sy-interval2;
                        if ((Math.abs(Sx)<Math.abs(transformation.getSx()))&&(Math.abs(Sy)<Math.abs(transformation.getSy()))){
                            stopTimer();
                        }
                    }
                    else{
                        Sx=Sx+interval;
                        Sy=Sy+interval2;
                        if ((Math.abs(Sx)>Math.abs(transformation.getSx()))&&(Math.abs(Sy)>Math.abs(transformation.getSy()))){
                            stopTimer();
                        }
                    }
                }break;
                case "shear":{
                    Transformation transformation1=new Transformation(transformation.getType(),transformation.getShear(),Shx,Shy,transformation.getXref(),transformation.getYref());
                    setTransformation(transformation1);
                    Shx=Shx+interval;
                    Shy=Shy+interval2;
                    if ((Math.abs(Shx)>Math.abs(transformation.getShx()))&&(Math.abs(Shy)>Math.abs(transformation.getShy()))){
                        stopTimer();
                    }
                }break;
            }
        }
    }
}

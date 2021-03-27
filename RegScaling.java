import java.util.ArrayList;

/**
 * Created by erosennin on 4/2/17.
 */
public class RegScaling {
    public static ArrayList<Point> Scaling(ArrayList<Point> vertex,double Sx,double Sy){
        ArrayList<Point> NewVertex=new ArrayList<>();
        double[][] ScalingMatrix=new double[3][3];
        double[][] Result=new double[3][1];

        ScalingMatrix[0][0]=Sx;
        ScalingMatrix[1][1]=Sy;
        ScalingMatrix[2][2]=1;


        for (int i=0;i<vertex.size();i++){
            double[][] vectorMatrix=new double[3][1];
            vectorMatrix[0][0]=vertex.get(i).getX();
            vectorMatrix[1][0]=vertex.get(i).getY();
            vectorMatrix[2][0]=1;

            for (int j=0;j<3;j++){
                for (int z=0;z<3;z++){
                    Result[j][0]+=(ScalingMatrix[j][z]*vectorMatrix[z][0]);
                }
            }
            NewVertex.add(new Point(Result[0][0],Result[1][0]));
            Result=new double[3][1];

        }

        return NewVertex;
    }

    public static void main(String[] args){
        ArrayList<Point> vertex=new ArrayList<>();

        vertex.add(new Point(1,2));
        vertex.add(new Point(2,4));
        vertex.add(new Point(3,2));

        ArrayList<Point> NewV=RegScaling.Scaling(vertex,0.5,3);

        for (Point x:NewV){
            System.out.println(x.getX()+","+x.getY());
        }
    }
}



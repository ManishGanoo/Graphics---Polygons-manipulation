import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.ArrayList;

/**
 * Created by erosennin on 4/7/17.
 */
public class TransFormation_Matrix {

    private double[][] ScalingMatrix=new double[3][3];
    private double[][] RotationMatrix=new double[3][3];
    private double[][] TranslationMatrix=new double[3][3];
    private double[][] ReflectionMatrix=new double[3][3];
    private double[][] ShearMatrix=new double[3][3];
    private ArrayList<Point> transformed=new ArrayList<>();

    public void setScalingMatrix(double Sx,double Sy){
        ScalingMatrix[0][0]=Sx;
        ScalingMatrix[1][1]=Sy;
        ScalingMatrix[2][2]=1;
    }

    public void setTranslationMatrix(double tx,double ty){
        TranslationMatrix[0][0]=1;
        TranslationMatrix[1][1]=1;
        TranslationMatrix[0][2]=tx;
        TranslationMatrix[1][2]=ty;
        TranslationMatrix[2][2]=1;
    }

    public void setRotationMatrix(double fita){
        double rad=Degree2Rad(fita);
        double sin=Math.sin(rad);
        double cos=Math.cos(rad);

        RotationMatrix[0][0]=cos;
        RotationMatrix[0][1]=-1*sin;
        RotationMatrix[1][0]=sin;
        RotationMatrix[1][1]=cos;
        RotationMatrix[2][2]=1;
    }

    public void setReflectionMatrix(int choice){
        switch (choice){
            case 1:{
                ReflectionMatrix[0][0]=1;
                ReflectionMatrix[1][1]=-1;
                ReflectionMatrix[2][2]=1;
            }break;
            case 2:{
                ReflectionMatrix[0][0]=-1;
                ReflectionMatrix[1][1]=1;
                ReflectionMatrix[2][2]=1;
            }break;
            case 3:{
                ReflectionMatrix[0][0]=-1;
                ReflectionMatrix[1][1]=-1;
                ReflectionMatrix[2][2]=1;
            }break;
            case 4:{
                ReflectionMatrix[0][1]=1;
                ReflectionMatrix[1][0]=1;
                ReflectionMatrix[2][2]=1;
            }break;
            case 5:{
                ReflectionMatrix[0][1]=-1;
                ReflectionMatrix[1][0]=-1;
                ReflectionMatrix[2][2]=1;
            }break;
        }
    }

    public void setShearMatrix(Transformation transformation){
        switch (transformation.getShear()){
            case 1:{
                ShearMatrix[0][0]=1;
                ShearMatrix[1][1]=1;
                ShearMatrix[2][2]=1;
                ShearMatrix[0][1]=transformation.getShx();
            }break;

            case 2:{
                ShearMatrix[0][0]=1;
                ShearMatrix[1][1]=1;
                ShearMatrix[2][2]=1;
                ShearMatrix[1][0]=transformation.getShy();
            }break;

            case 3:{
                ShearMatrix[0][0]=1;
                ShearMatrix[0][1]=transformation.getShx();
                ShearMatrix[0][2]=(-1*transformation.getShx()*transformation.getYref());
                ShearMatrix[1][1]=1;
                ShearMatrix[2][2]=1;
            }break;

            case 4:{
                ShearMatrix[0][0]=1;
                ShearMatrix[1][0]=transformation.getShy();
                ShearMatrix[1][2]=(-1*transformation.getShy()*transformation.getXref());
                ShearMatrix[1][1]=1;
                ShearMatrix[2][2]=1;
            }break;
        }
    }

    public double Degree2Rad(double degree){
        double rad;
        rad=(Math.PI/180)*degree;
        return rad;
    }

    public double[][] getScalingMatrix() {
        return ScalingMatrix;
    }

    public double[][] getRotationMatrix() {
        return RotationMatrix;
    }

    public double[][] getTranslationMatrix() {
        return TranslationMatrix;
    }

    public double[][] getReflectionMatrix(){
        return ReflectionMatrix;
    }

    public double[][] getShearMatrix(){
        return ShearMatrix;
    }

    public void setNewVertex(ArrayList<Point> vertex){
        transformed=vertex;
    }

    public ArrayList<Point> getNewVertex(ArrayList<Point> vertex,Transformation transformation){
        double[][] Matrix;

        Matrix=getMatrix(transformation);
        ArrayList<Point> NewVertex;

        NewVertex=getNewVertex(vertex,Matrix);

        setNewVertex(NewVertex);
        return NewVertex;
    }

    public ArrayList<Point> getNewVertex(){
        return transformed;
    }

    public ArrayList<Point> getNewVertex(ArrayList<Point> vertex,double[][] Matrix){
        double[][] Result=new double[3][1];
        ArrayList<Point> NewVertex=new ArrayList<>();


        for (int i=0;i<vertex.size();i++){
            double[][] vectorMatrix=new double[3][1];
            vectorMatrix[0][0]=vertex.get(i).getX();
            vectorMatrix[1][0]=vertex.get(i).getY();
            vectorMatrix[2][0]=1;

            for (int j=0;j<3;j++){
                for (int z=0;z<3;z++){
                    Result[j][0]+=(Matrix[j][z]*vectorMatrix[z][0]);
                }
            }
            NewVertex.add(new Point(Result[0][0],Result[1][0]));
            Result=new double[3][1];
        }

        setNewVertex(NewVertex);
        return NewVertex;
    }

    public double[][] BuildMatrix(ArrayList<Transformation> lstTransformation){
        double[][] Matrix;
        double[][] FinalMatrix=new double[3][3];
        Transformation transformation;
        for (int i=(lstTransformation.size()-1);i>=0;i--) {
            transformation = lstTransformation.get(i);

            Matrix=getMatrix(transformation);

            if (i==(lstTransformation.size()-1)){
                FinalMatrix=Matrix;
            }
            else {
                FinalMatrix=MatrixMultiplication3x3(FinalMatrix,Matrix);
            }
        }
        return FinalMatrix;
    }

    public double[][] getMatrix(Transformation transformation){
        double[][] Matrix=new double[3][3];

        switch (transformation.getType()){
            case "translation":{
                setTranslationMatrix(transformation.getTranX(),transformation.getTranY());
                Matrix=getTranslationMatrix();
            }break;

            case "orotation":{
                setRotationMatrix(transformation.getAngle());
                Matrix=getRotationMatrix();
            }break;

            case "oscaling":{
                setScalingMatrix(transformation.getSx(),transformation.getSy());
                Matrix=getScalingMatrix();
            }break;

            case "reflection":{
                setReflectionMatrix(transformation.getReflection());
                Matrix=getReflectionMatrix();
            }break;

            case "fscaling":{
                setTranslationMatrix(transformation.getP().getX(),transformation.getP().getY());
                setScalingMatrix(transformation.getSx(),transformation.getSy());
                Matrix=MatrixMultiplication3x3(getTranslationMatrix(),getScalingMatrix());
                setTranslationMatrix((-1*transformation.getP().getX()),(-1*transformation.getP().getY()));
                Matrix=MatrixMultiplication3x3(Matrix,getTranslationMatrix());
            }break;

            case "frotation":{
                setTranslationMatrix(transformation.getP().getX(),transformation.getP().getY());
                setRotationMatrix(transformation.getAngle());
                Matrix=MatrixMultiplication3x3(getTranslationMatrix(),getRotationMatrix());
                setTranslationMatrix((-1*transformation.getP().getX()),(-1*transformation.getP().getY()));
                Matrix=MatrixMultiplication3x3(Matrix,getTranslationMatrix());
            }break;

            case "shear":{
                setShearMatrix(transformation);
                Matrix=getShearMatrix();
            }break;
        }

        return Matrix;
    }

    public double[][] MatrixMultiplication3x3(double[][] Matrix1,double[][] Matrix2){
        double[][] FinalMatrix=new double[3][3];

        for (int i=0;i<3;i++){
            for (int z=0;z<3;z++){
                for (int j=0;j<3;j++){
                    FinalMatrix[i][z]+=(Matrix1[i][j]*Matrix2[j][z]);
                }
            }
        }

        return FinalMatrix;
    }
}

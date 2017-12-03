package recommend;

import javafx.util.Pair;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import segmenter.ChineseSegmenter;
import segmenter.ChineseSegmenterImpl;
import tf_idf.TF_IDF;
import tf_idf.TF_IDFImpl;
import util.GetResult;
import vo.StockInfo;
import vo.UserInterest;

import java.util.List;

public class RecommenderImpl implements Recommender {

    /**
     * this function need to calculate stocks' content similarity,and return the similarity matrix
     *
     * @param stocks stock info
     * @return similarity matrix
     */
    private ChineseSegmenter segmenter=new ChineseSegmenterImpl();
    private TF_IDF tf_idf=new TF_IDFImpl();
    @Override
    public double[][] calculateMatrix(StockInfo[] stocks,int type) {
        //TODO: write your code here
        double[][] matrix=new double[stocks.length][stocks.length];
        List<String> words=segmenter.getWordsFromInput(stocks,type);
        Pair<String,Integer>[] pairs=tf_idf.getResult(words,stocks,type);
        for(int i=0;i<stocks.length;i++){
            for(int j=i+1;j<stocks.length;j++){
                MyWord[] myWord=new MyWord[20];
                for(int k=0;k<pairs.length;k++){
                    myWord[k]=new MyWord(pairs[k].getKey());
                    GetResult getX=new GetResult(type,stocks[i]);
                    Result resultX= getX.getResult();
                    List<Term> termsX=resultX.getTerms();
                    for(int m=0;m<termsX.size();m++){
                        if(termsX.get(m).getName().equals(myWord[k].myword))myWord[k].setX( myWord[k].getX()+1);
                    }
                    GetResult getY=new GetResult(type,stocks[j]);
                    Result resultY= getY.getResult();
                    List<Term> termsY=resultY.getTerms();
                    for(int m=0;m<termsY.size();m++){
                        if(termsY.get(m).getName().equals(myWord[k].myword))myWord[k].setY( myWord[k].getY()+1);
                    }
                }
                double Up=0,Downl=0,Downr=0;
                for(int k=0;k<pairs.length;k++){
                    Up=Up+myWord[k].getX()*myWord[k].getY();
                    Downl=Downl+Math.pow(myWord[k].getX(),2.0);
                    Downr=Downr+Math.pow(myWord[k].getY(),2.0);
                }
                matrix[i][j]=Up/(Math.sqrt(Downl)*Math.sqrt(Downr));
                matrix[j][i]=matrix[i][j];
            }
        }
        for(int i=0;i<stocks.length-1;i++){
            matrix[i][i]=0;
        }
        return matrix;
    }

    private class MyWord{
        private String myword;
        private double x;
        private double y;
        public void setWord(String word){myword=word;}
        public void setX(double x){this.x=x;}
        public void setY(double y){this.y=y;}
        public double getX(){return x;}
        public double getY(){return y;}
        public MyWord(){
            this.x=0;
            this.y=0;
            this.myword=null;
        }
        public MyWord(String word){
            this.x=0;
            this.y=0;
            this.myword=word;
        }
    }

    /**
     * this function need to recommend the most possibility stock number
     *
     * @param matrix       similarity matrix
     * @param userInterest user interest
     * @return commend stock number
     */
    @Override
    public double[][] recommend(double[][] matrix, UserInterest[] userInterest) {
        //TODO: write your code here
        double[][] rec=new double[1][10];
        for(int i=0;i<userInterest.length;i++){
            double[] number=new double[userInterest[i].getIsLike().length];
            int[] boo=userInterest[i].getIsLike();
            for(int j=0;j<userInterest[i].getIsLike().length;j++){//计算每个用户对各个文章的喜好度
                double temp=0;
                for(int k=0;k<boo.length;k++){
                    temp=temp+boo[k]*matrix[j][k];
                }
                number[j]=temp;
            }
            for(int j=0;j<10;j++){
                double max = 0;
                int queue = 0;
                for(int k=0;k<userInterest[i].getIsLike().length;k++) {//选取其中最大的值，记录其ID后将其归零，以便选取次大的值。
                    if (number[k] > max) {
                        number[queue] = max;
                        max = number[k];
                        queue = k;
                        number[k] = 0;
                    }
                }
                rec[i][j]=queue+1;
            }
        }
        return rec;
    }
}
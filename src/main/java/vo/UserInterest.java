package vo;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class UserInterest {
    private int[] IsLike;
    public UserInterest(int[] IsLike)
    {
        this.IsLike=IsLike;
    }
    public int[] getIsLike(){
        return IsLike;
    }
    public void setIsLike(int x,int interest){//更新用户喜好信息
        if(IsLike[x]<interest) {
            IsLike[x] = interest;
        }
        try {
            PrintWriter printWriter=new PrintWriter(new FileWriter("UserInterest.txt"));
            for(int i=0;i<60;i++){
                printWriter.print(IsLike[i]);
            }
            printWriter.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public double[][] ReadMatrix() throws IOException {
        FileReader fileReader=new FileReader("CloseMatrix.txt");
        Scanner scanner=new Scanner(fileReader);
        double[][] matrix=new double[60][60];
        for(int i=0;i<60;i++){
            for(int j=0;j<60;j++){
                if(scanner.hasNextDouble())
                    matrix[i][j]=scanner.nextDouble();
            }
        }
        scanner.close();
        return matrix;
    }
}

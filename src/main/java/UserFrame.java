import javafx.util.Pair;
import segmenter.ChineseSegmenter;
import segmenter.ChineseSegmenterImpl;
import vo.StockInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.List;

public class UserFrame extends MainFrame{
    private final JButton FileChooser;
    private final JRadioButton TitleChooser;
    private final JRadioButton ContentChooser;
    private final JRadioButton AnswerChooser;
    private final ButtonGroup NameChooser;
    private final JButton MakeSure1;
    private final JTextField Input;
    private String[] datas=new String[10];

    File file;
    MakeSureHandler makeSureHandler1=new MakeSureHandler(1);
    InputHandler inputHandler=new InputHandler();
    Pair<String,Integer>[] pairs;
    public UserFrame(){
        super("文件选取");
        setLayout(new FlowLayout());
        FileChooser=new JButton("选择文件");
        add(FileChooser);
        FileChooserHandler fileChooserHandler=new FileChooserHandler();
        FileChooser.addActionListener(fileChooserHandler);

        TitleChooser=new JRadioButton("按标题查找");
        ContentChooser=new JRadioButton("按内容查找");
        AnswerChooser=new JRadioButton("按回答查找");
        add(TitleChooser);
        add(ContentChooser);
        add(AnswerChooser);
        NameChooser=new ButtonGroup();
        NameChooser.add(TitleChooser);
        NameChooser.add(ContentChooser);
        NameChooser.add(AnswerChooser);
        TitleChooser.addItemListener(new NameChooserHandler(2));
        ContentChooser.addItemListener(new NameChooserHandler(6));
        AnswerChooser.addItemListener(new NameChooserHandler(8));

        MakeSure1=new JButton("确定数据类型");
        add(MakeSure1);
        MakeSure1.addActionListener(makeSureHandler1);

        Input=new JTextField(30);
        add(Input);
        Input.addActionListener(inputHandler);
    }

    /**文件选取手柄**/
    private class FileChooserHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser jfc = new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            jfc.showDialog(new JLabel(), "选择");
            file = jfc.getSelectedFile();
            stockInfos = fileHandler.getStockInfoFromFile(file.getPath());
        }
    }

    /**确定手柄**/
    private class MakeSureHandler implements ActionListener{

        private int type;
        private int make;
        public MakeSureHandler(int m){
            make=m;
        }
        public void setType(int t){
            type=t;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(file==null){
                JOptionPane.showMessageDialog(UserFrame.this,String.format("您还没有选择文件！"));
                return;
            }
            switch(make){
                case 1:
                    ChineseSegmenter chineseSegmenter= new ChineseSegmenterImpl();
                    List<String> lists=chineseSegmenter.getWordsFromInput(stockInfos,type);
                    pairs=tf_idf.getResult(lists,stockInfos,type);
                    JOptionPane.showMessageDialog(UserFrame.this,String.format("已完成关键字提取。"));
                    break;
            }
        }
    }

    /**选择数据类型手柄**/
    private class NameChooserHandler implements ItemListener{

        private int type;

        public NameChooserHandler(int t){
            type=t;
        }
        @Override
        public void itemStateChanged(ItemEvent e) {
            if(file==null){
                JOptionPane.showMessageDialog(UserFrame.this,String.format("您还没有选择文件！"));
                return;
            }
            makeSureHandler1.setType(type);
            inputHandler.setType(type);
        }
    }

    /**文本框输入手柄**/
    private class InputHandler implements ActionListener{

        int type;
        public void setType(int t){
            type=t;
        }

        private void setnum(double[] degree,double[] num,StockInfo est,int type){
            for(int i=0;i<stockInfos.length;i++){
                StockInfo[] stocks={stockInfos[i],est};
                double[][] matrix=recommender.calculateMatrix(stocks,type);
                degree[i]=matrix[0][1];
            }
            for(int j=0;j<10;j++){
                double max=0;
                int id=0;
                for(int k=0;k<degree.length;k++){
                    if(degree[k]>max){
                        degree[id]=max;
                        id=k;
                        max=degree[k];
                        degree[k]=0;
                    }
                }
                num[j]=id;
            }
            AddShow.clear();
            for(int i=0;i<10;i++){
                datas[i]=stockInfos[(int)num[i]].getTitle();
                AddShow.addElement(datas[i]);
            }
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand() == null) {
                JOptionPane.showMessageDialog(UserFrame.this, String.format("请输入一条数据！"));
                return;
            }
            double[] degree = new double[stockInfos.length];
            StockInfo est=null;
            switch (type){
                case 2:
                    est=new StockInfo("", e.getActionCommand(), "", "", "", "", "","");break;
                case 6:
                    est=new StockInfo("","","","","",e.getActionCommand(),"","");break;
                case 8:
                    est=new StockInfo("","","","","","","",e.getActionCommand());break;
                default:break;
            }
            double[] dnum=new double[10];
            setnum(degree, dnum, est, type);
            setNum(dnum);
        }
    }
}

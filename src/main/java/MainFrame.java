import recommend.Recommender;
import recommend.RecommenderImpl;
import tf_idf.TF_IDF;
import tf_idf.TF_IDFImpl;
import util.FileHandler;
import util.FileHandlerImpl;
import vo.StockInfo;
import vo.UserInterest;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class MainFrame extends JFrame {
    protected DefaultListModel<String> AddShow;
    protected DefaultListModel<String> AddMessage;
    protected JList<String> DataShow;
    protected JList<String> MessageShow;
    protected JScrollPane ScrollShow;
    protected JScrollPane ScroolMessageShow;
    protected StockInfo[] stockInfos=new StockInfo[60];
    protected TF_IDF tf_idf=new TF_IDFImpl();
    protected Recommender recommender=new RecommenderImpl();
    protected FileHandler fileHandler=new FileHandlerImpl();
    protected UserInterest userInterest=fileHandler.getUserInterestFromFile("UserInterest.txt");
    protected UserInterest userInterested[]={fileHandler.getUserInterestFromFile("UserInterest.txt")};

    protected int[] num = new int[10];
    public MainFrame(String s){
        super(s);
        AddShow=new DefaultListModel<>();
        DataShow=new JList<>(AddShow);
        ScrollShow=new JScrollPane(DataShow);
        ScrollShow.setSize(150,100);
        add(ScrollShow);
        DataShow.addMouseListener(new DataHandler());
        AddMessage=new DefaultListModel<>();
        MessageShow=new JList<>(AddMessage);
        ScroolMessageShow=new JScrollPane(MessageShow);
        ScroolMessageShow.setSize(150,100);
        add(ScroolMessageShow);
    }

    public void setStockInfos(StockInfo[] stocks){
        stockInfos=stocks;
    }

    public void setNum(double[] num){
        for(int i=0;i<10;i++){
            this.num[i]=(int)num[i];
        }
    }
    /**数据选定手柄**/
    private class DataHandler implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            JList thelist=(JList) e.getSource();
            int count=thelist.getSelectedIndex();
            /**双击显示词云**/
            if(e.getClickCount()==2){
                WordCloudFrame wordCloudFrame= null;
                userInterest.setIsLike(num[count],2);
                try {
                    wordCloudFrame = new WordCloudFrame(stockInfos,stockInfos[num[count]]);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                wordCloudFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                wordCloudFrame.setSize(960,480);
                wordCloudFrame.setVisible(true);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            JList thelist=(JList) e.getSource();
            int count=thelist.getSelectedIndex();
            /**鼠标释放显示详细信息**/
            AddMessage.clear();
            userInterest.setIsLike(num[count],1);
            for(int i=1;i<=8;i++) {
                AddMessage.addElement(stockInfos[0].get(i) + ":" + stockInfos[num[count]].get(i));
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}

import recommend.RecommenderImpl;
import segmenter.ChineseSegmenter;
import segmenter.ChineseSegmenterImpl;
import vo.StockInfo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class WordCloudFrame extends MainFrame{
    private JTextField ImageName;
    private static String imageString="image";
    private JButton SaveImage;
    private WordCloudBuilder wordCloudBuilder;
    private ChineseSegmenter segmenter=new ChineseSegmenterImpl();
    protected double[][] matrix=new double[60][60];

    public WordCloudFrame(StockInfo[] stocks,StockInfo stock) throws IOException {
        super("为你推荐");
        setLayout(new FlowLayout());
        try {
            matrix=userInterest.ReadMatrix();
        } catch (IOException e) {
            e.printStackTrace();
        }
        double[][] recommend=new RecommenderImpl().recommend(matrix,userInterested);
        setNum(recommend[0]);
        setStockInfos(stocks);
        Color[] colors = new Color[10];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = new Color(
                    (new Double(Math.random() * 128)).intValue() + 128,
                    (new Double(Math.random() * 128)).intValue() + 128,
                    (new Double(Math.random() * 128)).intValue() + 128);
        }
        wordCloudBuilder.buildWordCouldByWords(720,480,30,72,12,
                segmenter.getWordsFromInput(stock),
                new Color(-1),"data.png",colors);
        File imagefile=new File("data.png");
        BufferedImage bufferedImage= ImageIO.read(imagefile);
        ImageIcon imageIcon=new ImageIcon();
        imageIcon.setImage(bufferedImage);
        JLabel label=new JLabel(imageIcon);
        add(label);

        ImageName=new JTextField("图片名");
        ImageName.setSize(36,24);
        add(ImageName);
        ImageName.addPropertyChangeListener(new ImageNameHandler());

        SaveImage=new JButton("选择保存位置");
        add(SaveImage);
        SaveImage.addActionListener(new SaveImageHandler());

        AddShow.clear();
        for(int i=0;i<10;i++){
            AddShow.addElement(stockInfos[num[i]].getTitle());
        }
    }

    private class ImageNameHandler implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            imageString=evt.getPropertyName();
        }
    }
    private class SaveImageHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e){
            int length=2097152;
            JFileChooser jfc=new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
            jfc.showDialog(new JLabel(),"另存为");
            String path=jfc.getSelectedFile().getAbsolutePath()+"\\"+imageString+".png";
            File infile=new File("E:\\GitHub\\javahomework_3\\data.png");
            File outfile=new File(path);
            try {
                FileInputStream inputStream = new FileInputStream(infile);
                FileOutputStream outputStream=new FileOutputStream(outfile);
                FileChannel inChannel=inputStream.getChannel();
                FileChannel outChannel=outputStream.getChannel();
                ByteBuffer buffer=null;
                while(true){
                    if(inChannel.position()==inChannel.size()){
                        inChannel.close();
                        outChannel.close();
                    }
                    else if(inChannel.size()-inChannel.position()<length){
                        length=(int)(inChannel.size()-inChannel.position());
                    }
                    else{
                        length=2097152;
                    }
                    buffer=ByteBuffer.allocate(length);
                    inChannel.read(buffer);
                    buffer.flip();
                    outChannel.write(buffer);
                    outChannel.force(false);
                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            JOptionPane.showMessageDialog(WordCloudFrame.this,String.format("保存成功！"));
        }
    }

}

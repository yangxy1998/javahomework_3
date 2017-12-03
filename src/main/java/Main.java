import com.github.davidmoten.guavamini.Lists;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String[] words = new String[]{
                "词频","java","考试","小明","毕业"
        };
        List<String> s = Lists.newArrayList(words);
        Color[] colors = new Color[10];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = new Color(
                    (new Double(Math.random() * 128)).intValue() + 128,
                    (new Double(Math.random() * 128)).intValue() + 128,
                    (new Double(Math.random() * 128)).intValue() + 128);
        }
        UserFrame userFrame=new UserFrame();
        userFrame.setSize(720,300);
        userFrame.setVisible(true);
        userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        WordCloudBuilder.buildWordCouldByWords(200,200,4,20,10,s,new Color(-1),"data.png",colors);
    }
}






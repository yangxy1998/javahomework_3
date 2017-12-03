package segmenter;


import org.ansj.domain.Result;
import org.ansj.domain.Term;
import util.GetResult;
import vo.StockInfo;

import java.util.ArrayList;
import java.util.List;

public class ChineseSegmenterImpl implements ChineseSegmenter {

    /**
     * this func will get chinese word from a list of stocks. You need analysis stocks' answer and get answer word.
     * And implement this interface in the class : ChineseSegmenterImpl
     * Example: 我今天特别开心 result : 我 今天 特别 开心
     *
     * @param stocks stocks info
     * @return chinese word
     * @see ChineseSegmenterImpl
     */
    @Override
    public List<String> getWordsFromInput(StockInfo[] stocks,int type) {
        //TODO: write your code here
        List<String> list = new ArrayList<>();
        for(int i=1;i<stocks.length;i++){
            GetResult get=new GetResult(type,stocks[i]);
            Result result=get.getResult();
            List<Term> terms = result.getTerms();
            for(int j = 0; j<terms.size();j++){
                int isexist=0;
                for(int k=0;k<list.size();k++) {
                    if(terms.get(j).getName().equals(list.get(k))){
                        isexist=1;
                        break;
                    }
                }
                if(isexist==0)list.add(terms.get(j).getName());
            }
        }
        return list;
    }

    @Override
    public List<String> getWordsFromInput(StockInfo stockInfo) {
        List<String> list = new ArrayList<>();
        for(int i=0;i<8;i++){
            GetResult get=new GetResult(i+1,stockInfo);
            Result result=get.getResult();
            if(result==null)continue;
            List<Term> terms = result.getTerms();
            for(int j=0;j<terms.size();j++){
                list.add(terms.get(j).getName());
            }
        }
        return list;
    }
}
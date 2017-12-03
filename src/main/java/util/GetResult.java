package util;

import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.ToAnalysis;
import vo.StockInfo;

public class GetResult {
    private Result result;
    public GetResult(int type, StockInfo stockInfo){
        result=ToAnalysis.parse(stockInfo.get(type));
    }
    public Result getResult(){
        return result;
    }

}

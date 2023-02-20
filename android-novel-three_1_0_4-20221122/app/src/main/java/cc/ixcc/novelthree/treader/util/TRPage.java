package cc.ixcc.novelthree.treader.util;

import java.util.List;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class TRPage {
    //小说单页的类
    //开始处
    private long begin;
    //结束处
    private long end;
    //每一行的内容
    private List<String> lines;

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public List<String> getLines() {
        return lines;
    }

    public String getLineToString(){
        String text ="";
        if (lines != null){
            for (String line : lines){
                text += line;
            }
        }
        return text;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }
}

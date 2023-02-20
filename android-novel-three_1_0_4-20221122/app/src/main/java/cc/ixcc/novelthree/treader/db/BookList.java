package cc.ixcc.novelthree.treader.db;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/27.
 */
public class BookList extends DataSupport implements Serializable{
    private int id;
//    private String bookname;
    private String bookpath;
    private long begin;
//    private String charset;
    private int anid;
    private String title;
    private String coverpic;
    private int chaps;
    private String info;
    private String preId;
    private String nextId;
    private String is_shelve;

    public String getBookname() {
        return this.title;
    }

    public void setBookname(String title) {
        this.title = title;
    }

    public String getBookpath() {
        return this.bookpath;
    }

    public void setBookpath(String bookpath) {
        this.bookpath = bookpath;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public String getCharset() {
        return title;
    }

    public void setCharset(String charset) {
        this.title = charset;
    }

    public int getAnid() {
        return anid;
    }

    public void setAnid(int anid) {
        this.anid = anid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverpic() {
        return coverpic;
    }

    public void setCoverpic(String coverpic) {
        this.coverpic = coverpic;
    }

    public int getChaps() {
        return chaps;
    }

    public void setChaps(int chaps) {
        this.chaps = chaps;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
//
//    public int getPreId() {
//        return preId;
//    }
//
//    public void setPreId(int preId) {
//        this.preId = preId;
//    }


    public String getPreId() {
        return preId;
    }

    public void setPreId(String preId) {
        this.preId = preId;
    }

    public String getNextId() {
        return nextId;
    }

    public void setNextId(String nextId) {
        this.nextId = nextId;
    }

    public String getIs_shelve() {
        return is_shelve;
    }

    public void setIs_shelve(String is_shelve) {
        this.is_shelve = is_shelve;
    }
}
